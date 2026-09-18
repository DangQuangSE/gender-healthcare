#!/usr/bin/env bash
set -Eeuo pipefail

: "${DEPLOY_HOST:?Set DEPLOY_HOST to the server hostname or IP}"

DEPLOY_USER="${DEPLOY_USER:-deploy}"
SSH_PORT="${SSH_PORT:-22}"
DEPLOY_DIR="${DEPLOY_DIR:-/var/www/gender-healthcare}"
SERVICE_NAME="${SERVICE_NAME:-gender-healthcare}"
HEALTH_URL="${HEALTH_URL:-http://127.0.0.1:${APP_PORT:-8085}/health}"

remote() {
    ssh -p "${SSH_PORT}" "${DEPLOY_USER}@${DEPLOY_HOST}" "$@"
}

echo "Current release:"
remote "readlink -f '${DEPLOY_DIR}/current' || true"

echo
echo "Service status:"
remote "sudo systemctl status '${SERVICE_NAME}' --no-pager || true"

echo
echo "Health check:"
remote "curl --fail --silent --show-error --max-time 5 '${HEALTH_URL}' || true"
echo

echo "Recent logs:"
remote "sudo journalctl -u '${SERVICE_NAME}' -n 80 --no-pager || true"
