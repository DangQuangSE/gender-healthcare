# Gender Healthcare Backend

Spring Boot modular monolith for the Gender Healthcare platform.

## Architecture

The backend is one deployable application. Business ownership is grouped by module:

```text
src/main/java/com/S_Health/GenderHealthCare/
├── common/          # shared response, validation, security, filters and exceptions
├── config/           # application-wide Spring configuration
├── modules/
│   ├── identity/     # login, registration and token handling
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
│   ├── payment/      # payment and settlement workflows
│   └── reporting/
└── integrations/    # external service adapters
```

Each module owns its controllers, services, DTOs, domain entities and enums. `GlobalExceptionHandler` converts expected domain errors and unexpected server errors into the common `ApiResponse` format.

## Local setup

Requirements: Java 21 and the project's local runtime dependencies.

Provide runtime configuration through your local development tooling, then start the application:

```powershell
.\mvnw.cmd spring-boot:run
```

## API contract

New REST endpoints use `/api/v1` and return the common response envelope. Examples:

```text
POST /api/v1/auth/login
GET  /api/v1/services
POST /api/v1/appointments
GET  /api/v1/medical-results/{id}
```

Old `/api/**` routes remain as explicitly named `Legacy*Controller` adapters while the frontend and provider callbacks are being verified. The current migration and compatibility map is maintained in [`plans/gender-healthcare-full-refactor/endpoint-security-compatibility.md`](../plans/gender-healthcare-full-refactor/endpoint-security-compatibility.md). Payment creation and status routes require authentication.

## Verification

Run the backend test suite and package build:

```powershell
.\mvnw.cmd test
.\mvnw.cmd -DskipTests package
```

Use the manual flow and deployment guidance in [`plans/gender-healthcare-refactor/docs/deployment/runbook.md`](../plans/gender-healthcare-refactor/docs/deployment/runbook.md).

## Deployment

Production deployment is handled by [`.github/workflows/deploy-vps.yml`](.github/workflows/deploy-vps.yml).
Pull requests build the application and validate the Docker image. A push to
`main` archives the backend, deploys it to the VPS, rebuilds the image, and
verifies the health endpoints.

Runtime configuration is managed outside the repository. See
[`deploy/ci-cd.md`](deploy/ci-cd.md) for CI/CD settings and
[`deploy/docker-vps.md`](deploy/docker-vps.md) for the VPS layout.

## External services

Integration settings are maintained outside source control. Operational details
are documented with the deployment and integration runbooks.
