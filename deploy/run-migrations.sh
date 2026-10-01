#!/usr/bin/env bash

set -euo pipefail
umask 077

ENV_FILE="${1:?Usage: run-migrations.sh <env-file>}"
BACKUP_DIR="${MIGRATION_BACKUP_DIR:-/opt/s-health/backups}"
MYSQL_CONTAINER="${MYSQL_CONTAINER:-s_health_mysql}"

if [[ ! -f "$ENV_FILE" ]]; then
  echo "Migration environment file does not exist: $ENV_FILE" >&2
  exit 1
fi

read_env() {
  local key="$1"
  grep -m1 "^${key}=" "$ENV_FILE" | cut -d= -f2- | tr -d '\r'
}

DB_NAME="$(read_env DB_NAME)"
DB_ROOT_PASSWORD="$(read_env DB_ROOT_PASSWORD)"

if [[ -z "$DB_NAME" || -z "$DB_ROOT_PASSWORD" ]]; then
  echo "DB_NAME and DB_ROOT_PASSWORD are required for migrations." >&2
  exit 1
fi

mysql_root() {
  docker exec -i -e MYSQL_PWD="$DB_ROOT_PASSWORD" "$MYSQL_CONTAINER" \
    mysql --batch --skip-column-names -u root "$DB_NAME" "$@"
}

if [[ "$(docker inspect -f '{{.State.Health.Status}}' "$MYSQL_CONTAINER")" != "healthy" ]]; then
  echo "MySQL container is not healthy: $MYSQL_CONTAINER" >&2
  exit 1
fi

mkdir -p "$BACKUP_DIR"
chmod 700 "$BACKUP_DIR"

MIGRATION_VERSION="payos-payment-data-v2-free-payment-method"
RELEASE_DIR="$(cd "$(dirname "$ENV_FILE")" && pwd)"
MIGRATION_FILE="$RELEASE_DIR/docs/migrations/payos-payment-data-v2-free-payment-method.sql"

if [[ ! -f "$MIGRATION_FILE" ]]; then
  echo "Migration file does not exist: $MIGRATION_FILE" >&2
  exit 1
fi

MIGRATION_CHECKSUM="$(sha256sum "$MIGRATION_FILE" | awk '{print $1}')"

mysql_root <<'SQL'
CREATE TABLE IF NOT EXISTS s_health_schema_migrations (
  version VARCHAR(128) NOT NULL PRIMARY KEY,
  checksum CHAR(64) NOT NULL,
  applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
SQL

recorded_checksum="$(mysql_root -e "SELECT checksum FROM s_health_schema_migrations WHERE version='${MIGRATION_VERSION}' LIMIT 1;")"

if [[ -n "$recorded_checksum" ]]; then
  if [[ "$recorded_checksum" != "$MIGRATION_CHECKSUM" ]]; then
    echo "Migration checksum changed after it was applied: $MIGRATION_VERSION" >&2
    exit 1
  fi

  echo "Migration already applied: $MIGRATION_VERSION"
  exit 0
fi

schema_before="$(mysql_root -e "SELECT COLUMN_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='payment' AND COLUMN_NAME='method';")"
if [[ -z "$schema_before" ]]; then
  echo "The payment.method column is missing; refusing to run migration." >&2
  exit 1
fi

backup_file="$BACKUP_DIR/pre-${MIGRATION_VERSION}-$(date -u +%Y%m%dT%H%M%SZ).sql.gz"
docker exec -e MYSQL_PWD="$DB_ROOT_PASSWORD" "$MYSQL_CONTAINER" \
  mysqldump --single-transaction --routines --triggers --hex-blob -u root "$DB_NAME" \
  | gzip -c > "$backup_file"
chmod 600 "$backup_file"
gzip -t "$backup_file"
test -s "$backup_file"
echo "Database backup created: $backup_file"

if [[ "$schema_before" == *"'FREE'"* ]]; then
  echo "Schema already contains FREE; recording the existing migration."
else
  echo "Applying migration: $MIGRATION_VERSION"
  docker exec -i -e MYSQL_PWD="$DB_ROOT_PASSWORD" "$MYSQL_CONTAINER" \
    mysql --show-warnings -u root "$DB_NAME" < "$MIGRATION_FILE"
fi

schema_after="$(mysql_root -e "SELECT COLUMN_TYPE FROM information_schema.COLUMNS WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='payment' AND COLUMN_NAME='method';")"
if [[ "$schema_after" != *"'FREE'"* ]]; then
  echo "Migration completed without adding FREE to payment.method; refusing to record it." >&2
  exit 1
fi

mysql_root -e "INSERT INTO s_health_schema_migrations (version, checksum) VALUES ('${MIGRATION_VERSION}', '${MIGRATION_CHECKSUM}');"
echo "Migration recorded: $MIGRATION_VERSION"
