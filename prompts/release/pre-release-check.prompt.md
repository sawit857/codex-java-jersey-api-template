Perform a pre-release readiness check without changing code unless explicitly assigned.

Verify:

- specification and implementation consistency
- non-zero passing tests
- no unresolved CRITICAL/HIGH review findings
- current client API documentation
- Java 8/Jersey/Tomcat constraints
- no secrets, debug output, generated build artifacts, or unexplained TODOs
- fresh `mvn clean verify` result

Return READY, NOT_READY, or BLOCKED with exact evidence and required owners.
