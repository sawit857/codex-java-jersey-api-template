Resume the repository workflow safely.

Read:

```text
AGENTS.md
docs/workflow-state.md
current working tree
docs/plans/
docs/reviews/latest-review.md when present
```

Validate recorded state against actual files and command evidence.
Continue from the recorded next phase only. Do not repeat completed phases
unless evidence is missing/stale, and do not assume an earlier agent summary
is correct without inspection. Update `docs/workflow-state.md` after the phase.
