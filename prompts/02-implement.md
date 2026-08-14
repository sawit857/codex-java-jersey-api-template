Use `$api-implement`.

Approved plan:

```text
docs/plans/2026-07-19-task-api-plan.md
```

Specification:

```text
spec/spec-api.md
```

Delegate production implementation to `api_developer`.

Allowed:

```text
src/main/java/
src/main/webapp/
pom.xml only when explicitly required by the approved plan
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

Preserve user changes. Use Java 8 and `javax.*`. Do not add Spring, Jakarta, persistence, authentication, or unrelated dependencies.

Required verification:

```bash
mvn -DskipTests compile
```

Run relevant existing tests when practical, but do not modify them.

Expected successful handoff:

```text
READY_FOR_TEST_IMPLEMENTATION
```
