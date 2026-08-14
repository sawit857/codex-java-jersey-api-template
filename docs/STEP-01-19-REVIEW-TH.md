# รายงานรีวิวความสอดคล้อง Step 1–19

เอกสารนี้เทียบ repository template กับแนวทางที่ออกแบบร่วมกันตั้งแต่ Step 1 ถึง Step 19
โดยตรวจทั้งโครงสร้างไฟล์ เนื้อหา agent/skill/prompt, ตัวอย่าง API, workflow artifact และวิธีใช้งาน

## สรุปผล

สถานะหลังปรับปรุงในรุ่น v3:

- **PASS:** 19/19 steps
- **Blocking gap:** ไม่มี
- **Known verification limitation:** ยังไม่มีผล `mvn clean verify` จาก environment ที่ใช้สร้าง ZIP เพราะไม่มี Maven และ shell ออก network ไม่ได้
- **Required local action:** รัน `./scripts/verify.sh` หรือ `scripts\\verify.cmd` บนเครื่องที่มี JDK 8 และ Maven

คำว่า PASS ในรายงานนี้หมายถึง **template content และ workflow coverage ตรงกับที่ออกแบบ**
ไม่ได้หมายความว่า Maven integration tests ผ่านแล้ว ดูหลักฐานที่ `VERIFICATION.txt`

---

## Step-by-step matrix

| Step | สิ่งที่ตกลง | หลักฐานใน repo | ผล |
|---:|---|---|---|
| 1 | Main agent คุม 5 specialized subagents | `.codex/agents/` มี analyst, developer, tester, reviewer, doc writer; `.codex/config.toml` จำกัด depth/thread | PASS |
| 2 | Task Management CRUD API บน JDK 8/Jersey 2/JUnit 5/Tomcat 9 | `spec/spec-api.md`, `pom.xml`, `src/main`, `src/test` | PASS |
| 3 | แยก AGENTS, agents, skills, prompts, spec, docs, scripts, source/test | repository structure ครบตาม map ใน `README.md` | PASS |
| 4 | Root `AGENTS.md` เป็น policy ไม่ใช่ task prompt | `AGENTS.md` ครอบคลุม source of truth, stack, architecture, testing, ownership, DoD | PASS |
| 5 | `.codex/config.toml` + read-only `api_analyst` | `.codex/config.toml`, `.codex/agents/api-analyst.toml`, `prompts/01-analyze.md` | PASS |
| 6 | `api_developer` เขียน production code ไม่แก้ test/spec | `.codex/agents/api-developer.toml`, `prompts/02-implement.md` | PASS |
| 7 | `api_tester` เขียน test จาก spec และคืน defect ให้ developer | `.codex/agents/api-tester.toml`, `prompts/03-test.md` | PASS |
| 8 | read-only `api_reviewer`, finding ต้องมี evidence/severity/owner | `.codex/agents/api-reviewer.toml`, `prompts/04-review.md` | PASS |
| 9 | `api_doc_writer` สร้าง client doc จาก verified contract | `.codex/agents/api-doc-writer.toml`, `prompts/05-document.md` | PASS |
| 10 | `$api-analyze` + analysis checklist | `.agents/skills/api-analyze/` | PASS |
| 11 | `$api-implement` + ownership/compile gate | `.agents/skills/api-implement/` | PASS |
| 12 | `$api-test` + focused/full tests, non-zero discovery, defect loop | `.agents/skills/api-test/` | PASS |
| 13 | `$api-review` + validated report gate | `.agents/skills/api-review/` | PASS |
| 14 | `$api-document` + contract/example checks | `.agents/skills/api-document/` | PASS |
| 15 | `$api-workflow` orchestrator + state/gates/retry loops/final verify | `.agents/skills/api-workflow/` และ references 3 ไฟล์ | PASS |
| 16 | Root repo policy และ skill/agent usage rules | `AGENTS.md` | PASS |
| 17 | Prompt library แยกตาม case | `prompts/` ครบ phase + feature + bugfix + refactor + review + docs + release + maintenance | PASS |
| 18 | Repo ตัวอย่าง clone/open แล้วศึกษาได้ มี code/test/spec/docs | Maven WAR project และ artifacts ตัวอย่างครบ | PASS |
| 19 | คู่มือใช้งานจริงบน Codex.app, phase commands, resume session | `README.md`, `docs/CODEX-USAGE-GUIDE-TH.md`, `docs/PROMPT-COOKBOOK-TH.md` | PASS |

---

## รีวิวรายละเอียด

### 1. Codex discovery conventions

โครงสร้างปัจจุบันใช้ตำแหน่งที่ Codex รองรับ:

```text
AGENTS.md
.codex/config.toml
.codex/agents/*.toml
.agents/skills/<skill>/SKILL.md
```

สิ่งที่ตั้งใจไม่ทำคือใส่ reusable skills ไว้ `.codex/skills/` หรือทำ agent เป็น Markdown
เพราะ custom agents ของ project ใช้ TOML และ repo skills ใช้ `.agents/skills`.

### 2. Separation of concerns

การแบ่งหน้าที่ปัจจุบันชัดเจน:

```text
AGENTS.md       = repository policy
SKILL.md        = reusable procedure
agent TOML      = specialized worker behavior/permission
prompt file     = current work order/input
spec/           = external contract
artifacts       = evidence passed between phases
```

