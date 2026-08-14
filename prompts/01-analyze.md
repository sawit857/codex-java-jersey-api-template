Use `$api-analyze`.

Analyze `spec/spec-api.md` against the current repository.

Scope:

- all Task API endpoints
- request/response DTOs
- service validation and timestamps
- repository behavior and defensive copies
- exception mapping and error contract
- Java 8/Jersey/Maven/WAR compatibility
- unit, repository, and Jersey integration tests
- documentation impact

Delegate repository exploration to `api_analyst`.

Remain read-only. Do not modify source, tests, specification, or documentation.

Return:

- current state and exact evidence
- specification coverage matrix
- files to create/modify/read only
- ordered implementation sequence and ownership
- positive/error/regression test scenarios
- risks, internal assumptions, and blocking questions
- exact verification commands
- handoff status

Expected successful status:

```text
READY_FOR_IMPLEMENTATION
```
