Perform final verification only.

Read AGENTS.md, the approved plan, latest review, API documentation, current diff,
and verification scripts. Run:

```bash
mvn clean verify
```

Confirm tests run > 0, failures = 0, errors = 0, required artifacts exist,
no unresolved CRITICAL/HIGH remains, and no unauthorized/generated/secret/debug
changes exist. Return COMPLETED only with fresh evidence; otherwise return
BLOCKED or REQUIRES_PARENT_DECISION with exact details.
