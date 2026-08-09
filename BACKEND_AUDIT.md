# Backend Audit

Audit of the Smart Farmer AI Spring Boot backend after completing the existing architecture.
Every PASS below was observed on this branch; nothing is marked PASS because a file exists.

## How this was verified

| Check | Command / evidence |
| --- | --- |
| Build | `mvn -B clean verify` |
| Tests | `mvn -B clean test` — 29 tests, 0 failures (JUnit 5, Mockito, Testcontainers PostgreSQL 16) |
| Runtime | `docker compose up -d --build`, application container reported `healthy` |
| Database | Flyway applied V1–V4 on an empty PostgreSQL 16 database, then Hibernate `ddl-auto: validate` accepted every entity mapping |
| Error handling | 400 for bad enums/malformed JSON/bad UUIDs/missing params, 404 for unknown routes, 405 for wrong methods, 409 duplicates, 503 unavailable providers — all uniform `ApiResponse` JSON |
| APIs | curl smoke run against the running container (health, auth lifecycle, farms, crops, markets, schemes, notifications, reports, disease scan upload, recommendations, weather, assistant, admin) |

## Result table

| Area | Status | Notes |
| --- | --- | --- |
| Build | PASS | `mvn clean verify` succeeds on Java 21 / Spring Boot 3.5.4 |
| Tests | PASS | 29 tests: JWT unit tests, auth lifecycle, farm ownership, admin authorization, provider boundaries, recommendation service, error mapping / session revocation / reports |
| Database | PASS | V1–V4 apply cleanly; `ddl-auto: validate` (never `update`) passes against the migrated schema |
| Authentication | PASS | register / login / refresh / logout / me plus forgot-password, reset-password, verify-otp, resend-otp |
| Authorization | PASS | Role authorities on the JWT, `/api/v1/admin/**` requires `ADMIN` (403 for other roles), per-resource ownership checks return 403; deactivating a user invalidates their in-flight access tokens |
| Users | PASS | profile, preferences, password change, deactivation, farmer profile — all DTO based |
| Farms | PASS | paginated CRUD scoped to the owner |
| Fields | PASS | CRUD under a farm, cross-farm access rejected |
| Crops | PASS | catalog + search + crop seasons; V4 seeds a real crop catalogue |
| Disease | PASS (provider boundary) | upload, validation, storage abstraction, status, history, ownership. With no AI provider configured the scan stays `PENDING`; no synthetic diagnosis is ever stored |
| Recommendations | PASS (provider boundary) | crop / fertilizer / irrigation persisted with history. Without a provider the record is `PENDING` with no text; provider errors record `FAILED` |
| Weather | PASS (provider boundary) | provider abstraction over `app.weather.base-url`; unconfigured or unreachable providers return 503 instead of invented measurements |
| Market | PASS | markets, prices, search, state filter, pagination and price trends (min/max/average plus points); admin writes |
| Schemes | PASS | search, state/category/active filters, eligibility, pagination; admin CRUD |
| Notifications | PASS | paginated listing, unread filter, mark one/all read, delete, ownership; admin can target one user or broadcast |
| Assistant | PASS (provider boundary) | conversations CRUD and message history. Answers come from the configured Gemini provider; with no `GEMINI_API_KEY` the call returns 503 and the exchange is rolled back rather than storing a placeholder answer |
| Admin | PASS | users, activate/deactivate, statistics, audit logs, scheme CRUD, market data, notifications |
| Audit logging | PASS | `audit_logs` written for registration, login, admin actions; paginated admin read API |
| Swagger | PASS | `/v3/api-docs` and `/swagger-ui/index.html` return 200; bearer security scheme documented |
| Reports | PASS | generate / list / get / delete, scoped to the owner; metadata holds real aggregated counts of the caller's farms, crop seasons, scans and recommendations |
| Docker | PASS | multi-stage `Dockerfile` (non-root runtime) and `docker-compose.yml` with a PostgreSQL healthcheck and `depends_on: service_healthy` |

## Deliberate limitations

These are boundaries, not omissions — the task forbids creating the AI service and forbids faking ML output.

- **No ML model, no FastAPI service.** `AiServiceClient` is the only integration point. `app.ai-service.base-url` is empty by default, so `isAvailable()` is false and disease scans / recommendations stay `PENDING` with no result rows.
- **No weather provider bundled.** `app.weather.base-url` is empty by default and the endpoints answer `503 Service Unavailable`.
- **Assistant needs a real key.** `GEMINI_API_KEY` enables the Google Generative Language integration; the key is read from configuration and never returned or logged.

## Security notes

- No credentials, keys or tokens are committed; every secret comes from an environment variable and `.env.example` holds placeholders only.
- `app.jwt.secret` has no production default: startup fails unless `JWT_SECRET` is at least 32 characters. Dev/test profiles carry a local-only value.
- Passwords are BCrypt hashed and never serialised; refresh tokens and OTP codes are stored as SHA-256 hashes.
- Refresh tokens are typed (`ACCESS` vs `REFRESH`), rotated on every refresh and revoked on logout; a reused token is rejected with 401.
- OTP codes are generated with `SecureRandom`, never logged, and only echoed in the response under the dev/test profiles.
- Error responses are uniform `ApiResponse` payloads (400/401/403/404/409/503/500) and never contain stack traces.

## Reproducing the verification

```bash
mvn clean verify

export JWT_SECRET=$(openssl rand -hex 32)
docker compose up -d --build
curl http://localhost:8080/api/v1/health
```
