# Codex Java 8 Jersey API Workflow Template

A complete example repository for learning how to use Codex with:

- repository instructions (`AGENTS.md`)
- project-scoped custom subagents (`.codex/agents/*.toml`)
- repository skills (`.agents/skills/*/SKILL.md`)
- reusable prompt commands (`prompts/`)
- specification-driven Java REST API development
- review and documentation gates

The example application is a **Task Management REST API** implemented with:

- Java 8
- Maven
- Jersey 2.x (`javax.ws.rs`)
- Jackson
- JUnit 5
- AssertJ
- Jersey Test Framework
- WAR deployment to Tomcat 9

The API is intentionally small. The main purpose is to demonstrate the Codex workflow, not to build a production task platform.


## เอกสารเริ่มต้นภาษาไทย

- [`docs/CODEX-USAGE-GUIDE-TH.md`](docs/CODEX-USAGE-GUIDE-TH.md) — วิธีใช้ Codex.app แบบ phase-by-phase
- [`docs/PROMPT-COOKBOOK-TH.md`](docs/PROMPT-COOKBOOK-TH.md) — prompt พร้อมใช้ตาม case
- [`docs/STEP-01-19-REVIEW-TH.md`](docs/STEP-01-19-REVIEW-TH.md) — ผลเทียบ template กับ Step 1–19

Recommended order:

```text
README.md
-> AGENTS.md
-> docs/CODEX-USAGE-GUIDE-TH.md
-> docs/PROMPT-COOKBOOK-TH.md
```

---

## 1. Repository map

```text
.
├── AGENTS.md                    Repository-wide rules loaded by Codex
├── .codex/
│   ├── config.toml              Project Codex configuration
│   └── agents/                  Custom specialized subagents
├── .agents/skills/              Reusable repository skills
├── prompts/                     Copy/paste commands for common tasks
├── spec/                        Functional source of truth
├── docs/
│   ├── plans/                   Analysis and approved plans
│   ├── reviews/                 Accepted review reports
│   ├── api/                     Client-facing API documentation
│   └── workflow-state.example.md
├── scripts/                     Verification helpers
├── src/main/                    Example production implementation
└── src/test/                    Example automated tests
```

### What each layer means

| Layer | Purpose |
|---|---|
| `AGENTS.md` | Rules and constraints that apply to repository work |
| Custom subagent | A specialized worker with a bounded role and permissions |
| Skill | A repeatable procedure for a type of work |
| Prompt | Input and scope for the current task |
| Spec | External API contract and source of truth |
| Plan/review/doc artifacts | Evidence passed between workflow phases |

A useful mental model:

```text
AGENTS.md = project constitution
Skill     = standard operating procedure
Subagent  = specialized worker
Prompt    = work order for this run
```

---

## 2. Codex discovery locations

This template follows the current Codex project conventions:

- Repository skills: `.agents/skills/<skill-name>/SKILL.md`
- Project custom agents: `.codex/agents/<agent>.toml`
- Project configuration: `.codex/config.toml`
- Repository instructions: `AGENTS.md`

Project `.codex/` configuration is loaded only when the project is trusted.

Official references:

- https://learn.chatgpt.com/docs/build-skills
- https://learn.chatgpt.com/docs/agent-configuration/subagents
- https://learn.chatgpt.com/docs/agent-configuration/agents-md
- https://learn.chatgpt.com/docs/config-file/config-basic

---

## 3. Open the template in Codex.app

1. Extract this repository to a local directory.
2. Open Codex.app.
3. Choose **Open Project** and select this repository folder.
4. Trust the project when Codex asks, otherwise project `.codex/` settings and custom agents may be skipped.
5. Start with a read-only bootstrap prompt:

```text
Read AGENTS.md and README.md.
Summarize the repository rules, available skills, custom agents,
and the required API workflow. Do not modify files.
```

Then verify discovery:

```text
List the repository skills available for this project.
List the custom agents available for this project.
Do not modify files.
```

Expected skills:

```text
api-workflow
api-analyze
api-implement
api-test
api-review
api-document
```

Expected custom agents:

```text
api_analyst
api_developer
api_tester
api_reviewer
api_doc_writer
```

Codex CLI/IDE can explicitly mention a skill with `$skill-name`. Custom agents are delegated by name in the task instructions. The `/agent` command can be used in supported local clients to inspect subagent activity.

---

## 4. Recommended first run

The repository already contains a complete sample implementation. Therefore, start by practicing the **analysis and review** phases rather than asking Codex to rewrite everything.

### Read-only analysis

```text
Read prompts/01-analyze.md and execute it.
```

Or directly:

```text
$api-analyze

Compare spec/spec-api.md with the current implementation and tests.
Remain read-only. Return the coverage matrix, exact evidence, risks,
and the recommended implementation plan.
```

### Independent review

```text
Read prompts/04-review.md and execute it.
```

### Client documentation

The sample document already exists in `docs/api/task-api.md`. To practice regeneration:

```text
$api-document

Regenerate docs/api/task-api.md only after validating
spec/spec-api.md, implementation, tests, and docs/reviews/latest-review.md.
Do not modify source code or tests.
```

---

## 5. Full feature workflow

For a new endpoint or behavior, update the specification first and then use:

```text
Read prompts/00-workflow.md and execute it.
```

Or:

```text
$api-workflow

Implement the approved change in spec/spec-api.md.
Use the repository custom agents and phase skills.
Do not skip gates.
```

The required lifecycle is:

```text
Analyze
  -> approved plan
Implement
  -> compile evidence
Test
  -> non-zero executed tests
Review
  -> no unresolved CRITICAL/HIGH finding
Document
  -> verified client contract
Final verify
  -> mvn clean verify
```

### Expected ownership

