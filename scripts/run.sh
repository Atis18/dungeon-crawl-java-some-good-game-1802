#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")/.."

if [[ ! -f .env ]]; then
  echo "Missing .env; copy .env.example to .env and set DB_PASSWORD first." >&2
  exit 1
fi

if ! command -v docker >/dev/null 2>&1; then
  echo "Docker is required for the local PostgreSQL database." >&2
  exit 1
fi

if [[ ! -f ./mvnw ]]; then
  echo "Maven Wrapper (mvnw) is missing. Generate it with the Maven Wrapper plugin in IntelliJ." >&2
  exit 1
fi

docker compose -f compose.db.yaml up -d

echo "Waiting for PostgreSQL to accept connections..."
ready=false
for _ in {1..40}; do
  if docker compose -f compose.db.yaml exec -T db sh -c 'pg_isready -U "$POSTGRES_USER" -d "$POSTGRES_DB"' >/dev/null 2>&1; then
    ready=true
    break
  fi
  sleep 2
done
if [[ "$ready" != true ]]; then
  echo "PostgreSQL did not become ready; check docker compose -f compose.db.yaml logs db" >&2
  exit 1
fi

bash ./mvnw -B -ntp javafx:run
