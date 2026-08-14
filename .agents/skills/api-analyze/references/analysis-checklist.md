# Analysis checklist

## Instructions and scope

- [ ] Root and applicable nested `AGENTS.md` read
- [ ] Exact specification and requested scope identified
- [ ] In-scope/out-of-scope behavior explicit
- [ ] Public-contract ambiguity separated from internal implementation choices

## Contract

- [ ] methods and paths
- [ ] consumes/produces
- [ ] path/query parameters
- [ ] request/response fields
- [ ] success statuses and headers
- [ ] error statuses/codes/body
- [ ] validation rules
- [ ] enum values

## Implementation evidence

- [ ] `pom.xml` and Java source level
- [ ] resource annotations
- [ ] DTO JSON names
- [ ] service validation and timestamps
- [ ] repository ID/storage/copy behavior
- [ ] exception mapper registration
- [ ] `web.xml` and application configuration

## Test evidence

- [ ] service tests and assertions
- [ ] repository tests and isolation
- [ ] Jersey tests and assertions
- [ ] 201 + Location
- [ ] 204 + empty body
- [ ] validation, not-found, invalid status, malformed JSON, media type
- [ ] actual test discovery/configuration

## Output quality

- [ ] exact existing/proposed paths
- [ ] owner for every changed file
- [ ] every production change has test scenario
- [ ] risks include evidence/impact/mitigation
- [ ] final status matches evidence
- [ ] no file modified
