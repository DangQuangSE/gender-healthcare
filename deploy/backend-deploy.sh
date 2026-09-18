#!/usr/bin/env bash
set -Eeuo pipefail

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(CDPATH= cd -- "${SCRIPT_DIR}/.." && pwd)"

: "${DEPLOY_HOST:?Set DEPLOY_HOST to the server hostname or IP}"

DEPLOY_USER="${DEPLOY_USER:-deploy}"
SSH_PORT="${SSH_PORT:-22}"
DEPLOY_DIR="${DEPLOY_DIR:-/var/www/gender-healthcare}"
SERVICE_NAME="${SERVICE_NAME:-gender-healthcare}"
SERVICE_USER="${SERVICE_USER:-genderhealthcare}"
SERVICE_GROUP="${SERVICE_GROUP:-genderhealthcare}"
APP_NAME="${APP_NAME:-be.jar}"
APP_PORT="${APP_PORT:-8085}"
HEALTH_URL="${HEALTH_URL:-http://127.0.0.1:${APP_PORT}/health}"
HEALTH_RETRIES="${HEALTH_RETRIES:-30}"

LOCAL_ARTIFACT="${PROJECT_DIR}/target/${APP_NAME}"
RELEASE_ID="$(date -u +%Y%m%d%H%M%S)"
REMOTE_RELEASE_DIR="${DEPLOY_DIR}/releases/${RELEASE_ID}"

remote() {
    ssh -p "${SSH_PORT}" "${DEPLOY_USER}@${DEPLOY_HOST}" "$@"
}

echo "Building ${APP_NAME}..."
cd "${PROJECT_DIR}"
./mvnw -DskipTests package

if [[ ! -f "${LOCAL_ARTIFACT}" ]]; then
    echo "Build artifact not found: ${LOCAL_ARTIFACT}" >&2
    exit 1
fi

PREVIOUS_RELEASE="$(remote "readlink -f '${DEPLOY_DIR}/current' 2>/dev/null || true" | tr -d '\r')"

echo "Preparing release ${RELEASE_ID}..."
remote "sudo mkdir -p '${REMOTE_RELEASE_DIR}' '${DEPLOY_DIR}/releases' && sudo chown -R '${DEPLOY_USER}:${SERVICE_GROUP}' '${DEPLOY_DIR}'"
scp -P "${SSH_PORT}" "${LOCAL_ARTIFACT}" "${DEPLOY_USER}@${DEPLOY_HOST}:${REMOTE_RELEASE_DIR}/${APP_NAME}"
remote "sudo chown '${SERVICE_USER}:${SERVICE_GROUP}' '${REMOTE_RELEASE_DIR}/${APP_NAME}' && sudo chmod 750 '${REMOTE_RELEASE_DIR}/${APP_NAME}'"

echo "Switching current release..."
remote "sudo ln -sfn '${REMOTE_RELEASE_DIR}' '${DEPLOY_DIR}/current' && sudo systemctl daemon-reload && sudo systemctl restart '${SERVICE_NAME}'"

echo "Waiting for application health..."
for ((attempt = 1; attempt <= HEALTH_RETRIES; attempt++)); do
    if remote "curl --fail --silent --show-error --max-time 5 '${HEALTH_URL}' >/dev/null"; then
        echo "Deployment ${RELEASE_ID} is healthy."
        exit 0
    fi
    sleep 2
done

echo "Health check failed; restoring the previous release." >&2
if [[ -n "${PREVIOUS_RELEASE}" ]]; then
    remote "sudo ln -sfn '${PREVIOUS_RELEASE}' '${DEPLOY_DIR}/current' && sudo systemctl restart '${SERVICE_NAME}'"
else
    remote "sudo systemctl stop '${SERVICE_NAME}' || true"
fi

echo "Recent service logs:" >&2
remote "sudo journalctl -u '${SERVICE_NAME}' -n 40 --no-pager" >&2 || true
exit 1
