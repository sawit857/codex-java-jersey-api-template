# Implementation checklist

## Preconditions
- [ ] approved plan exists and is explicitly accepted
- [ ] plan has no blocking question
- [ ] plan agrees with spec
- [ ] working tree inspected

## Ownership
- [ ] only planned `src/main` files changed
- [ ] `pom.xml` change is explicitly planned and production-related
- [ ] no `src/test`, `spec`, review, or API-doc change

## Design
- [ ] Resource -> Service -> Repository maintained
- [ ] request DTOs exclude server-managed fields
- [ ] service owns business validation/timestamps
- [ ] repository does not expose mutable internal state
- [ ] expected exceptions have specific mapping
- [ ] generic mapper does not expose internals

## Compatibility
- [ ] Java 8 syntax/APIs
- [ ] `javax.ws.rs` and `javax.servlet`
- [ ] dependencies support Java 8
- [ ] WAR/Tomcat 9 configuration remains valid

## Diff and evidence
- [ ] no unrelated formatting/debug/TODO/secrets
- [ ] `mvn -DskipTests compile` executed
- [ ] relevant tests executed when practical
- [ ] exact command output recorded
- [ ] accurate handoff selected
