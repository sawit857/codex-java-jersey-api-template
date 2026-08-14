# Task API Implementation Plan

**Goal:** Implement the Task Management API described by `spec/spec-api.md` using Java 8, Jersey 2.x, in-memory storage, JUnit 5, and Tomcat 9 WAR packaging.

**Architecture:** `TaskResource` handles HTTP, `TaskService` owns business rules and timestamps, and `TaskRepository` abstracts defensive in-memory storage. Specific exception mappers implement documented client errors; a generic mapper is the final safe fallback.

## Scope

In scope:

- six documented endpoints
- Task/request/response/error models
- status filtering
- validation and exception mapping
- Java-time JSON configuration
- service, repository, and Jersey integration tests
- client-facing API documentation

Out of scope: authentication, database, pagination, frontend, Docker, and OpenAPI generation.

## Files and ownership

### Production: `api_developer`

- `pom.xml`
- `src/main/java/com/example/taskapi/config/*`
- `src/main/java/com/example/taskapi/resource/*`
- `src/main/java/com/example/taskapi/service/*`
- `src/main/java/com/example/taskapi/repository/*`
- `src/main/java/com/example/taskapi/model/*`
- `src/main/java/com/example/taskapi/dto/*`
- `src/main/java/com/example/taskapi/exception/*`
- `src/main/java/com/example/taskapi/mapper/*`
- `src/main/webapp/WEB-INF/web.xml`

### Tests: `api_tester`

- `src/test/java/com/example/taskapi/service/TaskServiceTest.java`
- `src/test/java/com/example/taskapi/repository/InMemoryTaskRepositoryTest.java`
- `src/test/java/com/example/taskapi/resource/TaskResourceTest.java`

### Review/documentation

- parent persists `docs/reviews/latest-review.md` after `api_reviewer`
- `api_doc_writer` owns `docs/api/task-api.md`

## Implementation sequence

1. Configure Maven Java 8, Jersey, Jackson JSR-310, JUnit 5, AssertJ, Mockito, Jersey Test, Surefire, and WAR plugin.
2. Define Task model/status and external DTOs.
3. Implement repository with `ConcurrentHashMap`, `AtomicLong`, sorting, and defensive copies.
4. Implement service validation, normalization, fixed-clock-friendly timestamps, status parsing, and DTO mapping.
5. Implement specific application exceptions.
6. Implement resource endpoints and correct HTTP responses.
7. Configure Jackson Java time, application singleton graph, exception mappers, and `/api/*` servlet mapping.
8. Add service tests.
9. Add repository isolation/concurrency tests.
10. Add Jersey tests for all endpoints and key errors.
11. Run `mvn clean verify`.
12. Perform independent review and generate client documentation.

## Key tests

- create valid/blank and default OPEN
- list empty/all/status-filter/invalid status
- get existing/not found
- update fields/not found/validation
- update status valid/invalid/not found
- delete existing/not found and empty 204 body
- malformed JSON and unsupported media type
- defensive repository copies and concurrent unique IDs

## Risks and mitigation

| Severity | Risk | Mitigation |
|---|---|---|
| MEDIUM | Java-time serialization returns timestamps | Register `JavaTimeModule` and disable timestamp serialization |
| MEDIUM | Framework errors use non-JSON defaults | Register `WebApplicationExceptionMapper` and test 400/415 |
| MEDIUM | Generic mapper masks specific errors | Register and test specific typed mappers |
| LOW | In-memory data is lost on restart | Explicitly document limitation |

## Blocking questions

NONE

## Verification

```bash
mvn -DskipTests compile
mvn test
mvn clean verify
```

## Handoff

READY_FOR_IMPLEMENTATION
