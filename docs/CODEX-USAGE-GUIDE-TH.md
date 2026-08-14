# คู่มือใช้งาน Codex.app กับ Java API Agent Template

คู่มือนี้อธิบายการใช้ repository ตั้งแต่เปิด project, ตรวจ discovery, เลือก workflow,
ใช้ subagent/skill, ส่ง defect กลับ owner และ resume เมื่อเปลี่ยน session.

> ตัวอย่างคำสั่งใช้ภาษาอังกฤษเพื่อให้ copy ไปใช้ใน Codex ได้ตรง ๆ ส่วนคำอธิบายเป็นภาษาไทย

---

## 1. ทำความเข้าใจ 4 ชั้นก่อนเริ่ม

```text
AGENTS.md          = กฎกลางของ repository
.agents/skills/    = ขั้นตอนมาตรฐานที่เรียกซ้ำ
.codex/agents/     = specialized subagents
prompts/           = ใบสั่งงานของแต่ละ case
```

Source of truth ของ API:

```text
spec/spec-api.md
```

Workflow artifacts:

```text
docs/plans/
docs/reviews/
docs/api/
docs/workflow-state.md
```

---

## 2. เตรียมเครื่อง

ต้องมี:

```text
JDK 8
Maven 3.8+ หรือ 3.9+
Codex.app / Codex CLI / Codex IDE extension
```

ตรวจ Java และ Maven:

```bash
java -version
mvn -version
```

Project นี้บังคับ compile target Java 8 แม้คุณเปิด Codex บนเครื่องที่มี JDK ใหม่กว่า
แต่เพื่อพิสูจน์ runtime compatibility จริง แนะนำให้ verification ด้วย JDK 8.

---

## 3. เปิดใน Codex.app

1. แตก ZIP
2. เปิด Codex.app
3. เลือก **Open Project**
4. เลือก folder `codex-java8-jersey-api-template-v3`
5. Trust project เมื่อ Codex ถาม

Project config ใต้ `.codex/` จะไม่โหลดถ้า project ไม่ trusted.

### Bootstrap prompt

ส่งคำสั่งนี้ก่อน:

```text
Read AGENTS.md, README.md, and docs/CODEX-USAGE-GUIDE-TH.md.
Summarize:
- repository constraints
- source of truth
- available skills
- available custom agents
- phase ownership
- completion gates
Do not modify files.
```

### ตรวจว่า skill ถูกค้นพบ

```text
List repository skills available in this project.
Do not modify files.
```

ควรเห็น:

```text
api-workflow
api-analyze
api-implement
api-test
api-review
api-document
```

หรือพิมพ์ `$` ใน composer แล้วดู skill selector.

### ตรวจ custom agents

```text
List project custom agents and summarize each agent's role and sandbox mode.
Do not modify files.
```

ควรเห็น:

```text
api_analyst
api_developer
api_tester
api_reviewer
api_doc_writer
```

หากไม่เห็น:

1. ตรวจว่าเปิด repository root ถูก folder
2. ตรวจว่า project trusted
3. restart Codex session
4. ตรวจ `.codex/config.toml` และ `.codex/agents/*.toml`

---

## 4. เลือกวิธีใช้งาน

### แบบ A — Full workflow

ใช้เมื่อ spec ชัดและต้องการครบตั้งแต่ analysis ถึง final verification:

```text
Read prompts/00-workflow.md and execute it.
```

หรือ:

```text
$api-workflow

Use spec/spec-api.md as the functional source of truth.
Run all required phases and do not skip gates.
```

ข้อดี: workflow ครบ
ข้อควรระวัง: ใช้ context/token มากกว่า และควรตรวจ phase artifact ระหว่างทาง

### แบบ B — Phase-by-phase

เหมาะสำหรับงานจริงและควบคุมง่ายกว่า:

```text
01 Analyze
02 Approve plan
03 Implement
04 Test
05 Review
06 Fix findings if any
07 Document
08 Final verify
```

แนะนำวิธีนี้สำหรับครั้งแรก.

---

## 5. Phase 1: Analyze

เรียกจากไฟล์:

```text
Read prompts/01-analyze.md and execute it.
```

หรือเรียก skill:

```text
$api-analyze

Analyze spec/spec-api.md against the current repository.
Delegate repository exploration to api_analyst.
Remain read-only.
Return exact files, test scenarios, risks, blocking questions,
and the READY_FOR_IMPLEMENTATION handoff when appropriate.
```

สิ่งที่ต้องตรวจจากผลลัพธ์:

- มี exact file paths
- มี evidence จาก class/method/test assertions
- มี requirement coverage matrix
- มี test scenarios ก่อน implementation
- ไม่มี source file ถูกแก้
- contract ambiguity ถูก block

### Save plan

เมื่อคุณตรวจและยอมรับ plan แล้ว สั่ง parent agent:

```text
Save the approved analysis plan to:
docs/plans/YYYY-MM-DD-<feature>-plan.md

Do not change the plan content except for formatting and resolved decisions.
```

Plan ไม่ถือว่า approved เพียงเพราะไฟล์ถูกสร้าง ต้องมี user/parent acceptance.

---

## 6. Phase 2: Implement

ใช้เมื่อมี approved plan แล้ว:

```text
Read prompts/02-implement.md and execute it.
```

หรือ:

```text
$api-implement

Approved plan:
docs/plans/2026-07-19-task-api-plan.md

Specification:
spec/spec-api.md

Delegate production changes to api_developer.
Do not modify src/test or spec.
Run mvn -DskipTests compile and report actual evidence.
```

ตรวจผล:

- เปลี่ยนเฉพาะ `src/main/` และ planned `pom.xml`
- ไม่มี `src/test/` หรือ `spec/` diff
- Java 8 / `javax.*`
- compile command มีผลจริง
- handoff คือ `READY_FOR_TEST_IMPLEMENTATION`

ถ้า plan ไม่มีหรือ conflict กับ spec ให้หยุด ไม่ต้องบอก developer ให้เดา.

---

## 7. Phase 3: Test

```text
Read prompts/03-test.md and execute it.
```

หรือ:

```text
$api-test

Spec:
spec/spec-api.md

Plan:
docs/plans/2026-07-19-task-api-plan.md

Delegate test work to api_tester.
Do not modify src/main.
Run focused tests and mvn test.
Confirm Tests run > 0.
```

Tester ต้องตรวจ 3 ระดับ:

```text
TaskServiceTest
InMemoryTaskRepositoryTest
TaskResourceTest
```

สิ่งที่ต้องดู:

```text
Tests run > 0
Failures = 0
Errors = 0
```

`BUILD SUCCESS` แต่ `Tests run: 0` ถือว่ายังไม่ผ่าน testing gate.

### เมื่อ test พบ production defect

ผลที่ถูกต้อง:

```text
PRODUCTION_DEFECT_FOUND
RETURN_TO_API_DEVELOPER
```

ห้าม tester:

- แก้ production code เอง
- ลด assertion
- disable test
- เปลี่ยน expected behavior ให้ตรง code ที่ผิด

---

## 8. Phase 4: Review

```text
Read prompts/04-review.md and execute it.
```

หรือ:

```text
$api-review

Review spec/spec-api.md, the approved plan, src/main, src/test,
pom.xml, web.xml, and actual command evidence.
Delegate to api_reviewer in read-only mode.
```

Finding ที่รับได้ต้องมี:

```text
ID
Severity
Exact file/location
Specification requirement
Evidence
Impact
Recommendation
Owner
```

Reviewer ต้องไม่แก้ source/test.

Parent agent ตรวจ finding แล้วบันทึก:

```text
docs/reviews/latest-review.md
```

ผ่าน gate เมื่อ:

```text
No unresolved CRITICAL
No unresolved HIGH
Handoff = READY_FOR_DOCUMENTATION
```

---

## 9. แก้ review findings

```text
Read prompts/06-fix-review.md and execute it.
```

หรือเลือกตาม owner:

### Production finding

```text
$api-implement

Fix accepted finding REV-001 from docs/reviews/latest-review.md.
Do not modify valid tests.
Keep the change limited to the finding's root cause.
```

จากนั้นต้อง test ใหม่:

```text
$api-test

Rerun tests affected by REV-001, then run mvn test.
Preserve existing valid assertions.
```

แล้ว review ใหม่:

```text
$api-review

Re-review REV-001 and affected regression paths.
Remain read-only.
```

### Test finding

```text
$api-test

Fix accepted test finding REV-004.
Do not modify production source.
Run focused tests and the complete suite.
```

### Contract ambiguity

หยุดให้ user/owner ตัดสินใจ ห้ามแก้ spec หรือ code แบบเลือกเอง.

---

## 10. Phase 5: Document

```text
Read prompts/05-document.md and execute it.
```

หรือ:

```text
$api-document

Specification:
spec/spec-api.md

Approved plan:
docs/plans/2026-07-19-task-api-plan.md

Accepted review:
docs/reviews/latest-review.md

Output:
docs/api/task-api.md
```

Doc writer เปลี่ยนได้เฉพาะ `docs/api/`.

ตรวจว่า document มี:

- all endpoints
- request/response models
- HTTP status and headers
- errors and validation
- valid JSON examples
- client checklist
- verified limitations

ห้ามแต่ง authentication, pagination, sorting, DB durability หรือ field ที่ไม่มีใน spec.

---

## 11. Final verification

เรียก prompt:

```text
Read prompts/maintenance/final-verification.prompt.md and execute it.
```

หรือรันเอง:

```bash
./scripts/verify.sh
```

Windows:

```bat
scripts\\verify.cmd
```

Manual command:

```bash
mvn clean verify
```

ก่อนใช้คำว่า `COMPLETED` ต้องมี:

- `BUILD SUCCESS`
- tests run > 0
- failures = 0
- errors = 0
- plan/review/API doc current
- no unresolved CRITICAL/HIGH
- no unauthorized/generated/secret/debug changes

---

## 12. Resume หลัง context เต็มหรือเปลี่ยน session

อย่าพิมพ์แค่ `ทำต่อ`.

ใช้ workflow state:

```text
docs/workflow-state.md
```

สร้างจาก example:

```bash
cp docs/workflow-state.example.md docs/workflow-state.md
```

อัปเดต:

```text
Current phase
Completed phases
Approved plan
Latest review status
Failing tests/findings
Next action
Commands and evidence
```

Session ใหม่ใช้:

```text
Read prompts/maintenance/resume-workflow.prompt.md and execute it.
```

หรือ:

```text
Read AGENTS.md and docs/workflow-state.md.
Inspect the current working tree and artifacts.
Continue only from the recorded next phase.
Do not repeat completed phases unless evidence is stale or inconsistent.
```

---

## 13. แนวทางแบ่ง session

สำหรับงานไม่ใหญ่มาก:

```text
Session 1: Analyze + approve plan
Session 2: Implement + compile
Session 3: Test + defect loop
Session 4: Review + fix routing
Session 5: Document + final verify
```

ไม่จำเป็นต้องบังคับแบ่งทุกครั้ง แต่ช่วยลด context บวมและลด agent เอา evidence เก่ามาปะปน.

---

## 14. วิธีใช้ prompt files

Prompt files ใน repo เป็น portable work orders:

```text
Read prompts/<path>.md and execute it.
```

ตัวอย่าง:

```text
Read prompts/feature/add-endpoint.prompt.md and execute it.
```

Skill เป็น reusable procedure:

```text
$api-analyze
```

Custom agent เป็น specialized worker ที่ parent skill delegate ให้:

```text
Use api_analyst for read-only repository exploration.
```

ใน Codex environment ที่เปิด custom prompts เป็น slash commands อาจเห็น `/prompts:<name>`;
แต่ repository นี้ไม่พึ่ง feature นั้น วิธี `Read prompts/...` ใช้ได้ชัดและ portable กว่า.

---

## 15. คำสั่ง bootstrap ที่แนะนำให้เก็บไว้

```text
Read AGENTS.md, README.md, docs/CODEX-USAGE-GUIDE-TH.md,
and prompts/README.md.

Summarize repository policy, source of truth, available skills,
custom agents, ownership boundaries, workflow gates, and verification commands.

Inspect only. Do not modify files.
```

---

## 16. คำสั่งตรวจว่า agent ไม่ทำเกิน ownership

หลัง implementation:

```text
Inspect the current diff.
Confirm api_developer changed only production-owned files.
List any unauthorized or unrelated modification.
Do not revert user changes automatically.
```

หลัง testing:

```text
Inspect the current diff.
Confirm api_tester did not modify src/main or spec.
Check for weakened assertions, @Disabled, Thread.sleep, and zero-test discovery.
```

หลัง documentation:

```text
Inspect the current diff.
Confirm api_doc_writer changed only docs/api.
Cross-check endpoints, fields, statuses, errors, and examples against spec and tests.
```

---

## 17. สิ่งที่ไม่ควรสั่ง

หลีกเลี่ยง:

```text
Create complete application and make all tests pass.
```

เพราะเปิดทางให้ agent ข้าม analysis/review และลด test เพื่อให้เขียว.

ใช้:

```text
Follow the repository workflow and ownership gates.
Implement the approved contract, preserve valid tests,
and report actual verification evidence.
```
