# Documentation checklist

## Gate
- [ ] accepted review exists
- [ ] handoff permits documentation
- [ ] no unresolved CRITICAL/HIGH
- [ ] no contract conflict

## Models/conventions
- [ ] media type and date format
- [ ] TaskResponse
- [ ] CreateTaskRequest
- [ ] UpdateTaskRequest
- [ ] UpdateTaskStatusRequest
- [ ] ErrorResponse
- [ ] server-managed fields identified
- [ ] status values exact

## Endpoints
- [ ] POST `/api/tasks`: 201 + Location
- [ ] GET `/api/tasks` and status filter
- [ ] GET `/api/tasks/{taskId}`
- [ ] PUT `/api/tasks/{taskId}`
- [ ] PATCH `/api/tasks/{taskId}/status`
- [ ] DELETE `/api/tasks/{taskId}`: 204 + no body
- [ ] request/response/errors/examples/client notes

## Accuracy
- [ ] valid JSON/no comments/trailing commas
- [ ] exact DTO field names
- [ ] exact enum/error/status values
- [ ] no invented auth/pagination/sorting/database/retry rules
- [ ] known limitations are verified
- [ ] changes limited to `docs/api/`
- [ ] handoff accurate
