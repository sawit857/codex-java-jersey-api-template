---
name: api-test
description: Create and execute specification-based JUnit 5 and Jersey tests for a Java 8 API. Use after production implementation. Delegate to api_tester, preserve valid failing tests, verify non-zero discovery, and route production defects back to api_developer.
---

# API Test

## Preconditions

- specification and approved plan exist
- production implementation exists
- public contract is clear
- test ownership/configuration can be identified
- working tree was inspected

## Ownership

Allowed:

```text
src/test/java/
src/test/resources/
pom.xml only for planned test dependencies/plugins
```

Production source and specification are forbidden.

## Procedure

1. Read instructions, spec, plan, source, existing tests, and `pom.xml`.
2. Build a requirement-to-test coverage matrix from actual assertions.
3. Define service, repository, and Jersey integration scenarios.
4. Delegate bounded test work to `api_tester`.
5. Inspect test diff and any test-specific `pom.xml` change.
6. Confirm no production/spec change, weakened assertion, arbitrary sleep, or hidden disabled test.
7. Run focused test classes.
8. Run `mvn test` and confirm tests executed (`Tests run > 0`).
9. Classify every failure: production defect, test defect, test configuration, build configuration, environment, or specification conflict.
10. Preserve valid failing tests and route production defects to `api_developer`.

## Handoff

```text
READY_FOR_REVIEW
RETURN_TO_API_DEVELOPER
REQUIRES_PARENT_DECISION
BLOCKED
```

Use `references/test-checklist.md`.
