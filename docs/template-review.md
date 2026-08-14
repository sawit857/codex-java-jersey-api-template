# Template Generation Review

This file records the checks performed when the downloadable template archive was generated.

## Content checks

- [x] `README.md` contains Codex.app setup, discovery, phase commands, build, resume, and adaptation guidance.
- [x] `AGENTS.md` contains project policy, technology constraints, ownership, workflow gates, safety, review severity, and definition of done.
- [x] `.codex/config.toml` contains bounded subagent concurrency/depth.
- [x] Five custom agents contain `name`, `description`, `sandbox_mode`, and detailed `developer_instructions`.
- [x] Six skills contain required YAML `name` and `description` metadata.
- [x] Each phase skill has a detailed checklist/reference.
- [x] Prompt library covers full workflow, each phase, finding fixes, feature, bugfix, and refactor.
- [x] API specification defines six endpoints, DTOs, validation, errors, limitations, and examples.
- [x] Maven/Jersey Java source and JUnit/Jersey tests are included.
- [x] Review, API document, plan, and workflow-state artifact examples are included.
- [x] macOS/Linux and Windows verification scripts are included.

## Static consistency checks

- [x] Agent names used in prompts/skills match `.codex/agents` `name` values.
- [x] Skill names used in prompts match skill frontmatter.
- [x] Spec endpoint paths match resource annotations.
- [x] Spec fields match DTO fields.
- [x] Status values match `TaskStatus`.
- [x] Error codes match exception mappers.
- [x] `web.xml` maps Jersey to `/api/*`.
- [x] WAR final name is `task-api`.

## Verification evidence

See `VERIFICATION.txt` at repository root for commands and actual outputs from archive generation.

Generation-time evidence includes:

- static TOML/XML/JSON/Markdown/contract consistency checks
- Java 8 syntax/type compilation of all production and test source using generated external-API stubs
- executable core service/repository behavior smoke test

A real `mvn clean verify` was not available in the generation environment because Maven was not installed and shell network resolution was unavailable. Run the included verification script locally before treating the sample as runtime-verified.

## Caveat

The checked-in review/API document are examples of workflow artifacts. When using this template for a new change, regenerate them from the current repository state rather than treating them as permanent proof.
