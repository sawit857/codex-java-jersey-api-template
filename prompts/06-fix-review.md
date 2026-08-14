Resolve accepted findings from:

```text
docs/reviews/latest-review.md
```

Rules:

1. Read the exact finding IDs and accepted ownership.
2. Production findings: use `$api-implement` and `api_developer`; do not modify valid tests.
3. Test findings: use `$api-test` and `api_tester`; do not modify production code unless a separate production finding authorizes it.
4. Public-contract ambiguity: stop for parent/user decision.
5. After every production fix, rerun affected focused tests and `mvn test`.
6. After corrections, use `$api-review` for a fresh read-only re-review.
7. Update `docs/reviews/latest-review.md` only after the parent validates the new report.
8. Do not proceed to documentation while CRITICAL/HIGH remains.

Return fixed finding IDs, changed files, actual command evidence, remaining findings, and next handoff.
