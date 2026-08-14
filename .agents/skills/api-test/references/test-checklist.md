# Test checklist

## Framework
- [ ] JUnit 5 discovery configured
- [ ] AssertJ available
- [ ] Jersey Test Framework configured
- [ ] Java 8-compatible dependencies/plugins

## Service
- [ ] valid create/default OPEN
- [ ] missing/blank title
- [ ] existing/unknown get
- [ ] update fields
- [ ] valid/invalid status
- [ ] existing/unknown delete
- [ ] fixed Clock for exact times

## Repository
- [ ] save/find
- [ ] unique IDs
- [ ] list/filter/delete
- [ ] defensive copies
- [ ] fresh state per test
- [ ] normal concurrent creation

## HTTP
- [ ] POST create: 201, Location, JSON
- [ ] GET list/filter
- [ ] GET existing/unknown
- [ ] PUT update
- [ ] PATCH status
- [ ] DELETE: 204, empty body
- [ ] validation/not-found/invalid status
- [ ] malformed JSON/unsupported media type where supported
- [ ] JSON error shape and no internal detail

## Execution
- [ ] focused tests executed
- [ ] `mvn test` executed
- [ ] non-zero test count
- [ ] failures/errors/skips recorded
- [ ] valid failing tests retained
- [ ] accurate handoff
