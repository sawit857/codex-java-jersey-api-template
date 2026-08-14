Perform a behavior-preserving legacy Java cleanup.

Target:

```text
<class/package/module>
```

Runtime constraints:

```text
<JDK, app server, Jersey/Servlet namespace, database if relevant>
```

Use `$api-analyze` first to map callers, public behavior, exceptions, logging,
configuration, tests, and fragile dependencies. Add characterization tests with
`$api-test` when coverage is insufficient, then use `$api-implement` and
`$api-review`. Do not upgrade platforms, rename public APIs, or rewrite the
module wholesale without explicit approval.
