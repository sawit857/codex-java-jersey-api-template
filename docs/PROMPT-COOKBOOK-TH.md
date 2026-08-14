# Prompt Cookbook สำหรับ Codex Java API Workflow

เอกสารนี้รวมคำสั่งพร้อมใช้ตาม case.
แก้ `<placeholder>` และ path ให้ตรงงานก่อนส่งให้ Codex.

## ตารางเลือก prompt

| Case | Prompt file | Skill หลัก |
|---|---|---|
| ทำ feature ครบ lifecycle | `prompts/00-workflow.md` | `$api-workflow` |
| วิเคราะห์อย่างเดียว | `prompts/01-analyze.md` | `$api-analyze` |
| implement approved plan | `prompts/02-implement.md` | `$api-implement` |
| เขียน/รัน test | `prompts/03-test.md` | `$api-test` |
| independent review | `prompts/04-review.md` | `$api-review` |
| client API document | `prompts/05-document.md` | `$api-document` |
| แก้ review findings | `prompts/06-fix-review.md` | ตาม owner |
| API/feature ใหม่ | `prompts/feature/new-api.prompt.md` | `$api-workflow` |
| เพิ่ม endpoint | `prompts/feature/add-endpoint.prompt.md` | `$api-workflow` |
| เพิ่ม field | `prompts/feature/add-field.prompt.md` | `$api-workflow` |
| สืบ root cause bug | `prompts/bugfix/investigate-bug.prompt.md` | `$api-analyze` |
| แก้ defect | `prompts/bugfix/fix-defect.prompt.md` | analyze→implement→test→review |
| safe refactor | `prompts/refactor/safe-refactor.prompt.md` | analyze→implement→test→review |
| legacy cleanup | `prompts/refactor/legacy-cleanup.prompt.md` | analyze→test→implement→review |
| code review | `prompts/review/code-review.prompt.md` | `$api-review` |
| security review | `prompts/review/security-review.prompt.md` | `$api-review` |
| performance review | `prompts/review/performance-review.prompt.md` | `$api-review` |
| API docs only | `prompts/documentation/api-doc.prompt.md` | `$api-document` |
| release notes | `prompts/documentation/release-note.prompt.md` | read-only evidence summary |
| pre-release check | `prompts/release/pre-release-check.prompt.md` | verify/review |
| release summary | `prompts/release/release-summary.prompt.md` | artifact summary |
| resume session | `prompts/maintenance/resume-workflow.prompt.md` | state-aware |
| final verify | `prompts/maintenance/final-verification.prompt.md` | final gate |

---

## Case 1 — Bootstrap repository

```text
Read AGENTS.md, README.md, docs/CODEX-USAGE-GUIDE-TH.md,
and prompts/README.md.

Summarize:
- project stack and prohibited technologies
- functional source of truth
- available skills
- available custom agents
- phase ownership
- workflow gates
- exact build/test commands

Do not modify files.
```

## Case 2 — Full feature workflow

```text
$api-workflow

Feature:
<describe requested API behavior>

Specification:
spec/spec-api.md

Use the complete lifecycle:
Analyze -> approved plan -> Implement -> Test -> Review -> Document -> Final Verify.

Do not skip gates or modify the public contract silently.
Only report COMPLETED after fresh mvn clean verify evidence with non-zero tests.
```

## Case 3 — Analyze only

```text
$api-analyze

Request:
<describe feature/defect/refactor>

Specification:
spec/spec-api.md

Compare the specification with the repository and tests.
Delegate read-only exploration to api_analyst.
Return exact impacted files, coverage matrix, test scenarios,
risks, assumptions, blocking questions, and handoff status.
Do not modify files.
```

## Case 4 — Save approved plan

```text
Save the user-approved plan from this conversation to:
docs/plans/YYYY-MM-DD-<feature>-plan.md

Preserve exact scope, contract decisions, file paths, test scenarios,
risks, and handoff status.
Do not implement code in this step.
```

## Case 5 — Implement approved plan

```text
$api-implement

Approved plan:
docs/plans/YYYY-MM-DD-<feature>-plan.md

Specification:
spec/spec-api.md

Delegate production implementation to api_developer.
Allowed: src/main and explicitly planned production pom.xml changes.
Forbidden: src/test, spec, docs/reviews, docs/api, workflow configuration.
Preserve user changes.
Run mvn -DskipTests compile and report actual evidence.
```

## Case 6 — Test implementation

