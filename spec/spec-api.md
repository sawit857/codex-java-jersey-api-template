# Task Management REST API Specification

## 1. Purpose

Provide a small JSON REST API for creating and managing tasks. The application stores data in process memory only.

## 2. Base contract

```text
Deployment context example: /task-api
JAX-RS base mapping:       /api
Resource base path:        /tasks
Full example base URL:     http://localhost:8080/task-api/api/tasks
```

Media type:

```text
application/json
```

Date-time representation:

```text
ISO-8601 local date-time without an offset, for example 2026-07-19T22:30:00
```

Task identifiers are positive server-generated integers.

## 3. Models

### 3.1 TaskResponse

| Field | JSON type | Required | Server managed | Description |
|---|---|---:|---:|---|
| `id` | number | Yes | Yes | Task identifier |
| `title` | string | Yes | No | Trimmed non-blank title |
| `description` | string or null | Yes | No | Optional trimmed description |
| `status` | string | Yes | Yes on create | Task status enum |
| `createdAt` | string | Yes | Yes | Creation date-time |
| `updatedAt` | string | Yes | Yes | Last update date-time |

### 3.2 CreateTaskRequest

| Field | JSON type | Required | Description |
|---|---|---:|---|
| `title` | string | Yes | Non-blank after trimming |
| `description` | string or null | No | Trimmed when present; blank becomes null |

The client must not send `id`, `status`, `createdAt`, or `updatedAt` as contract fields.

### 3.3 UpdateTaskRequest

| Field | JSON type | Required | Description |
|---|---|---:|---|
| `title` | string | Yes | Non-blank after trimming |
| `description` | string or null | No | Trimmed when present; blank becomes null |

The operation replaces editable task fields. It does not change `status`.

### 3.4 UpdateTaskStatusRequest

| Field | JSON type | Required | Description |
|---|---|---:|---|
| `status` | string | Yes | One documented status value |

### 3.5 ErrorResponse

| Field | JSON type | Required | Description |
|---|---|---:|---|
| `code` | string | Yes | Stable client-facing error code |
| `message` | string | Yes | Human-readable safe message |
| `timestamp` | string | Yes | ISO-8601 local date-time |
| `path` | string | Yes | Request URI path |

Error responses must not expose stack traces, exception class names, package names, filesystem paths, or raw internal exception messages.

## 4. Task status values

```text
OPEN
IN_PROGRESS
COMPLETED
CANCELLED
```

This specification does not restrict transitions between documented values.

## 5. Endpoints

### 5.1 Create Task

```http
POST /api/tasks
Content-Type: application/json
Accept: application/json
```

Request:

```json
{
  "title": "Prepare API specification",
  "description": "Write the client integration document"
}
```

Success:

```http
201 Created
Location: {baseUrl}/api/tasks/{taskId}
Content-Type: application/json
```

Response body: `TaskResponse` with:

- generated `id`
- normalized title/description
- `status` = `OPEN`
- `createdAt` = current application clock
- `updatedAt` = same value as `createdAt`

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 400 | `VALIDATION_ERROR` | missing body, missing title, or blank title |
| 400 | `VALIDATION_ERROR` | malformed JSON |
| 415 | `UNSUPPORTED_MEDIA_TYPE` | request media type is not JSON |
| 500 | `INTERNAL_ERROR` | unexpected failure |

### 5.2 List Tasks

```http
GET /api/tasks
GET /api/tasks?status=OPEN
Accept: application/json
```

Success:

```http
200 OK
Content-Type: application/json
```

Body: JSON array of `TaskResponse`. An empty repository returns `[]`.

When `status` is supplied, return only matching tasks.

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 400 | `INVALID_TASK_STATUS` | unsupported status query value |
| 500 | `INTERNAL_ERROR` | unexpected failure |

### 5.3 Get Task

```http
GET /api/tasks/{taskId}
Accept: application/json
```

Success: `200 OK` with `TaskResponse`.

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 404 | `TASK_NOT_FOUND` | identifier does not exist |
| 500 | `INTERNAL_ERROR` | unexpected failure |

### 5.4 Update Task

```http
PUT /api/tasks/{taskId}
Content-Type: application/json
Accept: application/json
```

Request:

```json
{
  "title": "Prepare final API specification",
  "description": "Include error response examples"
}
```

Success: `200 OK` with updated `TaskResponse`.

Rules:

- update `title` and `description`
- keep `id`, `status`, and `createdAt`
- set `updatedAt` from the application clock
- validation failure makes no partial update

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 400 | `VALIDATION_ERROR` | missing body or blank/missing title |
| 400 | `VALIDATION_ERROR` | malformed JSON |
| 404 | `TASK_NOT_FOUND` | identifier does not exist |
| 415 | `UNSUPPORTED_MEDIA_TYPE` | request media type is not JSON |
| 500 | `INTERNAL_ERROR` | unexpected failure |

### 5.5 Update Task Status

```http
PATCH /api/tasks/{taskId}/status
Content-Type: application/json
Accept: application/json
```

Request:

```json
{
  "status": "COMPLETED"
}
```

Success: `200 OK` with updated `TaskResponse`.

Rules:

- keep editable text, ID, and `createdAt`
- update status
- set `updatedAt` from the application clock

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 400 | `INVALID_TASK_STATUS` | missing, blank, or unsupported status |
| 400 | `VALIDATION_ERROR` | malformed JSON |
| 404 | `TASK_NOT_FOUND` | identifier does not exist |
| 415 | `UNSUPPORTED_MEDIA_TYPE` | request media type is not JSON |
| 500 | `INTERNAL_ERROR` | unexpected failure |

### 5.6 Delete Task

```http
DELETE /api/tasks/{taskId}
```

Success:

```http
204 No Content
```

The success response has no entity body.

Errors:

| HTTP | Code | Condition |
|---:|---|---|
| 404 | `TASK_NOT_FOUND` | identifier does not exist |
| 500 | `INTERNAL_ERROR` | unexpected failure |

## 6. Common framework errors

| HTTP | Error code | Condition |
|---:|---|---|
| 400 | `VALIDATION_ERROR` | malformed or otherwise unreadable JSON request |
| 405 | `METHOD_NOT_ALLOWED` | unsupported HTTP method for a known resource path |
| 415 | `UNSUPPORTED_MEDIA_TYPE` | unsupported request Content-Type |
| 500 | `INTERNAL_ERROR` | unhandled unexpected exception |

## 7. Out of scope

- authentication and authorization
- database or durable persistence
- pagination and sorting
- search beyond status filtering
- status-transition restrictions
- optimistic locking
- distributed/multi-node state
- frontend application
- Docker/deployment automation
- OpenAPI generation
