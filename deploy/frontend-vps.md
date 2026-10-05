# S-Health frontend on the shared VPS

The production frontend is built as a Vite static bundle and served by the
host Nginx instance. Nginx also proxies the S-Health API and SockJS endpoint to
the backend container. This keeps the browser on one HTTPS origin and does not
use Vercel or a Cloudflare Tunnel.

```text
shealth.duckdns.org
  /              -> /opt/s-health/frontend/current (Vite dist)
  /health        -> 127.0.0.1:8086/health
  /api/*         -> 127.0.0.1:8086/api/*
  /ws/*          -> 127.0.0.1:8086/ws/*
```

The backend remains private at `127.0.0.1:8086`; MySQL is private to the
Compose network. Only Nginx ports 80 and 443 are public.

## Build and publish manually

Build the frontend with the public same-origin URLs:

```powershell
$env:VITE_API_BASE_URL = "https://shealth.duckdns.org/api"
$env:VITE_WEBSOCKET_URL = "https://shealth.duckdns.org/ws/chat"
npm ci
npm run build
```

Publish `dist` as a timestamped release under `/opt/s-health/frontend/releases`
and update the `current` symlink atomically. The Nginx source file is
`deploy/nginx.shealth.conf`; install it at
`/etc/nginx/sites-available/s-health`, link it into `sites-enabled`, then run
`nginx -t` before reloading Nginx.

## HTTPS and renewal

DuckDNS must resolve `shealth.duckdns.org` to the VPS public IP and inbound TCP
80/443 must be allowed. The first certificate is issued with Certbot's webroot
challenge using `/var/www/certbot`. Keep the certificate directory and renew
it before expiry; reload Nginx only after `nginx -t` succeeds.

## Verification

```bash
curl --fail https://shealth.duckdns.org/health
curl --fail https://shealth.duckdns.org/
curl --fail https://shealth.duckdns.org/ws/chat/info
```

An authenticated API route returning `401` is an application authentication
result and confirms that the Nginx-to-backend route is reachable.
