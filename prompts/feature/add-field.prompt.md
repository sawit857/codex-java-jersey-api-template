Use `$api-workflow`.

Change model:

```text
<request/response model>
```

Field:

```text
<name, JSON type, required/optional, validation, server-managed flag>
```

Specification:

```text
spec/spec-api.md
```

Analyze backward compatibility and every impacted DTO, mapper/service/resource,
JSON example, test, and client document. Do not infer field behavior from the
internal model. Stop when compatibility or null/default semantics are undefined.
