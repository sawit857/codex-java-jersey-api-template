Use `$api-analyze` first.

Defect:

```text
<describe observed behavior>
```

Evidence:

```text
<paste logs, stack trace, failing request/response, or test output>
```

Expected behavior and source:

```text
<specification section or accepted requirement>
```

Workflow:

1. identify root cause and regression scope without modifying files
2. create/approve a focused fix plan
3. use `$api-implement` for production correction
4. use `$api-test` to add/preserve a regression test and run the suite
5. use `$api-review` for independent verification
6. update documentation only when the external contract changes

Do not weaken tests or modify the specification to match incorrect behavior.
