Analyze and refactor the target without changing public API behavior.

Target:

```text
<class/package>
```

Problem:

```text
<duplication, complexity, coupling, or maintainability issue>
```

Use `$api-analyze` to map callers, behavior, tests, dependencies, and risks.

Then use `$api-implement`, `$api-test`, and `$api-review`.

Constraints:

- preserve endpoint paths, fields, statuses, errors, and validation
- preserve Java 8/Jersey/Tomcat compatibility
- avoid unrelated cleanup
- do not rewrite the module wholesale
- add characterization/regression tests before changing fragile behavior when coverage is insufficient
- document only externally visible changes (normally none)
