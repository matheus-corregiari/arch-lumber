# CI and releases

The CI workflows and commands are identical in Arch Lumber, Arch Android and Arch Event Observer.
The convention plugins select module tasks; `build-logic/ci.json` selects the runner and the isolated
CodeQL compiler. Coverage floors live in `gradle.properties` and must only increase as tests improve.

## Pull requests

Every PR runs CI, regardless of its destination. For a destination other than `master`, branch names
are unrestricted and no release version is reserved. A merge into a development branch does not
trigger another CI run; the next PR update does.

For `master`, Release Policy runs first and accepts only:

- `release/X.Y.Z`: the next major (`M+1.0.0`) or minor (`M.m+1.0`) from the latest stable remote tag.
- `hotfix/X.Y.Z`: the next patch (`M.m.p+1`).
- Optional `-rcN` uses the existing numeric RC convention; increasing RCs and promotion to the stable
  version are allowed, but returning to an RC after a stable release is rejected.

Versions have three components, no leading zeroes, and no `v` prefix. Duplicate or historical remote
tags are rejected. A failed remote query fails the policy. A repository without stable tags begins
at `release/1.0.0`.

## Required gates

| Check | Command or responsibility |
|---|---|
| Release Policy | Python policy unit tests and validation against remote tags |
| Coverage Gate | `./gradlew ciBuild ciCoverage`: assemble, tests, merged coverage verification |
| Static Analysis | `./gradlew ciLint`: Detekt, ktlint and available Android lint tasks |
| Docs Gate | `./gradlew ciDocs`, then `python -m mkdocs build --strict` |
| CodeQL | `./gradlew ciCodeql`: JVM/Android compilation; also analyzes Actions and Python |
| CI Gate | Requires successful completion of every gate, including policy |

`ciCoverage` already includes `ciTest`. There is no second test job. Projects with Apple targets use
macOS for build/tests/publication; Android uses Linux. The same build job owns all supported targets,
so JVM/Android/browser tests are not repeated on a second host. Windows local validation does not
prove Apple binaries; the macOS CI run does.

CodeQL has a separate checkout and compiler configuration. Its outputs are never published. Coverage
reports are uploaded as artifacts; Codecov receives master reports for visibility, while Gradle
enforces the actual gate. The Codecov upload is not the coverage threshold.

Configure branch rules to require `CI Gate`, `Coverage Gate` and `Static Analysis`, with branches up
to date, and retain CodeQL/code-quality merge protections. Requiring `CI Gate` prevents a skipped
downstream job from making a rejected release policy mergeable. Apply common gates to all PR targets;
only the version policy is specific to master. Administrator bypasses remain explicit exceptions.

## Master and publication

The push of a merged commit to `master` reruns the same CI. Tag creation waits for all gates, identifies
the merged PR, fetches remote tags again, and creates an annotated tag on that exact SHA. The release
GitHub App sends the tag so its push triggers `release.yml`. Pages deploys the already-built site.

The tag workflow requires the annotated remote tag, a matching merged PR, master ancestry and a
successful master CI run for the exact SHA. It publishes using the tag's exact version, first to Maven
Central and then to GitHub Packages, from a single host. It confirms publication coordinates before
creating the GitHub Release. No additional test/lint/coverage suite runs for the tag; native publication
tasks may compile/package their dependencies, reusing available Gradle outputs.

Tags and publication are serialized without canceling active releases. GitHub may replace a pending
run if several releases arrive together; resume the affected run explicitly and revalidate the version.
Queue order is not a version reservation. Never move, overwrite or delete an existing release tag to
recover a publication failure.

## Recovery

Use the Release workflow's manual dispatch with the existing tag and destination `central`, `github`,
`both`, or `release-only`. Skipped destinations must already contain every publication's POM; the
workflow verifies this before proceeding and checks both registries before creating the GitHub Release.
If Central is still processing a deployment, wait for that deployment rather than uploading it again.
Selecting `both` is only appropriate when neither destination has accepted the release.

Required secrets: `RELEASE_APP_ID`, `RELEASE_APP_PRIVATE_KEY`, `MAVEN_CENTRAL_USERNAME`,
`MAVEN_CENTRAL_PASSWORD`, `SIGN_KEY`, `SIGN_KEYID`, `SIGN_PASSWORD`; `CODECOV_TOKEN` is used for
reporting. The release App must be allowed to create tags by the tag ruleset. PR validation does not
use publication or signing secrets.

## Local commands

```sh
python -m unittest discover -s .github/scripts -p 'test_*.py'
./gradlew ciBuild ciCoverage
./gradlew ciLint ciDocs
python -m pip install -r .github/requirements-docs.txt
python -m mkdocs build --strict
```

Use JDK 21 and the project wrapper. Publication also has `ciPublicationManifest` for verifying the
complete list of Maven coordinates, without uploading packages.

## Coverage and Codecov

`build-logic/src/main/kotlin/arch-ci.gradle.kts` is the single source of report exclusions. It applies the same Kover filter
to every covered module and the root report. Only Android-generated `*.BuildConfig`, `*.R`
and `*.R$*` are excluded: they contain generated constants/resources, not application behavior.
Do not exclude DTOs, state classes, Compose functions or entire packages just to raise coverage.

```sh
./gradlew ciCoverage
# Root report only (automatically runs the required JVM/Android host tests):
./gradlew :koverXmlReport :koverHtmlReport :koverVerify
```

Open `build/reports/kover/html/index.html` locally. Codecov receives only
`build/reports/kover/report.xml`, with `disable_search: true`; automatic discovery would also find
module or older reports and could merge excluded classes back into the result.
There is deliberately no second `ignore` list in `codecov.yml`: Codecov consumes the already-filtered
XML. Its `ignore` patterns describe source paths, while Kover filters describe JVM class names.
An IDE coverage run or another coverage tool must use this Gradle report to share these exclusions.

Compare the same commit and line metric. Codecov's treatment of partially covered lines can differ
from Kover, so equal file scope does not promise identical percentages. Existing Gradle verification
rules remain authoritative; Codecov provides visibility rather than an additional threshold.
JVM/Android host execution supplies the coverage counters. Apple, JS and Wasm tests still run in
the platform test suite but do not add Kover coverage. Coverage uploads occur only after successful
master validation (release validation for Toolkit).

References: [Kover report filtering](https://kotlin.github.io/kotlinx-kover/gradle-plugin/#filtering-reports),
[Codecov file search](https://docs.codecov.com/docs/file-search) and
[Codecov path ignores](https://docs.codecov.com/docs/ignoring-paths).
