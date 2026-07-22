#!/bin/sh
# Exit immediately if a command exits with a non-zero status
set -e

echo "Running Alembic Database Migrations..."
alembic upgrade head

echo "Starting Gunicorn FastAPI Application Server..."
exec gunicorn app.main:app -w 4 -k uvicorn.workers.UvicornWorker -b 0.0.0.0:10000