```text
$api-test

Specification:
spec/spec-api.md

Approved plan:
docs/plans/YYYY-MM-DD-<feature>-plan.md

Delegate tests to api_tester.
Cover service, repository, and Jersey HTTP behavior.
Do not modify production code.
Run focused tests, then mvn test, and confirm Tests run > 0.
Keep valid failing tests and route production defects to api_developer.
```

## Case 7 — Independent review

```text
$api-review

Review scope:
- spec/spec-api.md
- docs/plans/YYYY-MM-DD-<feature>-plan.md
- src/main
- src/test
- pom.xml and web.xml
- actual command evidence

Delegate to api_reviewer in read-only mode.
Every finding must have ID, severity, exact path/symbol,
specification, evidence, impact, recommendation, and owner.
```

## Case 8 — Fix accepted production finding

```text
$api-implement

Fix accepted finding <REV-ID> from docs/reviews/latest-review.md.
Limit the change to the root cause.
Do not modify valid tests or the specification.
Run production compile and report evidence.
```

แล้วต่อด้วย:

```text
$api-test

Rerun tests affected by <REV-ID>, then run mvn test.
Preserve existing assertions.
```

และ:

```text
$api-review

Re-review <REV-ID> and affected regression paths.
Remain read-only and update the review handoff based on fresh evidence.
```

## Case 9 — Fix accepted test finding

```text
$api-test

Fix accepted test finding <REV-ID>.
Do not modify production source or specification.
Add meaningful assertions/coverage, run focused tests, then mvn test.
```

## Case 10 — Generate client API documentation

```text
$api-document

Specification:
spec/spec-api.md

Plan:
docs/plans/YYYY-MM-DD-<feature>-plan.md

Accepted review:
docs/reviews/latest-review.md

Output:
docs/api/<feature>-api.md

Document only verified endpoints, fields, statuses, headers, errors,
validation, examples, and limitations.
```

## Case 11 — Add a new endpoint

```text
Read prompts/feature/add-endpoint.prompt.md and execute it.

Endpoint:
<METHOD> <PATH>

Purpose:
<behavior>

Specification section:
<path/heading>
```

## Case 12 — Add a request/response field

```text
Read prompts/feature/add-field.prompt.md and execute it.

Model:
<CreateTaskRequest / TaskResponse / other>

Field:
<name, type, required/optional, validation>

Compatibility decision:
<backward-compatible or breaking>
```

## Case 13 — Investigate bug without changing code

```text
Read prompts/bugfix/investigate-bug.prompt.md and execute it.

Observed behavior:
<what happened>

Expected behavior:
<what should happen and source>

Evidence:
<logs, stack trace, request/response, failing test>
```

## Case 14 — Fix defect with regression test

```text
Read prompts/bugfix/fix-defect.prompt.md and execute it.

Defect:
<description>

Evidence:
<evidence>

Expected behavior:
<spec/accepted requirement>
```

## Case 15 — Safe refactor

```text
Read prompts/refactor/safe-refactor.prompt.md and execute it.

Target:
<class/package>

Problem:
<duplication, complexity, coupling>

External behavior must not change.
```

## Case 16 — Legacy cleanup

```text
Read prompts/refactor/legacy-cleanup.prompt.md and execute it.

Target:
<class/package/module>

Runtime constraints:
<JDK/App Server/Jersey version>

Known callers:
<paths or modules>
```

## Case 17 — Security review

```text
Read prompts/review/security-review.prompt.md and execute it.

Scope:
<resource/filter/JWT/configuration/package>

Threat focus:
<token validation, authorization, secret exposure, logging, error exposure>
```

## Case 18 — Performance review

```text
Read prompts/review/performance-review.prompt.md and execute it.

Scope:
<endpoint/service/repository>

Evidence:
<latency, throughput, heap/thread data, profile, logs>

Do not recommend optimization without evidence.
```

## Case 19 — Pre-release check

```text
Read prompts/release/pre-release-check.prompt.md and execute it.
```

## Case 20 — Resume after session/context limit

```text
Read prompts/maintenance/resume-workflow.prompt.md and execute it.
```

## Case 21 — Final verification only

```text
Read prompts/maintenance/final-verification.prompt.md and execute it.
```

---

## Prompt design rule

Prompt ของงานหนึ่งรอบควรมี:

```text
Goal
Input files
Scope
Constraints
Required workflow
Expected output
Success criteria
```

อย่า copy policy ทั้ง `AGENTS.md` ไปทุก prompt และอย่า copy procedure ทั้ง `SKILL.md` ไปทุก prompt.
Prompt ควรบอกเฉพาะ input และ scope ของรอบนั้น.
