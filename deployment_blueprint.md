# MindGuard AI Deployment Architecture Blueprint
## Enterprise Production Infrastructure-as-Code, Containerization, and Continuous Deployment Design

This document details the production-ready **Deployment Architecture** for MindGuard AI. It describes the Infrastructure-as-Code (IaC) templates, Docker container configurations, environment variables, health checks, SRE failover configurations, and continuous delivery (CD) workflows required to deploy the FastAPI backend, MySQL database, and Redis cache on Render, and the Next.js frontend on Vercel.

---

## 1. Complete Production Deployment Architecture

The platform runs as a distributed system, using a private network to isolate datastores from the public internet.

```
 [Users: Next.js Web App]          [Users: Android App]          [Admin Console Interface]
            │                                │                              │
            ├────────────────────────────────┴──────────────────────────────┤
            │ HTTPS TLS 1.3 Requests                                        │
            ▼                                                               ▼
 ┌──────────────────────────────────────────────────────────────────────────────┐
 │                              Vercel / Edge WAF                               │
 └──────────────────────────────────────┬───────────────────────────────────────┘
                                        │ Load Balanced HTTP Traffic
                                        ▼
 ┌──────────────────────────────────────────────────────────────────────────────┐
 │                      Render Cloud Platform Load Balancer                     │
 └──────────────────────────────────────┬───────────────────────────────────────┘
                                        │ Scoped Routing
                                        ▼
 ┌──────────────────────────────────────────────────────────────────────────────┐
 │                  FastAPI Application Server Clusters (Docker)                │
 │                  - Horizontal Pods: mindguard-web-service                    │
 └───────────────────┬───────────────────┬───────────────────┬──────────────────┘
                     │                   │                   │
  Private Network    │                   │                   │
  (Blocked Public)   ▼                   ▼                   ▼
            ┌────────┴────────┐ ┌────────┴────────┐ ┌────────┴────────┐
            │    MySQL DB     │ │   Redis Cache   │ │   AI / Email    │
            │  (mindguard-db) │ │(mindguard-redis)│ │  (Outbound WAN) │
            └─────────────────┘ └─────────────────┘ └─────────────────┘
```

---

## 2. Render Infrastructure-as-Code (`render.yaml`)

We use Render's Infrastructure-as-Code (IaC) format to define and manage our cloud services.

```yaml
services:
  # 1. Primary FastAPI Web Service
  - type: web
    name: mindguard-api
    env: docker
    plan: standard
    region: oregon
    buildCommand: docker build -t mindguard-api .
    startCommand: alembic upgrade head && gunicorn app.main:app -w 4 -k uvicorn.workers.UvicornWorker -b 0.0.0.0:10000
    healthCheckPath: /api/v1/health/ready
    numInstances: 2 # Horizontal Scaling enabled by default
    disk:
      name: mindguard-uploads
      mountPath: /app/uploads
      sizeGB: 10
    envVars:
      - key: APP_ENV
        value: production
      - key: DATABASE_URL
        fromDatabase:
          name: mindguard-db
          property: connectionString
      - key: REDIS_URL
        fromService:
          name: mindguard-redis
          type: redis
          property: connectionString
      - key: JWT_SECRET_KEY
        sync: false # Loaded securely via Render Dashboard
      - key: JWT_REFRESH_SECRET_KEY
        sync: false
      - key: LLM_API_KEY
        sync: false
      - key: SMTP_PASSWORD
        sync: false
      - key: CORS_ORIGINS
        value: '["https://mindguard.vercel.app"]'

  # 2. Redis Cache Service
  - type: redis
    name: mindguard-redis
    plan: standard
    ipAllowList: [] # Access restricted to services inside the private network

databases:
  # 3. Primary Managed MySQL Database
  - name: mindguard-db
    databaseName: mindguard
    user: dbadmin
    plan: standard
    region: oregon
    ipAllowList: [] # Access restricted to services inside the private network
```

---

## 3. Docker Production Architecture

### 3.1 Multi-Stage `Dockerfile`
Our multi-stage Docker build separates dependency compilation from the runtime environment, reducing the final image size and minimizing security vulnerabilities.

```dockerfile
# Stage 1: Compile dependencies
FROM python:3.13-slim AS builder

WORKDIR /build

RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    libmariadb-dev \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir --user -r requirements.txt

# Stage 2: Final runtime container
FROM python:3.13-slim AS runner

WORKDIR /app

RUN apt-get update && apt-get install -y --no-install-recommends \
    libmariadb3 \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy installed packages from builder
COPY --from=builder /root/.local /root/.local
COPY . .

ENV PATH=/root/.local/bin:$PATH
ENV PYTHONUNBUFFERED=1
EXPOSE 10000

# Health check instructions for the container engine
HEALTHCHECK --interval=30s --timeout=5s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:10000/api/v1/health/live || exit 1

CMD ["gunicorn", "app.main:app", "-w", "4", "-k", "uvicorn.workers.UvicornWorker", "-b", "0.0.0.0:10000"]
```

