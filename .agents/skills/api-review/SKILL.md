---
name: api-review
description: Perform an independent read-only review of a Java 8 Jersey API after code and tests exist. Compare spec, approved plan, code, tests, configuration, and command evidence; validate findings and route them to the correct owner.
---

# API Review

## Procedure

1. Read `AGENTS.md`, specification, approved plan, source, tests, configuration, and available command evidence.
2. Identify the review baseline/current change set.
3. Delegate bounded read-only review to `api_reviewer`.
4. Validate every finding against actual paths, symbols, assertions, contract, and command output.
5. Remove speculation, duplication, personal style, unrelated findings, and out-of-scope requirements.
6. Build the specification implementation/test coverage matrix.
7. Confirm severity and owner.
8. Evaluate the review gate.
9. Parent persists the accepted report to `docs/reviews/latest-review.md`.

## Gate

`READY_FOR_DOCUMENTATION` requires:

- no unresolved CRITICAL finding
- no unresolved HIGH finding
- acceptable contract implementation
- sufficient test and build evidence
- no blocking ambiguity

Other handoffs:

```text
RETURN_TO_API_DEVELOPER
RETURN_TO_API_TESTER
REQUIRES_PARENT_DECISION
BLOCKED
```

The reviewer remains read-only and never fixes its own findings.

Use `references/review-checklist.md`.
