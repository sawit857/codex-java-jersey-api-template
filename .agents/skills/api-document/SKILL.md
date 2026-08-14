---
name: api-document
description: Generate verified client-facing API documentation after implementation, tests, and review acceptance. Delegate to api_doc_writer, cross-check all contract details, and block invented or conflicting behavior.
---

# API Document

## Preconditions

- specification, approved plan, implementation, relevant tests, and accepted review exist
- review handoff is `READY_FOR_DOCUMENTATION`
- no unresolved CRITICAL/HIGH finding
- no contract conflict

## Ownership

Allowed:

```text
docs/api/
```

Source, tests, specification, plan, review, `pom.xml`, and workflow configuration are read-only.

## Procedure

1. Read all contract and verification sources.
2. Build an endpoint/model/error contract matrix.
3. Block on any disagreement rather than choosing silently.
4. Delegate output to `api_doc_writer`.
5. Inspect the document and diff.
6. Verify every endpoint, field, enum, status, header, error, example, and limitation.
7. Ensure examples are valid JSON and do not invent authentication/pagination/persistence/retry/lifecycle behavior.
8. Return `READY_FOR_FINAL_VERIFICATION` only when complete.

Required sections:

1. Overview
2. Common Conventions
3. Data Models
4. Task Status Values
5. Endpoints
6. Error Catalog
7. End-to-End Examples
8. Validation Summary
9. Client Integration Checklist
10. Known Limitations

Use `references/documentation-checklist.md`.
