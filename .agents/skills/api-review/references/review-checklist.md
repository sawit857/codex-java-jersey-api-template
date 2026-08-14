# Review checklist

## Contract
- [ ] methods/paths/media types
- [ ] request/response fields
- [ ] success/error statuses and codes
- [ ] Location header and 204 empty body
- [ ] validation and enum values

## Compatibility/configuration
- [ ] Java 8 compiler/source APIs
- [ ] `javax` namespace
- [ ] Jersey dependency consistency
- [ ] Surefire/JUnit 5 discovery
- [ ] WAR and `web.xml`

## Implementation
- [ ] Resource -> Service -> Repository
- [ ] service validation/timestamps
- [ ] repository defensive copy/concurrency
- [ ] specific/generic exception mapping
- [ ] no internal information exposure

## Tests
- [ ] actual assertions inspected
- [ ] positive/error scenarios
- [ ] 201 + Location
- [ ] 204 + empty entity
- [ ] deterministic/isolated/no sleeps
- [ ] no weakened/disabled valid tests
- [ ] non-zero test execution evidence

## Findings
- [ ] exact ID, path, symbol/location
- [ ] accurate specification reference
- [ ] concrete evidence and impact
- [ ] smallest correction and correct owner
- [ ] severity justified
- [ ] no duplicate/speculative/out-of-scope issue
- [ ] accepted report persisted by parent
