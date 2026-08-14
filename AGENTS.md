# AGENTS.md

## Project purpose

This repository demonstrates a specification-driven Codex workflow for a Java 8 Jersey REST API.

The example application is a Task Management API. Keep the business domain intentionally small so repository users can focus on:

1. analysis before implementation
2. bounded custom subagent roles
3. reusable repository skills
4. test evidence
5. independent review
6. client-facing documentation
7. final verification

## Source of truth

The functional source of truth is:

```text
spec/spec-api.md
```

Rules:

- Do not invent endpoints, fields, status codes, error codes, or business rules.
- Do not change the external API contract unless the user explicitly requests a specification change.
- When source and specification disagree, report the conflict.
- Internal implementation ambiguity may use the smallest conservative choice when it does not change the public contract; record the assumption in the plan.
- Public-contract ambiguity is blocking.

Source precedence:

```text
1. spec/spec-api.md
2. explicit user decisions
3. approved implementation plan
4. production implementation
5. automated tests
6. API documentation
```

## Technology constraints

Required:

```text
Java              JDK 8
Build             Maven
REST              Jersey 2.x
REST namespace    javax.ws.rs.*
Servlet namespace javax.servlet.*
JSON              Jackson
Testing           JUnit 5
Assertions        AssertJ
API tests         Jersey Test Framework
Packaging         WAR
Runtime           Tomcat 9
```

Out of scope unless the user explicitly expands scope:

- Spring or Spring Boot
- `jakarta.*` APIs
- JPA or Hibernate
- external database
- Lombok
- MapStruct
- authentication/JWT
- Docker
- frontend code
- OpenAPI code generation

Do not add a dependency merely to reduce a few lines of simple code.

## Java 8 compatibility

All source and tests must compile with Java 8.

Do not use:

- `var`
- records
- sealed classes
- text blocks
- switch expressions
- pattern matching
- `List.of`, `Set.of`, `Map.of`
- `Optional.isEmpty`
- APIs introduced after Java 8

Use `java.time` where appropriate. Prefer injecting `java.time.Clock` into time-dependent services so tests can use a fixed clock.

## Architecture

Expected request flow:

```text
HTTP request
    -> TaskResource
    -> TaskService
    -> TaskRepository
    -> InMemoryTaskRepository
```

Package responsibilities:

```text
config       Jersey configuration and JSON provider
resource     HTTP/JAX-RS handling
service      business rules and validation
repository   storage abstraction and in-memory implementation
model        internal domain model and enum
dto          external request and response models
exception    expected application exceptions
mapper       JAX-RS exception mappers
```

Layer rules:

- Resources handle HTTP concerns and call services.
- Resources must not access the repository directly.
- Services own business validation, task existence, timestamps, and orchestration.
- Repositories own storage operations only.
- Request DTOs must not expose server-managed fields.
- Internal model objects must not be accepted directly as HTTP request DTOs.
- Exception mappers convert failures to the documented JSON error contract.
- Avoid speculative abstractions.

## REST rules

Use `javax.ws.rs.*` and `javax.ws.rs.core.*`.

Each JSON endpoint must define suitable `@Consumes` and/or `@Produces` behavior.

Required success behavior:

- create: `201 Created`, response entity, and `Location` header
- get/list/update/status update: `200 OK`
- delete: `204 No Content` with no response entity

Do not return `200` for all outcomes.

Do not expose stack traces, Java exception names, package names, filesystem paths, or raw internal error messages to clients.

## Validation rules

Validation behavior must follow `spec/spec-api.md`.

General expectations:

- missing or blank title is rejected
- supported status values are exact enum names
- unsupported status values are rejected
- missing resources are distinct from invalid input
- failed validation must not partially change stored data
- normalize text consistently when trimming is allowed

Business validation belongs in the service layer unless it is strictly an HTTP parsing concern.

## Repository rules

The default implementation uses:

```java
ConcurrentHashMap<Long, Task>
AtomicLong
```

Requirements:

- IDs are unique within the application process.
- Repository methods must not expose mutable internal map views.
- Repository returns defensive copies of mutable tasks.
- Tests start with fresh repository state.
- No files, static global cache, external service, or persistence layer is added.

## Testing rules

Required levels:

1. `TaskServiceTest`
2. `InMemoryTaskRepositoryTest`
3. `TaskResourceTest` using Jersey Test Framework

Rules:

- Use JUnit 5.
- Use AssertJ.
- Use Mockito only where a real boundary benefits from isolation.
- Do not mock DTOs, collections, `Task`, `TaskStatus`, or the class under test.
- Do not test private methods directly.
- Do not depend on test execution order.
- Do not use arbitrary sleeps.
- Do not disable a valid failing test to complete a task.
- For a defect, add or preserve a test that reproduces the specified behavior.
- Test actual response body, headers, media type, and status when defined by the contract.

