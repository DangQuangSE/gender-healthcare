# Backend deployment on the shared VPS

The backend runs beside sport-pro on the same VPS. The stacks are isolated by
their Compose project names, networks, containers, and database volumes.

```text
/opt/s-health/backend/
|-- .env                         # runtime secrets, never committed
`-- deploy/docker-compose.vps.yml
```

## Runtime layout

- Spring Boot listens on `8085` inside its container.
- Only `127.0.0.1:8086` is published on the VPS.
- Cloudflare Tunnel forwards `api.s-health.xyz` to `http://127.0.0.1:8086`.
- MySQL has no host port and is reachable only on `s_health_network`.
- The database persists in the `s_health_mysql_data` Docker volume.

Port `8086` is deliberate: sport-pro uses its own internal API port and is not
changed by this stack.

## First-time VPS setup

Install Docker Engine and the Compose plugin, then create the runtime file:

```bash
sudo mkdir -p /opt/s-health/backend
cd /opt/s-health/backend
git clone https://github.com/DangQuangSE/gender-healthcare.git .
touch .env
chmod 600 .env
```

Create `.env` from the approved local configuration and fill every key listed in
`deploy/required-env.list`. Keep this file on the VPS only. The deployment
workflow checks that every listed key exists and is non-empty before Compose is
started.

Validate and start the backend stack:

```bash
docker compose --project-name s-health --env-file .env \
  -f deploy/docker-compose.vps.yml config --quiet
docker compose --project-name s-health --env-file .env \
  -f deploy/docker-compose.vps.yml up -d --build
docker compose --project-name s-health --env-file .env \
  -f deploy/docker-compose.vps.yml ps
curl --fail http://127.0.0.1:8086/health
```

Do not run `docker compose down -v` on this stack unless the database volume is
intentionally being destroyed.

## Updates and rollback

The backend workflow uploads a source archive, copies the existing `.env` into
the release, rebuilds the ARM VPS image there, and checks `/health` before
switching the release directory. If the health check fails, it attempts to
restart the previous Compose definition.

The workflow does not copy `.env` from GitHub and does not publish MySQL or the
Spring Boot port directly to the Internet.
