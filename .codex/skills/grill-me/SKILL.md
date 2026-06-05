---
name: grill-me
description: Interview the user relentlessly about a plan or design until reaching shared understanding, resolving each branch of the decision tree. Use when user wants to stress-test a plan, get grilled on their design, or mentions "grill me".
---

# Grill Me

## Interview
- Interview the user relentlessly about every aspect of this plan until we reach a shared understanding.
- Walk down each branch of the design tree, resolving dependencies between decisions one-by-one
- Ask the questions one at a time.
- For each question: provide a recommended answer + at least 2 alternatives
- If a question can be answered by exploring the codebase, explore the codebase instead.

## Conclusion after all questions solved
- Present a summary key points
- Ask if user wants:

1. Implement plan
2. Export plan as MD file at `.codex/grill/<name>.md`
