Use `$api-test`.

Specification:

```text
spec/spec-api.md
```

Approved plan:

```text
docs/plans/2026-07-19-task-api-plan.md
```

Delegate tests to `api_tester`.

Allowed:

```text
src/test/java/
src/test/resources/
pom.xml only for explicitly planned test configuration
```

Forbidden:

```text
src/main/
spec/
docs/
AGENTS.md
.codex/
.agents/
prompts/
```

Cover service, repository, and Jersey HTTP behavior for all six endpoints plus validation/not-found/invalid-status and relevant parsing/media-type errors.

Run focused tests, then:

```bash
mvn test
```

Confirm tests actually executed.

If valid tests expose production defects, retain them and return the issue to `api_developer` without editing production code.

Expected successful handoff:

```text
READY_FOR_REVIEW
```
