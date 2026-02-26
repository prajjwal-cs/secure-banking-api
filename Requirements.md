# Production-Readiness Requirements

This project is a strong **learning/demo foundation**, but it is **not production-ready yet** without the requirements below.

## 1) Security Baseline
- [ ] Use environment variables or secret manager for all secrets (`APP_JWT_SECRET`, DB password).
- [ ] Enforce strong JWT secret (minimum 256-bit random secret) and rotation policy.
- [ ] Configure HTTPS only at runtime (TLS termination + secure headers).
- [ ] Add request validation on every input DTO and consistent error responses.
- [ ] Add account lockout policy with unlock workflow and audit trail.
- [ ] Add refresh-token revocation strategy (device/session-aware revocation preferred).

## 2) Database & Data Integrity
- [ ] Use schema migrations (Flyway/Liquibase); do not rely on `ddl-auto=update`.
- [ ] Set `ddl-auto=validate` in production.
- [ ] Add unique constraints and indexes for `username`, `email`, transaction lookup fields.
- [ ] Introduce optimistic locking where concurrent updates are possible.
- [ ] Back up strategy + restore drills.

## 3) API & Application Hardening
- [ ] Add API versioning (`/api/v1/...`).
- [ ] Add OpenAPI/Swagger documentation.
- [ ] Add structured logging + correlation IDs.
- [ ] Add global request/response observability (latency, error rates, rate-limit hits).
- [ ] Add idempotency support for transfer/payment APIs.

## 4) Testing Requirements
- [ ] Unit tests for services and security components.
- [ ] Controller/integration tests for auth flow, role access, and money movement edge cases.
- [ ] Testcontainers-based integration tests for PostgreSQL.
- [ ] Security regression tests (invalid JWT, expired token, replay scenarios).
- [ ] Performance smoke tests and basic load profile.

## 5) Delivery & Operations
- [ ] CI pipeline: build, test, static analysis, dependency vulnerability scan.
- [ ] CD with environment promotion and rollback strategy.
- [ ] Production profiles (`application-prod.properties`) and sane defaults per environment.
- [ ] Health/readiness endpoints + alerting.
- [ ] Error budget / SLO definitions (availability, p95 latency, auth failures).

## 6) Resume-Ready Enhancements
To position this as a strong resume project, include these visible improvements:
- [ ] Add Flyway migrations and seed scripts.
- [ ] Add GitHub Actions CI with test + SpotBugs/Checkstyle + OWASP dependency check.
- [ ] Add Docker Compose (API + PostgreSQL) and clear runbook.
- [ ] Add integration test suite proving secure auth + transaction consistency.
- [ ] Add architecture diagram and threat model summary.
