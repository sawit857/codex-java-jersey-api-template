---
name: api-analyze
description: Analyze a Java 8 Jersey REST API specification or requested change before implementation. Use for spec-to-code comparison, impacted-file planning, test scenarios, risk analysis, and blocking contract questions. Remain read-only and delegate exploration to api_analyst.
---

# API Analyze

Analyze before modifying source.

## Inputs

- exact user request
- specification path, normally `spec/spec-api.md`
- existing plan path when applicable
- requested scope

## Procedure

1. Read `AGENTS.md` and applicable nested instructions.
2. Read the complete relevant specification.
3. Inspect `pom.xml`, source tree, deployment configuration, and tests.
4. Delegate bounded read-only exploration to `api_analyst`.
5. Validate the analyst's paths, symbols, assertions, and conclusions.
6. Build a specification coverage matrix.
7. Identify exact files to create, modify, and read only.
8. Define tests before implementation.
9. Classify Java 8, Jersey, Maven, WAR, Tomcat, repository, validation, and documentation risks.
10. Separate conservative internal assumptions from blocking public-contract ambiguity.
11. Return a plan and handoff status. Do not edit files.

## Coverage states

```text
IMPLEMENTED
PARTIAL
MISSING
CONFLICT
NOT_APPLICABLE
```

## Required test levels

```text
SERVICE_UNIT
REPOSITORY
JERSEY_INTEGRATION
BUILD_VERIFICATION
```

Every proposed production behavior must have a corresponding test scenario.

## Output

```text
# API Analysis Result
## Objective
## Scope
## Current Repository State
## Specification Coverage
## Impacted Files
## Implementation Sequence
## Test Scenarios
## Risks
## Assumptions
## Blocking Questions
## Recommended Verification
## Handoff Status
```

Handoff:

```text
READY_FOR_IMPLEMENTATION
BLOCKED_BY_SPEC_AMBIGUITY
BLOCKED_BY_REPOSITORY_STATE
BLOCKED_BY_ENVIRONMENT
```

The delegated analyst returns plan content. The parent validates it and saves an approved plan under `docs/plans/`.

Use `references/analysis-checklist.md` during the inspection.
