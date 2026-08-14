Perform a read-only, evidence-based performance review.

Scope:

```text
<endpoint/service/repository/package>
```

Evidence:

```text
<latency metrics, profiler output, thread/heap data, query/log evidence>
```

Trace hot paths and identify verified algorithmic, allocation, concurrency,
I/O, serialization, or configuration risks. Distinguish measured bottlenecks
from hypotheses. Do not recommend caching, concurrency, or dependencies without
an observed problem and a verification plan.
