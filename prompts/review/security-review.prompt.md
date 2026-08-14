Use `$api-review` with an explicit security focus.

Authorized scope:

```text
<packages/files/endpoints/configuration>
```

Review only evidence-supported risks relevant to this application, including:

- authentication/authorization boundaries when present
- JWT/token/certificate validation when present
- secret and sensitive-data exposure
- unsafe logging and exception responses
- input validation and unsafe deserialization
- dependency/configuration risks visible in the repository

Remain read-only. Do not invent requirements outside the authorized scope.
Every finding needs exploit/impact reasoning, exact evidence, severity, and owner.
