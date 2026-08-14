---
name: api-workflow
description: Orchestrate the complete Java 8 Jersey API lifecycle from analysis through final verification. Use for full feature work requiring plan, implementation, tests, review, documentation, defect loops, and mvn clean verify evidence.
---

# API Workflow

This skill coordinates specialized phases. It does not directly implement, test, review, or document when a specialized skill/agent owns that work.

## State model

```text
ANALYSIS_REQUIRED
-> ANALYSIS_COMPLETED
-> IMPLEMENTATION_REQUIRED
-> IMPLEMENTATION_COMPLETED
-> TESTING_REQUIRED
-> TESTING_COMPLETED
-> REVIEW_REQUIRED
-> REVIEW_COMPLETED
-> DOCUMENTATION_REQUIRED
-> DOCUMENTATION_COMPLETED
-> FINAL_VERIFICATION_REQUIRED
-> COMPLETED
```

## Phase 1: Analyze

Invoke `$api-analyze` and delegate to `api_analyst`.

Pass condition:

```text
READY_FOR_IMPLEMENTATION
```

Parent validates and persists an approved plan under `docs/plans/`.

Stop on contract ambiguity.

## Phase 2: Implement

Invoke `$api-implement` and delegate production work to `api_developer`.

Required evidence:

```text
mvn -DskipTests compile
```

Pass condition:

```text
READY_FOR_TEST_IMPLEMENTATION
```

## Phase 3: Test

Invoke `$api-test` and delegate to `api_tester`.

Required evidence:

```text
mvn test
Tests run > 0
Failures = 0
Errors = 0
```

Pass condition:

```text
READY_FOR_REVIEW
```

A production defect returns to implementation; after the fix, testing runs again.

## Phase 4: Review

Invoke `$api-review` and delegate read-only review to `api_reviewer`.

Parent validates and persists:

```text
docs/reviews/latest-review.md
```

Pass condition:

```text
READY_FOR_DOCUMENTATION
```

Production findings return to implementation then testing and re-review. Test findings return to testing then re-review. Public-contract decisions stop for the parent/user.

## Phase 5: Document

Invoke `$api-document` and delegate to `api_doc_writer`.

Required artifact:

```text
docs/api/task-api.md
```

Pass condition:

```text
READY_FOR_FINAL_VERIFICATION
```

## Phase 6: Final verification

Inspect working tree and artifacts, then run:

```bash
mvn clean verify
```

Completion requires:

- Maven `BUILD SUCCESS`
- non-zero executed tests
- zero failures/errors
- current plan/review/API document
- no unresolved CRITICAL/HIGH
- no unauthorized/generated/secret/debug changes

Only then use:

```text
COMPLETED
```

## Defect loops

```text
review production defect
-> api-implement
-> api-test
-> api-review

review test defect
-> api-test
-> api-review
```

Do not skip re-testing or re-review after a correction.

## Retry policy

Default maximum per repeated implementation/test/review correction loop: 3 rounds. After that, return `REQUIRES_PARENT_DECISION` with attempted changes and evidence. This limit prevents endless automated churn; the user may explicitly authorize another round.

## Workflow report

```text
# API Workflow Result
## Current Phase
## Completed Phases
## Artifact State
## Delegated Agents
## Verification Evidence
## Open Issues
## Next Action
## Workflow Status
```

Statuses:

```text
IN_PROGRESS
BLOCKED
REQUIRES_PARENT_DECISION
COMPLETED
```

Use `references/workflow-gates.md`.
