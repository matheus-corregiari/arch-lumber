# Contributing

## Working style

- keep changes small
- keep examples real
- keep docs aligned with the public API
- use the Gradle wrapper for local validation

## Local validation

```bash
./gradlew :lumber:assemble
./gradlew :lumber:allTests
./gradlew :lumber:dokkaGeneratePublicationHtml
mkdocs build --strict
```

Use the same wrapper and toolchain settings as the project when checking docs or examples.

## What to update with code

- README
- KDocs
- API reference landing page and generated Dokka HTML under `docs/api/reference/` when public API docs change
- changelog entry
- API examples if the public behavior changed

## Documentation rules

- Keep KDoc aligned with the shipped behavior and signatures.
- Update both MkDocs and the Dokka HTML output when setup, examples, API, or platform behavior changes.
- Keep dependency versions, Android compatibility notes, and toolchain references aligned with project configuration.
- Generate each release changelog from the diff between the previous tag and the current release tag.
- Keep repository-level engineering conventions in `.ai/steering/` and treat them as the source for contributor-facing conventions.
