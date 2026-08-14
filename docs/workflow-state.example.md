# Workflow State

Feature:

```text
<feature name>
```

Specification:

```text
spec/spec-api.md
```

Approved plan:

```text
docs/plans/<plan>.md
```

Current phase:

```text
ANALYSIS_REQUIRED | IMPLEMENTATION_REQUIRED | TESTING_REQUIRED |
REVIEW_REQUIRED | DOCUMENTATION_REQUIRED | FINAL_VERIFICATION_REQUIRED | COMPLETED
```

Completed phases:

- [ ] Analysis
- [ ] Implementation
- [ ] Testing
- [ ] Review
- [ ] Documentation
- [ ] Final verification

Latest evidence:

| Command/artifact | Result | Date/session |
|---|---|---|
| `mvn -DskipTests compile` | | |
| `mvn test` | | |
| `mvn clean verify` | | |
| `docs/reviews/latest-review.md` | | |
| `docs/api/task-api.md` | | |

Open issues/findings:

```text
NONE or finding IDs/details
```

Next action:

```text
<exact skill, agent, scope, and command>
```

Resume prompt:

```text
Read AGENTS.md, the specification, approved plan, and this workflow-state file.
Continue from the recorded current phase only.
Do not repeat completed phases unless their evidence is stale or affected by later changes.
```
