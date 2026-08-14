Use `$api-review`.

Specification:

```text
spec/spec-api.md
```

Approved plan:

```text
docs/plans/2026-07-19-task-api-plan.md
```

Review:

```text
src/main/
src/test/
pom.xml
src/main/webapp/WEB-INF/web.xml
```

Delegate the independent review to `api_reviewer` in read-only mode.

Compare contract, Java 8 compatibility, architecture, validation, repository behavior, exception mapping, information exposure, tests, Maven/Surefire, WAR/Tomcat configuration, and actual command evidence.

Every finding requires an ID, severity, exact file/symbol, specification, evidence, impact, recommendation, and owner.

The parent validates findings and saves the accepted report to:

```text
docs/reviews/latest-review.md
```

Expected successful handoff:

```text
READY_FOR_DOCUMENTATION
```

Do not proceed while an unresolved CRITICAL or HIGH finding remains.