Preferred naming:

```text
methodName_condition_expectedResult
```

## Build and verification

Commands from repository root:

```bash
mvn -DskipTests compile
mvn test
mvn clean verify
```

Platform wrappers:

```text
macOS/Linux  scripts/verify.sh
Windows      scripts/verify.cmd
```

Do not report completion when `mvn clean verify` fails.

A successful Maven process with `Tests run: 0` does not pass the testing gate.

If verification cannot run:

1. report the exact command
2. report the relevant output
3. classify code, configuration, dependency-resolution, or environment failure
4. do not claim tests or build passed

## Workflow

Required complete lifecycle:

```text
Analyze
-> approved plan
Implement
-> compile evidence
Test
-> executed tests
Review
-> accepted review gate
Document
-> verified client document
Final verify
-> mvn clean verify
```

Available skills:

```text
$api-workflow
$api-analyze
$api-implement
$api-test
$api-review
$api-document
```

Do not skip phases for non-trivial work.

## Custom subagent responsibilities

### `api_analyst`

- read-only repository and specification exploration
- coverage matrix
- exact impacted files
- test scenarios
- risks and blocking questions
- returns plan content to the parent agent

The parent validates and persists an approved plan under `docs/plans/`.

### `api_developer`

Primary ownership:

```text
src/main/
pom.xml only for approved production/build changes
```

The developer must not modify tests to make an incorrect implementation pass.

### `api_tester`

Primary ownership:

```text
src/test/
pom.xml only for approved test configuration
```

The tester must retain valid failing tests that expose a production defect and route the defect to the developer.

### `api_reviewer`

- read-only
- reviews specification, plan, source, tests, configuration, and command evidence
- reports concrete findings with severity and ownership
- returns report content to the parent agent

The parent validates and persists the accepted report under `docs/reviews/`.

### `api_doc_writer`

Primary ownership:

```text
docs/api/
```

The writer documents only behavior verified by specification, accepted decisions, implementation, tests, and review.

## Multi-agent coordination

- Only one writing agent owns a file at a time.
- Do not run `api_developer` and `api_tester` concurrently over overlapping configuration files.
- Read-only analysis and review may run in parallel only when scopes are independent.
- Do not ask multiple agents to implement the same feature.
- The parent agent remains responsible for checking files and command evidence.
- Subagent summaries are not final evidence.
- Do not recursively create further subagents unless explicitly requested.

## Artifact locations

```text
docs/plans/       approved implementation plans
docs/reviews/     accepted review reports
docs/api/         client-facing API documentation
```

Do not place planning notes in `src/`.

Do not commit temporary scratchpad notes.

## Working tree safety

Before editing:

1. inspect the current working tree
2. identify user modifications
3. read relevant source and tests
4. choose the smallest complete change

Do not:

- run `git reset --hard`
- run destructive `git clean`
- discard user changes
- rewrite history
- force-push
- delete unknown files
- commit or push unless explicitly requested
- add secrets, tokens, private keys, certificates, or machine-specific credentials

## Review severity

### CRITICAL

Severe security exposure, destructive data loss, complete service compromise, or equivalent catastrophic impact.

### HIGH

Breaks the documented API contract, causes major runtime/build failure on the required platform, permits invalid state, or exposes meaningful sensitive internals.

### MEDIUM

Important test gap, realistic concurrency defect, significant validation/error-handling problem, important architecture violation, or material maintainability risk.

### LOW

Minor clarity, consistency, duplication, or non-blocking test improvement.

Every finding must include:

- ID
- severity
- exact file and symbol/location
- specification requirement
- evidence
- concrete impact
- smallest recommended correction
- suggested owner

Do not report personal style preferences as HIGH.

## Definition of done

A task is complete only when all applicable conditions are satisfied:

```text
[ ] Implementation matches spec/spec-api.md.
[ ] No undocumented endpoint or field was added.
[ ] Java 8 compilation passes.
[ ] Unit and repository tests pass.
[ ] Jersey integration tests pass.
[ ] Tests actually executed.
[ ] No unresolved CRITICAL or HIGH finding remains.
[ ] Client API documentation is current.
[ ] Accepted review report is current.
[ ] mvn clean verify passes.
[ ] No unexplained placeholder or TODO remains.
```

## Final report

The final response must state:

1. summary
2. exact changed files
3. tests added or updated
4. commands actually executed and results
5. review status
6. documentation status
7. unresolved issues or environment limits

Do not say “should pass” or “appears complete.” State what was verified.