---

## 4. Production Folder Structure

This layout groups deployment configurations at the root level of the project:

```
mindguard-backend/
├── app/                          # Application source code
│   ├── main.py
│   └── ...
├── scripts/                      # Deployment utility scripts
│   ├── build.sh                  # Custom compilation script
│   ├── db_backup.sh              # Database backup automation script
│   └── run_tests.sh              # CI pipeline test trigger script
├── migrations/                   # Alembic database migration files
│   ├── env.py
│   └── versions/
├── alembic.ini
├── Dockerfile
├── render.yaml                   # Infrastructure-as-code template
├── requirements.txt
└── .env.example
```

---

## 5. Telemetry & SRE Monitoring Strategy

*   **Render Metrics:** Tracks container performance, sending alerts to Slack or email if CPU or memory usage exceeds 90% for more than 5 minutes.
*   **Error Logging:** Integrates with error-tracking services (such as Sentry) to capture unhandled exceptions, database connection errors, and AI API failures.
*   **Alerting Rules:** Generates alerts on key metrics:
    *   *Service Latency:* Alert if 95th percentile response times exceed 500ms over a 5-minute window.
    *   *Error Rate:* Alert if server errors (`HTTP 5xx`) exceed 1% of total requests over a 5-minute window.

---

## 6. SRE Health Check APIs

The FastAPI backend exposes dedicated health check endpoints:
*   `GET /api/v1/health/live` (Liveness Probe): Returns `HTTP 200 OK` to verify that the FastAPI container is running.
*   `GET /api/v1/health/ready` (Readiness Probe): Verifies active connections to the MySQL database and Redis cache:
    ```json
    {
      "status": "ready",
      "timestamp": "2026-07-22T14:32:05.102Z",
      "services": {
        "database": "connected",
        "redis": "connected",
        "llm_provider": "available"
      }
    }
    ```

---

## 7. Database Migration & Rollback Strategy

1.  **Automated Migrations:** Running migrations is integrated into the Render startup command (`alembic upgrade head`). This ensures database schemas are updated before the new container version is brought online.
2.  **Schema Verification:** Migration scripts are verified against a staging database before being run on the production database.
3.  **Rollback Plan:** If a migration fails during deployment, the release is aborted, the active container versions are preserved, and administrators can run the rollback command:
    `alembic downgrade -1`

---

## 8. SRE Disaster Recovery Protocols

*   **Database Failures:** If the primary database fails, the backend automatically routes read queries to a read replica, ensuring the application remains functional while the primary database is restored.
*   **Container Crash Loops:** If the container fails to start, Render keeps the previously active container version online to prevent service downtime.
*   **AI API Interruptions:** If our primary LLM provider is unavailable, the AI Coach falls back to secondary providers (e.g. falling back from Gemini to Claude) or returns a cached response.

---

## 9. Production Readiness Checklist

Before moving to the implementation phase, verify the following configuration points:
*   [ ] The Render MySQL database is configured with automated daily backups.
*   [ ] Database and Redis connections are restricted to Render's Private Network.
*   [ ] Encryption keys and credentials are saved in Render environment variables.
*   [ ] API CORS policies are configured to only allow connections from the production Next.js frontend URL.
*   [ ] API health endpoints (`/health/live` and `/health/ready`) are verified.
*   [ ] Target resource alerts for CPU and memory usage are configured.

---

## 10. Deployment Roadmap

```
  Phase 1: Local Docker Compose   Phase 2: DB & Private Network   Phase 3: Render CD Configs
  - Build local image             - Provision MySQL & Redis       - Bind GitHub repo
  - Run Pytest suite              - Configure private network     - Deploy staging environment
        │                               │                               │
        ▼                               ▼                               ▼
  [Local Verification] ──────────► [Resource Provisioning] ────────► [Continuous Delivery]
```

1.  **Phase 1 (Local Verification):** Verify the multi-stage Docker build locally, run automated tests inside the container, and verify database migrations.
2.  **Phase 2 (Resource Provisioning):** Provision the MySQL database and Redis cache instances on Render, and configure the private network.
3.  **Phase 3 (Continuous Delivery):** Connect the GitHub repository to Render, configure environment variables, set up build hooks to deploy on push, and perform staging validation runs.