Agent ownership:

| Agent | Default mode | Primary ownership |
|---|---|---|
| `api_analyst` | read-only | ส่ง analysis/plan content ให้ parent |
| `api_developer` | workspace-write | `src/main/`, planned production `pom.xml` |
| `api_tester` | workspace-write | `src/test/`, planned test config |
| `api_reviewer` | read-only | ส่ง review content ให้ parent |
| `api_doc_writer` | workspace-write | `docs/api/` |

### 3. Workflow gates

Full lifecycle ถูกล็อกเป็น:

```text
Analyze
  -> Approved plan
Implement
  -> Compile evidence
Test
  -> Non-zero tests, zero failure/error
Review
  -> No unresolved CRITICAL/HIGH
Document
  -> Verified client contract
Final Verify
  -> mvn clean verify
```

Defect routing:

```text
Production defect -> api_developer -> api_tester -> api_reviewer
Test defect       -> api_tester -> api_reviewer
Contract ambiguity -> parent/user decision
```

ไม่มี phase ใดมีสิทธิ์ประกาศ `COMPLETED` แทน final workflow gate.

### 4. Prompt library

รุ่น v2 มี prompt หลักแล้ว แต่ยังไม่ครอบคลุมทุก case ที่อธิบายใน Step 17.
รุ่น v3 เติม:

```text
feature/add-endpoint.prompt.md
feature/add-field.prompt.md
bugfix/investigate-bug.prompt.md
refactor/legacy-cleanup.prompt.md
review/code-review.prompt.md
review/security-review.prompt.md
review/performance-review.prompt.md
documentation/api-doc.prompt.md
documentation/release-note.prompt.md
release/pre-release-check.prompt.md
release/release-summary.prompt.md
maintenance/resume-workflow.prompt.md
maintenance/final-verification.prompt.md
```

Prompt เหล่านี้เป็นไฟล์ใน repo เพื่อให้ใช้คำสั่งแบบ portable:

```text
Read prompts/<path>.md and execute it.
```

หรือเรียก skill โดยตรง:

```text
$api-analyze
$api-implement
$api-test
$api-review
$api-document
$api-workflow
```

### 5. Sample Java project

ตัวอย่างมี:

- six Task endpoints
- request/response DTO separation
- in-memory repository
- service validation and injected `Clock`
- specific and generic exception mappers
- Jersey application configuration and `web.xml`
- service, repository, and Jersey integration tests
- API plan, review report และ client API document ตัวอย่าง

ตัวอย่าง intentionally ไม่มี database, auth, Docker, frontend, JPA หรือ Spring ตาม scope ที่ตกลง.

---

## Deviations ที่ยอมรับได้

### ชื่อ repository

ในบทสนทนามีชื่อทดลองหลายชื่อ เช่น `codex-java-api-workflow`, `java-api-agent-demo` และ `java-api-agent-template`.
ชื่อ package ที่ส่งคือ:

```text
codex-java8-jersey-api-template-v3
```

ชื่อไม่กระทบ workflow และสื่อ stack ชัดกว่า จึงถือว่า PASS.

### ชื่อ reference file ของ workflow

Step 15 เคยเสนอทั้ง `workflow-state.md`, `workflow-gate.md` และมีบางช่วงใช้ `workflow-gates.md`.
รุ่น v3 เก็บครบ:

```text
workflow-state.md
workflow-gate.md
workflow-gates.md
```

`workflow-gates.md` เป็น detailed checklist; อีกสองไฟล์เป็น concise state/gate reference.

### Reviewer/analyst artifact writing

Design เลือกให้ analyst/reviewer read-only และส่ง content ให้ parent persist.
ดังนั้น ownership ของ `docs/plans/` และ `docs/reviews/` เป็น parent orchestration ใน execution จริง
แม้ table ระดับสูงจะเรียก artifact ว่า analyst/reviewer output.
นี่เป็น intentional safety boundary ไม่ใช่ gap.

---

## Verification status

ตรวจแล้วใน generation environment:

- TOML/XML/JSON parsing
- skill metadata และ directory names
- agent/skill references
- Java package/path consistency
- endpoint/status/error-code consistency
- Java 8 syntax/type compilation ด้วย generated external API stubs
- core service/repository smoke behavior
- ZIP integrity

ยังไม่ได้รันจริง:

```bash
mvn clean verify
```

เหตุผลและหลักฐานอยู่ใน `VERIFICATION.txt`.

ก่อนใช้เป็น baseline ให้รันบนเครื่องคุณ:

```bash
./scripts/verify.sh
```

Windows:

```bat
scripts\\verify.cmd
```

---

## Final review conclusion

Template v3 ตรงกับแนวทาง Step 1–19 ในระดับ:

1. architecture ของ Codex workflow
2. agent ownership และ permissions
3. reusable skills และ gates
4. prompt commands สำหรับแต่ละ case
5. example Java API repository
6. onboarding และ daily usage guide
7. session resume และ evidence-based completion

ข้อจำกัดที่ยังเปิดเผยตรง ๆ คือไม่มี Maven/Jersey/JUnit runtime verification จากเครื่องสร้าง ZIP.
ห้ามใช้ `VERIFICATION.txt` แทน local `mvn clean verify`.
