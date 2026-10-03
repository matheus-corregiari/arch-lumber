# Binary compatibility

Starting with 1.5.0, public ABI dumps from the
[Kotlin ABI validator](https://kotlinlang.org/docs/gradle-binary-compatibility-validation.html)
are reviewed in source control. CI runs
`ciCompatibility` on the macOS release runner for JVM, Android, JS, Wasm JS and the
published iOS targets. Changes to the dumps require review; regenerating a dump is not evidence
that a removed signature is compatible.

The isolated CodeQL build uses `-PcodeqlAnalysis=true` to omit ABI configuration that its older
runtime cannot load. The release build and Coverage Gate always validate ABI with the normal
compiler. CodeQL still compiles and analyzes JVM and Android sources without rewriting ABI DSL.

```bash
./gradlew ciCompatibility
```

## Published contract regression

Published JVM JARs confirm that 1.1.0 exposed `OakWood.tag(String): Oak`. In 1.2.0 the API
changed to `TaggedLumber` and removed the option methods from `Oak`. Published 1.4.4 lacks the
old JVM descriptors. An already compiled consumer therefore fails with `NoSuchMethodError`.

EasyNavigation core JVM 1.1.0 declares Lumber **1.0.3** in its POM, rather than 1.1.0.
The cached published Lumber JVM JARs for 1.0.3 and 1.1.0 have identical SHA-1
`fe2ab4873a8590f9b5ccb30cf99178d70c4b71aa`. `NavigationController` bytecode invokes
`OakWood.tag(String): Oak`, followed by `Oak.error`, including its throwable overload.

1.5.0 restores the base methods and uses covariant overrides to retain both old and current
JVM return descriptors. Current Kotlin callers still receive `TaggedLumber` from `Lumber.tag`.
The legacy protected option getters are restored, including their consume-on-read behavior and
support for subclass overrides. `Oak.tag` now creates a `TaggableOak` with an immutable tag and
the original logging destination. Its tag persists across calls. One-shot options are stored in
`Oak`'s single `atomicOptions` state, transferred when creating a facade, and consumed by its
next log attempt, including a filtered or suppressed attempt. The public, open `TaggableOak`
lives in its own file and inherits the fluent option methods. The nested `TaggedLumber` remains
only as a compatibility adapter for its published JVM name and method return types.
Forest facades retain the current persistent tag and one-shot option semantics. This restores
linkage for the tested calls; it does not reproduce the old one-shot tag implementation.

## Consumer validation

`lumber/src/compatibility/java/Consumer.java` is compiled against published Lumber JVM 1.1.0.
It covers navigation logging calls, restored option methods, calls through an `Oak` reference,
and subclass overrides that invoke all four protected superclass getters. It verifies consumption
of the one-shot quiet and length options; a direct tagged facade now keeps its tag instead.
`CurrentConsumer.java` is compiled against published 1.4.4 to protect the current facade
descriptors. Each compiled class runs against both the candidate JVM JAR and the `classes.jar`
inside the candidate Android AAR published to the build-directory Maven repository. Neither
consumer is compiled against candidate classes. Runtime assertions verify tags, messages,
throwables, suppression and chunking. A negative control requires the old consumer to fail
with `NoSuchMethodError` on published 1.4.4.

This is a JVM linkage test of Android classes, not an Android device test. It exercises the
navigation logging contract, not the complete EasyNavigation UI. JS/Wasm and iOS ABI checks
protect future changes; they do not establish execution compatibility with old released KLIBs.
Windows cannot execute iOS tests or link the Apple frameworks. Unsupported-target ABI inference
is disabled; validation must fail when a complete dump cannot be generated. The macOS CI gate
must also build the frameworks and run the available native tests.

For an intentional API addition, review the change and run `ciUpdateAbi` on a host
that supports all published targets. Do not accept a dump update to conceal removed API.

## Shared build convention

`arch-compatibility` owns ABI validation and JVM/optional Android published-consumer checks.
It derives Maven coordinates from the library's publication. Each library configures its released
consumer main classes and API generation boundaries through `CompatibilityExtension`; the fixture directory and
Java toolchain version are configurable. It uses the shared `LocalPath` publication repository.
Fixtures belong in the library's `src/compatibility/java`, outside its published source sets.
`ciCompatibility` aggregates module checks, and `ciCoverage` includes it automatically. Neither
the shared workflow nor the contributor commands need library-specific task paths.

Release versions come from stable Git tags (`X.Y.Z`, optionally prefixed by `v`), ordered
numerically. In this library's Gitflow, a release tag identifies a released version. The legacy
fixture uses the latest tag before the 1.2.0 API change; the current fixture uses the latest
release tag before the candidate version. CI checks out the tags; local clones must fetch them.
Missing tags fail the compatibility task, while unrelated tasks such as `help` still work in
shallow clones used by automatic dependency submission.
The 1.4.4 negative control stays fixed because it reproduces a specific known regression.

For example, EasyNavigation's already compiled bytecode asks for `OakWood.tag(String): Oak`.
Recompiling application sources with a return type of `TaggedLumber` does not rewrite that
dependency. The consumer check compiles against the selected old release and runs that exact
bytecode with the candidate. ABI dumps separately detect signature changes relative to the
reviewed baseline; updating a baseline alone does not prove that old bytecode still works.

## Local release evidence

On Windows, the release work validated JVM, Android host, JS browser and Wasm browser tests:
203 tests on each target, 812 total, with no failures. Coverage verification passed.
`ciBuild` assembled available targets and compiled the three iOS KLIBs; Apple framework linking
and native test execution were skipped by the toolchain. Those remain macOS CI responsibilities.
Both generations of released consumers passed on the candidate JVM and Android publications,
and the negative control reproduced the failure on 1.4.4. These results cover the tested logging
contract; no execution compatibility claim is made for old JS/Wasm/iOS KLIB consumers.

The initial macOS CI run also linked release frameworks for all three iOS targets, passed
`iosSimulatorArm64Test`, and passed ABI and consumer checks. `iosX64Test` was skipped on the
Apple Silicon runner; its framework and KLIB were built, but its tests were not executed.
