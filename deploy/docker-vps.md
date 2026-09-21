# Docker deployment on a VPS

The Compose file lives in the backend repository and expects both repositories
to be siblings:

```text
/opt/gender-healthcare/
|-- gender-healthcare/
`-- gender-healthcare-fe/
```

Only the frontend container is published. MySQL and Spring Boot stay on the
private Docker network.

## 1. Prepare the server

Install Docker Engine and the Docker Compose plugin. Then clone or update both
repositories and create the runtime environment file:

```bash
cd /opt/gender-healthcare/gender-healthcare
cp deploy/backend.env.example .env
chmod 600 .env
```

Fill in every `change-me` or `replace-with-*` value in `.env`. Important VPS
values include:

```dotenv
HTTP_BIND=127.0.0.1:8080
JPA_DDL_AUTO=validate
CORS_ALLOWED_ORIGINS=https://your-domain.example
VITE_API_BASE_URL=/api
VITE_WEBSOCKET_URL=/ws/chat
VITE_GOOGLE_CLIENT_ID=your-google-client-id
PAYOS_RETURN_URL=https://your-domain.example/user/booking
PAYOS_CANCEL_URL=https://your-domain.example/user/booking
PAYOS_WEBHOOK_URL=https://your-domain.example/api/v1/payments/payos/webhook
```

`HTTP_BIND=80` is suitable when this stack owns the public HTTP port directly.
Use `127.0.0.1:8080` when a host-level Nginx, Caddy or another TLS proxy will
terminate HTTPS. PayOS webhook delivery must use a publicly reachable HTTPS
URL.

## 2. Check and start the stack

Run these commands from the backend repository:

```bash
docker compose --env-file .env config --quiet
docker compose --env-file .env up -d --build
docker compose ps
docker compose logs --tail=100 backend
```

The frontend serves the SPA and proxies `/api/` and `/ws/` to the backend. The
backend health endpoint is `/health`; the frontend health endpoint is `/health`.

## 3. Database and migrations

The named `mysql_data` volume persists database data across container updates.
The default `JPA_DDL_AUTO=validate` intentionally prevents Hibernate from
silently changing the production schema. Restore or provision the application
schema before the backend is started, and execute reviewed SQL migrations
through the database change process. The PayOS migration is documented in
`docs/migrations/README.md`.

Back up before migrations and before removing the stack. Do not use
`docker compose down -v` on production unless the database volume is
deliberately being destroyed.

## 4. Update and rollback

```bash
git pull
docker compose --env-file .env up -d --build
docker compose ps
```

If a new image is unhealthy, inspect `docker compose logs backend` and return to
the previously verified Git revision before rebuilding. Keep `.env` outside
Git; only `.env.example` is intended to be versioned.
