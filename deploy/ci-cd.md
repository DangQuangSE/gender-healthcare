# CI/CD setup

## Backend workflow

`.github/workflows/deploy-vps.yml` runs these jobs:

1. `build` compiles the Spring Boot artifact and stores a source release archive.
2. `docker-build` builds the Docker image as a separate CI check.
3. `deploy` runs only for `main` pushes or a manual dispatch, then updates the
   backend on the VPS.
4. `verify-vps` checks the private `127.0.0.1:8086/health` endpoint over SSH.
5. `verify-public` checks `https://shealth.duckdns.org/health` through host Nginx.

Pull requests run the build and Docker jobs only. A push to `main` runs the
full chain after the `production` environment's protection rules are satisfied.

The workflow reads these non-secret repository variables and fails before the
build when either is missing:

| Variable | Example |
| --- | --- |
| `VPS_DEPLOY_PATH` | `/opt/s-health/backend` |
| `PUBLIC_API_URL` | `https://shealth.duckdns.org` |

## GitHub environment secrets

Create a GitHub environment named `production` in the backend repository and
add these **environment secrets** with exactly these names:

| Secret | Value |
| --- | --- |
| `ORACLE_HOST` | VPS hostname or IP address |
| `ORACLE_USER` | SSH user, currently `ubuntu` |
| `ORACLE_SSH_PRIVATE_KEY` | Complete private-key file contents used for SSH |

The private key belongs in GitHub Secrets, never in the repository or a normal
environment variable. The workflow uses the same secret names as sport-pro.

## Frontend workflow

`.github/workflows/build.yml` installs dependencies, builds the Vite bundle with
the public API and WebSocket URLs, and verifies `dist/index.html` in a second
job. The production bundle is served from the shared VPS at
`shealth.duckdns.org`; the publishing layout is documented in
`deploy/frontend-vps.md`.

Add these public values as repository **Actions variables** in the frontend
repository. The workflow fails before `npm ci` when any value is missing:

| Variable | Example |
| --- | --- |
| `VITE_API_BASE_URL` | `https://shealth.duckdns.org/api` |
| `VITE_WEBSOCKET_URL` | `https://shealth.duckdns.org/ws/chat` |
| `VITE_CLOUDINARY_UPLOAD_URL` | Cloudinary upload endpoint |
| `VITE_CLOUDINARY_UPLOAD_PRESET` | Unsigned upload preset |

## Required external routing

Before expecting `verify-public` to pass, DuckDNS must resolve
`shealth.duckdns.org` to the VPS public IP, the host firewall/OCI security list
must allow TCP 80 and 443, and Nginx must route the hostname to the static
frontend and `127.0.0.1:8086`.
