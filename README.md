# Gender Healthcare Backend

Spring Boot modular monolith for the Gender Healthcare platform.

## Architecture

The backend is one deployable application. Business ownership is grouped by module:

```text
src/main/java/com/S_Health/GenderHealthCare/
├── common/          # shared response, validation, security, filters and exceptions
├── config/           # application-wide Spring configuration
├── modules/
│   ├── identity/     # login, registration, JWT and OAuth clients
│   ├── user/         # profile, certification and user administration
│   ├── catalog/      # services, rooms, tags and specializations
│   ├── scheduling/   # consultant schedules and slots
│   ├── appointment/  # booking, appointment status and check-in
│   ├── medical/      # profiles, medical results and treatment protocols
│   ├── health/       # application liveness endpoint and response DTO
│   ├── healthtracking/
│   ├── content/      # blogs, comments and tags
│   ├── communication/# chat and notifications
│   ├── feedback/
│   ├── payment/      # PayOS payment and settlement workflows
│   └── reporting/
└── integrations/    # mail, storage, Zoom and payment provider adapters
```

Each module owns its controllers, services, DTOs, domain entities and enums. `GlobalExceptionHandler` converts expected domain errors and unexpected server errors into the common `ApiResponse` format.

## Local setup

Requirements: Java 21, MySQL 8 and a POSIX-compatible shell for deployment scripts.

1. Create a database named `genderhealthcare`.
2. Copy `.env.example` to `.env` and replace every `change-me`/`replace-with-*` value.
3. Start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

The default local port is `8085`. A safe unauthenticated liveness endpoint is:

```text
GET http://localhost:8085/health
```

## API contract

New REST endpoints use `/api/v1` and return the common response envelope. Examples:

```text
POST /api/v1/auth/login
GET  /api/v1/services
POST /api/v1/appointments
GET  /api/v1/medical-results/{id}
POST /api/v1/payments/payos
POST /api/v1/payments/payos/deposit
GET  /api/v1/payments/payos/{orderCode}
POST /api/v1/payments/payos/webhook
```

Old `/api/**` routes remain as explicitly named `Legacy*Controller` adapters while the frontend and provider callbacks are being verified. The current migration and compatibility map is maintained in [`plans/gender-healthcare-full-refactor/endpoint-security-compatibility.md`](../plans/gender-healthcare-full-refactor/endpoint-security-compatibility.md). PayOS webhook delivery uses the exact public route `/api/v1/payments/payos/webhook`; payment creation and status routes require authentication.

## Verification

Run the backend test suite and package build:

```powershell
.\mvnw.cmd test
.\mvnw.cmd -DskipTests package
```

Use the manual flow and deployment guidance in [`plans/gender-healthcare-full-refactor/runbook.md`](../plans/gender-healthcare-full-refactor/runbook.md).

## Deployment

The deployment path uses the repository deployment script and a managed Spring Boot jar. Configure the server-side values as environment variables, then run:

```bash
export DEPLOY_HOST=your-server.example
export DEPLOY_USER=deploy
./deploy/backend-deploy.sh
```

The script builds the jar, uploads a timestamped release, switches the `current` symlink, restarts systemd, checks `/health`, and rolls back the symlink if verification fails. Server preparation and rollback details are in [`plans/gender-healthcare-full-refactor/runbook.md`](../plans/gender-healthcare-full-refactor/runbook.md).

## External services

Runtime credentials are injected through `.env` or the server environment. The source contains no production secrets. Supported adapters are PayOS, Cloudinary, Zoom and SMTP mail. Configure the PayOS webhook using [`docs/payos-webhook-runbook.md`](docs/payos-webhook-runbook.md).
