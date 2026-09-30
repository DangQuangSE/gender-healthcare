# CI/CD setup

## Backend workflow

`.github/workflows/deploy-vps.yml` runs these jobs:

1. `build` compiles the Spring Boot artifact and stores a source release archive.
2. `docker-build` builds the Docker image as a separate CI check.
3. `deploy` runs only for `main` pushes or a manual dispatch, then updates the
   backend on the VPS.
4. `verify-vps` checks the private `127.0.0.1:8086/health` endpoint over SSH.
5. `verify-public` checks `https://api.s-health.xyz/health` through Cloudflare.

Pull requests run the build and Docker jobs only. A push to `main` runs the
full chain after the `production` environment's protection rules are satisfied.

The workflow reads these non-secret repository variables and fails before the
build when either is missing:

| Variable | Example |
| --- | --- |
| `VPS_DEPLOY_PATH` | `/opt/s-health/backend` |
| `PUBLIC_API_URL` | `https://api.s-health.xyz` |

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

## Frontend workflow and Vercel

`.github/workflows/build.yml` installs dependencies, builds the Vite bundle with
the public API and WebSocket URLs, and verifies `dist/index.html` in a second
job. Vercel remains the deployment owner for the frontend: its Git integration
deploys the production project when `main` is pushed. The workflow does not
duplicate that deployment or require a Vercel token.

Add these public values as repository **Actions variables** in the frontend
repository. The workflow fails before `npm ci` when any value is missing:

| Variable | Example |
| --- | --- |
| `VITE_API_BASE_URL` | `https://api.s-health.xyz/api` |
| `VITE_WEBSOCKET_URL` | `https://api.s-health.xyz/ws/chat` |
| `VITE_CLOUDINARY_UPLOAD_URL` | Cloudinary upload endpoint |
| `VITE_CLOUDINARY_UPLOAD_PRESET` | Unsigned upload preset |

## Required external routing

Before expecting `verify-public` to pass, Cloudflare DNS must point
`api.s-health.xyz` to the active tunnel hostname and keep the record proxied.
The tunnel route must target `http://127.0.0.1:8086`. The root and `www`
frontend records remain managed by Vercel.
