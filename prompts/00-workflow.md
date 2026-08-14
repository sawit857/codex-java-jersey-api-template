Use `$api-workflow`.

Goal:
Implement the requested change in `spec/spec-api.md` through analysis, approved plan, production implementation, tests, independent review, client documentation, and final verification.

Required agents:

```text
api_analyst
api_developer
api_tester
api_reviewer
api_doc_writer
```

Required phases:

```text
Analyze -> Implement -> Test -> Review -> Document -> Final Verify
```

Constraints:

- follow `AGENTS.md`
- Java 8, Jersey 2.x, `javax.*`, Maven, WAR, Tomcat 9
- preserve unrelated user changes
- do not modify the specification silently
- do not skip gates
- route production defects to `api_developer`
- route test defects to `api_tester`
- stop for public-contract decisions

Required artifacts:

```text
docs/plans/<feature>-plan.md
docs/reviews/latest-review.md
docs/api/task-api.md
```

Completion requires fresh successful evidence from:

```bash
mvn clean verify
```

and a non-zero executed test count with zero failures/errors.
