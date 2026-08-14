---
name: api-implement
description: Implement an approved Java 8 Jersey API plan. Use after analysis approval. Delegate production changes to api_developer, enforce ownership and Java 8 constraints, preserve user edits, and require compile evidence before testing.
---

# API Implement

## Preconditions

- approved plan path is explicit
- specification and plan agree
- no blocking question remains
- requested scope is covered by the plan
- working tree was inspected

Stop with a blocking status when any precondition fails.

## Ownership

Allowed for `api_developer`:

```text
src/main/java/
src/main/webapp/
pom.xml only for planned production/build changes
```

Forbidden:

```text
src/test/
spec/
docs/reviews/
docs/api/
AGENTS.md
.codex/
.agents/
prompts/
```

## Procedure

1. Read `AGENTS.md`, specification, approved plan, `pom.xml`, relevant source, and related tests.
2. Inspect modified/untracked files and preserve unrelated user changes.
3. Validate planned file ownership.
4. Delegate a bounded production-only task to `api_developer`.
5. Inspect the resulting diff; do not trust the summary alone.
6. Confirm planned behavior and Java 8/`javax` compatibility.
7. Run `mvn -DskipTests compile`.
8. Run relevant existing tests when practical without modifying them.
9. Classify code, build configuration, dependency resolution, or environment failures.
10. Return exact evidence and handoff.

## Output handoff

Success:

```text
READY_FOR_TEST_IMPLEMENTATION
```

Other statuses:

```text
RETURN_TO_API_DEVELOPER
REQUIRES_PARENT_DECISION
BLOCKED
```

Use `references/implementation-checklist.md`.
