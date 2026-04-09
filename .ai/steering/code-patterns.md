# Code Patterns Steering

## API Design

- Keep `Lumber` as the single convenience entry point.
- Keep severity-specific helpers thin; they should delegate to the generic logging path instead of duplicating logic.
- Keep `Oak` as the customization seam for filtering and sinks.
- Preserve current naming unless a breaking API change is explicitly intended.

## One-Shot State

- Treat `tag`, `quiet`, `maxLogLength`, and `maxTagLength` as single-use overrides.
- Consume one-shot state even when a message is filtered out. This is current behavior and tests depend on it.
- Keep execution-context state isolated through `ThreadSafe<T>` rather than global mutable fields.

## Logging Pipeline

- Route all public logging paths through `prepareLog`.
- Apply behavior in this order:
  1. resolve tag and length overrides
  2. check `isLoggable` and `quiet`
  3. format the message
  4. merge throwable information
  5. split oversized output into numbered chunks
  6. emit through `log(level, tag, message, error)`
- Keep chunked messages deterministic by appending `#<index>` to the tag.

## Formatting Rules

- Keep the built-in formatter intentionally small; this project currently supports `%s` and `%d` only.
- Avoid introducing heavyweight formatting utilities unless there is a clear multiplatform need.
- Preserve blank-message semantics:
  - blank message and no throwable: drop the log
  - blank message with throwable: emit the stack trace
  - message plus throwable: append the stack trace separated by a blank line

## `Oak` Implementation Rules

- Custom `Oak` implementations should decide filtering in `isLoggable` and write output in `log`.
- `OakWood` is a dispatcher, not a sink. Do not add direct output behavior there.
- Do not plant `Lumber` into itself.
- Keep forest management operations simple: plant, uproot, uprootAll, and inspect via `forest()`.

## `DebugOak` Rules

- `DebugOak` should stay a sensible default backend for each platform.
- Platform files may differ in transport, but they should keep the same conceptual contract.
- Keep output readable for humans first; this library is optimized for developer logging, not structured event ingestion.

## Style Expectations

- Favor small functions and explicit names over clever abstractions.
- Prefer internal helpers for shared implementation details.
- Keep KDoc aligned with actual behavior; several public types are intended to be self-documenting through their KDoc.
