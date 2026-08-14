# Prompt library

Prompt files are work orders for a current case.

```text
AGENTS.md       = repository policy
SKILL.md        = reusable procedure
agent TOML      = specialized worker
prompt file     = task input and scope
```

## Usage

Portable form:

```text
Read prompts/<path>.md and execute it.
```

Skill form:

```text
$api-analyze
$api-implement
$api-test
$api-review
$api-document
$api-workflow
```

In Codex environments that expose custom prompts as slash commands, those may
appear as `/prompts:<name>`. This repository intentionally remains usable
without relying on slash-command installation.

## Phase prompts

| File | Purpose |
|---|---|
| `00-workflow.md` | Complete lifecycle |
| `01-analyze.md` | Read-only analysis and plan proposal |
| `02-implement.md` | Approved production implementation |
| `03-test.md` | Specification-based tests |
| `04-review.md` | Independent read-only review |
| `05-document.md` | Verified client documentation |
| `06-fix-review.md` | Route and verify accepted findings |

## Case prompts

| Category | Files |
|---|---|
| Feature | `new-api`, `add-endpoint`, `add-field` |
| Bugfix | `investigate-bug`, `fix-defect` |
| Refactor | `safe-refactor`, `legacy-cleanup` |
| Review | `code-review`, `security-review`, `performance-review` |
| Documentation | `api-doc`, `release-note` |
| Release | `pre-release-check`, `release-summary` |
| Maintenance | `resume-workflow`, `final-verification` |

Complete copy/paste examples are in:

```text
docs/PROMPT-COOKBOOK-TH.md
```

Replace placeholders, plan paths, output paths, and scope before executing.
