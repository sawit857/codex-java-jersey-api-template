# Prompt library

Prompts contain the input and scope for a current task. Reusable procedure belongs in `.agents/skills/`; repository policy belongs in `AGENTS.md`; specialized worker behavior belongs in `.codex/agents/`.

Usage:

```text
Read prompts/<file>.md and execute it.
```

Prompt list:

| File | Purpose |
|---|---|
| `00-workflow.md` | Full lifecycle |
| `01-analyze.md` | Read-only analysis and plan proposal |
| `02-implement.md` | Approved production implementation |
| `03-test.md` | Specification-based tests |
| `04-review.md` | Independent read-only review |
| `05-document.md` | Verified client documentation |
| `06-fix-review.md` | Route and verify accepted findings |
| `feature/new-api.prompt.md` | New API feature starter |
| `bugfix/fix-defect.prompt.md` | Root-cause and regression flow |
| `refactor/safe-refactor.prompt.md` | Behavior-preserving refactor |

Replace plan paths and scope in prompts when using them for a different feature.
