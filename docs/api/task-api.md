# Task Management API

## 1. Overview

The Task Management API creates, lists, retrieves, updates, changes status, and deletes tasks using JSON over HTTP.

Base URL placeholder:

```text
{baseUrl}/api/tasks
```

Example Tomcat deployment:

```text
http://localhost:8080/task-api/api/tasks
```

Authentication is not implemented. Data is stored in application memory and is lost when the application restarts.

## 2. Common conventions

| Item | Value |
|---|---|
| Request/response media type | `application/json` |
| Date-time | ISO-8601 local date-time, e.g. `2026-07-19T22:30:00` |
| Task ID | Positive server-generated integer |
| Status serialization | Exact uppercase enum name |
| Delete success body | Empty |

Common error:

```json
{
  "code": "TASK_NOT_FOUND",
  "message": "Task 99 was not found",
  "timestamp": "2026-07-19T22:35:00",
  "path": "/task-api/api/tasks/99"
}
```

## 3. Data models

### TaskResponse

| Field | Type | Required | Server managed | Description |
|---|---|---:|---:|---|
| `id` | number | Yes | Yes | Identifier |
| `title` | string | Yes | No | Normalized task title |
| `description` | string/null | Yes | No | Optional task detail |
| `status` | string | Yes | On create | Task status |
| `createdAt` | string | Yes | Yes | Creation time |
| `updatedAt` | string | Yes | Yes | Last update time |

### CreateTaskRequest / UpdateTaskRequest

| Field | Type | Required | Description |
|---|---|---:|---|
| `title` | string | Yes | Non-blank after trimming |
| `description` | string/null | No | Blank becomes null |

### UpdateTaskStatusRequest

| Field | Type | Required | Description |
|---|---|---:|---|
| `status` | string | Yes | Documented status value |

### ErrorResponse

| Field | Type | Required | Description |
|---|---|---:|---|
| `code` | string | Yes | Stable error code |
| `message` | string | Yes | Safe human message |
| `timestamp` | string | Yes | Error time |
| `path` | string | Yes | Request URI path |

## 4. Task status values

- `OPEN`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`

The API does not enforce transition restrictions.

## 5. Endpoints

### 5.1 Create Task

```http
POST /api/tasks
Content-Type: application/json
Accept: application/json
```

```json
{
  "title": "Prepare API specification",
  "description": "Write client documentation"
}
```

Success:

```http
201 Created
Location: {baseUrl}/api/tasks/1
```

```json
{
  "id": 1,
  "title": "Prepare API specification",
  "description": "Write client documentation",
  "status": "OPEN",
  "createdAt": "2026-07-19T22:30:00",
  "updatedAt": "2026-07-19T22:30:00"
}
```

Errors: `400 VALIDATION_ERROR`, `415 UNSUPPORTED_MEDIA_TYPE`, `500 INTERNAL_ERROR`.

### 5.2 List Tasks

```http
GET /api/tasks
GET /api/tasks?status=OPEN
```

Success: `200 OK` with an array. An empty result is `[]`.

Invalid status: `400 INVALID_TASK_STATUS`.

### 5.3 Get Task

```http
GET /api/tasks/{taskId}
```

Success: `200 OK` with `TaskResponse`.

Missing task: `404 TASK_NOT_FOUND`.

### 5.4 Update Task

```http
PUT /api/tasks/{taskId}
Content-Type: application/json
```

```json
{
  "title": "Prepare final API specification",
  "description": "Include error examples"
}
```

Success: `200 OK` with updated response. Status and creation time remain unchanged.

Errors: `400 VALIDATION_ERROR`, `404 TASK_NOT_FOUND`, `415 UNSUPPORTED_MEDIA_TYPE`.

### 5.5 Update Task Status

```http
PATCH /api/tasks/{taskId}/status
Content-Type: application/json
```

```json
{
  "status": "COMPLETED"
}
```

Success: `200 OK` with updated response.

Errors: `400 INVALID_TASK_STATUS`, `400 VALIDATION_ERROR` for unreadable JSON, `404 TASK_NOT_FOUND`, `415 UNSUPPORTED_MEDIA_TYPE`.

### 5.6 Delete Task

```http
DELETE /api/tasks/{taskId}
```

Success:

```http
204 No Content
```

Do not attempt to parse a JSON response body after a successful delete.

Missing task: `404 TASK_NOT_FOUND`.

## 6. Error catalog

| Code | HTTP | Meaning | Suggested client action |
|---|---:|---|---|
| `VALIDATION_ERROR` | 400 | Request body/fields invalid or malformed | Correct request and do not retry unchanged |
| `INVALID_TASK_STATUS` | 400 | Unsupported/missing status | Send one documented status |
| `TASK_NOT_FOUND` | 404 | Task ID does not exist | Refresh state or stop operation |
| `METHOD_NOT_ALLOWED` | 405 | HTTP method unsupported | Correct method |
| `UNSUPPORTED_MEDIA_TYPE` | 415 | Request Content-Type is not JSON | Send `application/json` |
| `INTERNAL_ERROR` | 500 | Unexpected server error | Log correlation context and retry according to client policy |

## 7. End-to-end example

1. POST a task and capture `Location`/`id`.
2. GET the returned task ID.
3. PATCH status to `COMPLETED`.
4. DELETE the same task and accept an empty `204` response.

## 8. Validation summary

- `title`: required and non-blank for create/update
- `description`: optional; blank normalizes to null
- `status`: exact documented enum value
- malformed JSON: `400 VALIDATION_ERROR`
- unknown task: `404 TASK_NOT_FOUND`
- non-JSON create/update/status request: `415 UNSUPPORTED_MEDIA_TYPE`

## 9. Client integration checklist

- [ ] Send `Content-Type: application/json` for body requests.
- [ ] Send/accept exact JSON field names.
- [ ] Handle `201` and read `Location` after creation.
- [ ] Treat ID and timestamps as server-managed.
- [ ] Send only documented status values.
- [ ] Parse `ErrorResponse` for documented failures.
- [ ] Do not parse a body after `204`.

## 10. Known limitations

- in-memory storage only
- data loss on restart
- no authentication/authorization
- no pagination/sorting
- no multi-node consistency
- no status-transition restrictions
