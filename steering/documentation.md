# Documentation Steering

## Documentation Layers

- Keep `README.md` as the repository landing page: explain what the library does, what problem it solves, how to install it, how to use it, system requirements, target compatibility, and what toolchain it is compiled with.
- Keep badges at the top of `README.md`, and keep their reference definitions at the bottom of the file.
- Keep `docs/` as the published MkDocs site for user-facing guidance.
- Keep Dokka as the generated API reference for public symbols and keep it aligned with MkDocs.
- Keep KDoc as the source of truth for API intent and behavior close to the code.
- Keep `docs/changelog/` as the public history of released behavior changes.

## KDoc Rules

- Add or update KDoc whenever public API behavior, parameters, return values, or examples change.
- Prefer concise KDoc that explains contract and behavior, not implementation trivia.
- Document one-shot behavior explicitly for APIs such as `tag`, `quiet`, `maxLogLength`, and `maxTagLength`.
- When platform behavior differs, describe the common contract in `commonMain` and keep platform-specific KDoc limited to the actual delta.
- Keep examples realistic and aligned with current public API names and signatures.

## Published Docs Rules

- Update `docs/` whenever a public behavior change affects setup, usage, platform expectations, or examples.
- Prefer short pages with runnable snippets over long narrative explanations.
- Keep the docs site aligned with the README, but avoid duplicating large sections verbatim.
- When adding a new concept page, wire it into `mkdocs.yml`.
- When changing examples in code or KDoc, check whether the same example appears in `README.md` or `docs/`.
- Always update Dokka together with MkDocs when public code changes anywhere in the repository.
- Reference concrete dependency versions from the project configuration whenever documentation mentions Kotlin, plugins, Android SDKs, or runtime/toolchain requirements.

## README Rules

- Keep the README optimized for first contact:
  - what the library does
  - what problem it solves
  - how to install it
  - a realistic implementation example
  - system requirements
  - target compatibility
  - which toolchain or versions it is compiled with
  - links to deeper docs
- Keep badges immediately below the title and keep reference definitions at the bottom of the file.
- Keep the README explanatory, not skeletal. It should let a new user understand whether the library fits their use case before they open the docs site.
- Avoid turning the README into a full manual. Move expanded guidance to `docs/`.

## Changelog Rules

- Generate each changelog page from the diff between the previous tag and the release tag being documented.
- Base the change summary on the commits and pull requests included in that tag-to-tag range.
- For each release page, include:
  - motivation for the release
  - bullet list of notable changes grouped from commits and PRs
  - compatibility cautions or migration notes
  - dependency/version table
  - target compatibility table
- Track version-by-version whether library capabilities were added, changed, or removed, and explain why.
- Track when compatibility increased or dropped, and call out the exact release where that happened.
- Reference concrete project dependency versions in changelog tables whenever they affect consumers or contributors.
- Do not ship a release with behavior changes that are undocumented in the changelog.

## Change Checklist

- Public API changed: update KDoc, README, MkDocs content, Dokka output, and changelog.
- User-visible behavior changed: update docs and changelog.
- Internal refactor only: update docs only if behavior or contributor guidance changed.