| Phase | Skill | Agent | Primary write ownership |
|---|---|---|---|
| Analyze | `$api-analyze` | `api_analyst` | Returns plan content; parent persists `docs/plans/` |
| Implement | `$api-implement` | `api_developer` | `src/main/`, planned production `pom.xml` changes |
| Test | `$api-test` | `api_tester` | `src/test/`, planned test `pom.xml` changes |
| Review | `$api-review` | `api_reviewer` | Read-only; parent persists `docs/reviews/` |
| Document | `$api-document` | `api_doc_writer` | `docs/api/` |

Only one writing agent should own a file at a time.

---

## 6. Phase-by-phase commands

### Analyze

```text
Read prompts/01-analyze.md and execute it.
```

Expected handoff:

```text
READY_FOR_IMPLEMENTATION
```

### Implement

Update the plan path in the prompt when necessary:

```text
Read prompts/02-implement.md and execute it.
```

Expected handoff:

```text
READY_FOR_TEST_IMPLEMENTATION
```

### Test

```text
Read prompts/03-test.md and execute it.
```

Expected handoff:

```text
READY_FOR_REVIEW
```

If a valid specification-based test exposes a production defect, keep the failing test and route the issue to `api_developer`.

### Review

```text
Read prompts/04-review.md and execute it.
```

Expected handoff:

```text
READY_FOR_DOCUMENTATION
```

The review agent is read-only. The parent agent validates the findings and saves the accepted report to `docs/reviews/latest-review.md`.

### Document

```text
Read prompts/05-document.md and execute it.
```

Expected handoff:

```text
READY_FOR_FINAL_VERIFICATION
```

### Fix accepted review findings

```text
Read prompts/06-fix-review.md and execute it.
```

This prompt routes production findings to the developer and test findings to the tester, then requires re-review.

---

## 7. Build and test locally

Prerequisites:

```text
JDK 8
Maven 3.8+
```

Check versions:

```bash
java -version
mvn -version
```

Run all verification:

```bash
./scripts/verify.sh
```

On Windows:

```bat
scripts\verify.cmd
```

Direct Maven command:

```bash
mvn clean verify
```

The successful result must include:

- Maven `BUILD SUCCESS`
- tests actually executed (`Tests run` greater than zero)
- zero failures and errors

A green build with `Tests run: 0` does not satisfy the testing gate.

Build the WAR:

```bash
mvn clean package
```

Output:

```text
target/task-api.war
```

Deploy the WAR to Tomcat 9 and call:

```text
http://localhost:8080/task-api/api/tasks
```

---

## 8. Example API calls

Create:

```bash
curl -i \
  -X POST \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{"title":"Prepare API specification","description":"Write client documentation"}' \
  http://localhost:8080/task-api/api/tasks
```

List:

```bash
curl -i -H 'Accept: application/json' \
  http://localhost:8080/task-api/api/tasks
```

Filter:

```bash
curl -i -H 'Accept: application/json' \
  'http://localhost:8080/task-api/api/tasks?status=OPEN'
```

Update status:

```bash
curl -i \
  -X PATCH \
  -H 'Content-Type: application/json' \
  -H 'Accept: application/json' \
  -d '{"status":"COMPLETED"}' \
  http://localhost:8080/task-api/api/tasks/1/status
```

Delete:

```bash
curl -i -X DELETE \
  http://localhost:8080/task-api/api/tasks/1
```

See `docs/api/task-api.md` for the complete client contract.

---

## 9. Resume after a session or usage limit

Do not start a new Codex session with only `continue` or `do the rest`.

Record workflow state using:

```text
docs/workflow-state.example.md
```

Copy it to a dated or feature-specific file, for example:

```text
docs/workflow-state-task-api.md
```

Start the next session with:

```text
Read AGENTS.md, spec/spec-api.md,
docs/plans/2026-07-19-task-api-plan.md,
and docs/workflow-state-task-api.md.

Continue from the recorded phase only.
Do not repeat completed phases unless verification evidence is stale.
```

Artifacts are the workflow memory. Chat history is helpful, but it should not be the only state store.

---

## 10. Adapt this template to another project

Change at least:

1. `AGENTS.md` technology constraints.
2. `spec/` source-of-truth files.
3. agent instructions that mention Java/Jersey ownership.
4. skill checklists and verification commands.
5. prompt paths and artifact names.
6. Maven/application example code.

Examples:

### JDK 17 + Jersey 4 + Tomcat 11

Change:

```text
Java 8       -> Java 17
Jersey 2.x   -> Jersey 4.x
javax.*      -> jakarta.*
Tomcat 9     -> Tomcat 11
```

### Legacy WebSphere project

Do not reuse this template unchanged. Replace Tomcat/Jersey assumptions with the exact WebSphere, Java, REST framework, classloader, and deployment constraints.

---

## 11. Common mistakes

- Putting task-specific requirements in `AGENTS.md` instead of a prompt or spec.
- Repeating the entire workflow in every prompt instead of using a skill.
- Giving developer and tester overlapping file ownership.
- Letting the reviewer modify the code it reviews.
- Generating documentation before review acceptance.
- Treating an agent summary as verification evidence.
- Reporting `mvn clean verify` as passed without actually running it.
- Locking a model name in every agent file when parent inheritance is enough.
- Adding tools/fields to custom-agent TOML that are not supported by Codex configuration.

---

## 12. Template status

This repository contains:

- detailed Codex project configuration
- five detailed custom-agent definitions
- six detailed repository skills and checklists
- reusable prompts
- a complete API specification
- complete Java sample source with Java 8 core-compilation evidence
- unit, repository, and Jersey integration tests
- an example review report
- client-facing API documentation
- Linux/macOS and Windows verification scripts

Review `docs/template-review.md` for the checks performed when this archive was generated.
