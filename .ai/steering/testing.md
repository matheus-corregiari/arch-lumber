# Testing Steering

## Test Categories

Treat tests as two intentional categories when adding or reorganizing coverage:

- behavior validation tests: verify the expected contract of the library through stable, assertive checks
- exploration tests: probe edge cases, target-specific behavior, regressions, and implementation-sensitive scenarios before or alongside behavior validation

Behavior validation tests are the default today. Exploration tests exist to discover risk, confirm assumptions, and justify new or refined behavior validation coverage when needed.

## Test Placement

- Prefer shared tests first.
- Default new tests to `lumber/src/commonTest`.
- Add target-specific tests only when the behavior cannot be validated in shared tests, or when the behavior intentionally differs by target.
- If a platform-specific implementation changes but the public contract does not, keep shared validation coverage and add only the narrow target-specific tests needed for the delta.
- Keep helper test doubles lightweight and local to the test source set.

## Scenario Selection

- Base tests on real usage scenarios and realistic implementations whenever possible.
- Search for concrete logging flows a consumer would actually use before inventing synthetic test cases.
- Prefer validating examples that match README, KDoc, and published documentation patterns.
- When exploring a bug or edge case, convert the confirmed finding into a stable behavior validation test if it represents expected library behavior.

## Behavior Validation Style

- Reset global logging state before and after each test with `Lumber.uprootAll()`.
- Use `RecordingOak`-style sinks to assert dispatched entries instead of relying on console output.
- Test behavior through the public API whenever possible.
- Keep assertions focused on contract, not incidental implementation details.

## Exploration Test Style

- Use exploration tests to investigate chunking edges, target-specific runtime differences, formatting ambiguities, concurrency assumptions, and compatibility boundaries.
- Keep exploration coverage close to the behavior under study.
- If an exploration test proves a stable requirement, follow it with a clearer behavior validation test.

## Naming And Readability

- Keep test names clear, direct, and specific about the expected behavior.
- Prefer names that describe outcome and context over internal mechanics.
- Keep names readable in reports; avoid unnecessary wording.
- Add short comments only when they materially improve understanding of setup, intent, or a non-obvious assertion.
- Do not add comments for obvious arrange-act-assert steps.

## Behaviors That Must Stay Covered

- severity helper dispatch
- raw `log` dispatch
- placeholder formatting
- blank-message handling
- throwable-only and throwable-plus-message cases
- chunking for oversized messages
- one-shot consumption of tag and length overrides
- `quiet(true)` affecting only the next log
- validation for invalid max lengths

## Regression Strategy

- When changing message assembly or filtering, add a test before refactoring.
- When changing platform defaults, preserve shared behavior in `commonTest` and add narrow target-specific tests only for the delta.
- If a fix changes externally visible output, update docs and tests in the same change.
- If a bug comes from a real consumer scenario, keep that scenario represented in the test suite.

## Review Bar

- A behavior change without tests is incomplete.
- A public API change without doc updates is incomplete.
- A target-specific test should explain why shared coverage is insufficient.
