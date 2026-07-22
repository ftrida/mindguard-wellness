# MindGuard AI Backend Architecture Blueprint
## Enterprise Python FastAPI Clean Architecture Design

This document details the production-ready backend architecture for MindGuard AI. Built with Python 3.13+, FastAPI, SQLAlchemy 2.0 (asyncio), and Pydantic V2, this architecture applies **Clean Architecture** to separate HTTP routing from business domain rules and database transactions.

---

## 1. Complete Backend Folder Structure

```
mindguard-backend/
├── app/
│   ├── api/
│   │   ├── v1/
│   │   │   ├── endpoints/        # HTTP Route Handlers (Auth, Profile, Logs, AI, Analytics)
│   │   │   └── router.py         # Registers v1 endpoint sub-routers
│   │   ├── deps.py               # Dependency injection helpers (db, security, services)
│   │   └── middleware/           # FastAPI middlewares (CORS, Rate Limiter, Correlation ID)
│   ├── core/
│   │   ├── config.py             # Environment configurations (Pydantic Settings V2)
│   │   ├── exceptions.py         # Custom application exceptions (AppException)
│   │   ├── handlers.py           # Global exception mapper definitions
│   │   ├── logging.py            # Structured logging setup (structlog/JSON logging)
│   │   └── security.py           # Password hashing, JWT encode/decode helpers
│   ├── database/
│   │   ├── base.py               # Declarative Base metadata definition
│   │   └── session.py            # Async engine and session factory definitions
│   ├── models/                   # SQLAlchemy 2.0 Declarative ORM Models
│   │   ├── base.py               # Abstract base model containing auditing columns
│   │   ├── auth.py
│   │   ├── lifestyle.py
│   │   ├── twin.py
│   │   └── ...
│   ├── schemas/                  # Pydantic V2 Validation & Serialization Schemas
│   │   ├── auth.py
│   │   ├── lifestyle.py
│   │   └── ...
│   ├── repositories/             # Data Access Layer (Repository Pattern)
│   │   ├── base.py               # Generic async repository interface
│   │   ├── user.py
│   │   ├── lifestyle.py
│   │   └── ...
│   ├── services/                 # Domain Business Logic Layer (Service Layer)
│   │   ├── auth.py
│   │   ├── lifestyle.py
│   │   └── ...
│   ├── engines/                  # Specialized AI/Analytical modules
│   │   ├── twin.py               # Digital twin learned baseline algorithms
│   │   ├── behavior.py           # Statistical deviation calculations
│   │   ├── stress.py             # Weighted stress estimation calculations
│   │   └── ai_coach.py           # LLM Prompt builders & orchestrators
│   ├── background/               # Background task definitions (FastAPI BackgroundTasks / Celery)
│   │   ├── tasks.py              # Baseline updates, email delivery, system purges
│   │   └── scheduler.py          # Daily cron runner hooks
│   └── main.py                   # FastAPI application constructor and startup hooks
├── migrations/                   # Alembic schema version history
│   ├── versions/
│   ├── env.py
│   └── script.py.mako
├── tests/                        # Automated Pytest Suite
│   ├── conftest.py               # Fixtures (clean DB, mock client, mock AI)
│   ├── api/                      # Integration endpoints validation
│   ├── services/                 # Business logic test validation
│   └── units/                    # Math engines validation
├── alembic.ini
├── Dockerfile
├── render.yaml                   # Infrastructure-as-code specification
├── requirements.txt
└── .env.example
```

---

## 2. Architecture Explanation

The backend utilizes **Clean Architecture** to isolate core business rules from web frameworks and storage implementations.

```
┌────────────────────────────────────────────────────────────────────────┐
│                              CLIENT LAYER                              │
│                    Android Client  /  Web Application                  │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ HTTP Requests
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                              API GATEWAY                               │
│        FastAPI Routers  ──>  Pydantic V2 Request Schemas Validation    │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Validated Schema DTOs
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                             SERVICE LAYER                              │
│    Applies Business Logic  /  Orchestrates AI Engines  /  Transactions │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ Entity Operations
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                           REPOSITORY LAYER                             │
│     Encapsulates SQLAlchemy 2.0 Async Queries  /  Abstracts DB Dialect  │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │ SQL Execution
                                    ▼
┌────────────────────────────────────────────────────────────────────────┐
│                           PERSISTENCE LAYER                            │
│                        MySQL Production Database                       │
└────────────────────────────────────────────────────────────────────────┘
```

### Key Separation Rules:
1.  **FastAPI Routers** are thin. Their only responsibilities are parameter extraction, authorization checking, calling services, and returning structured data. They never access databases or run mathematical calculations.
2.  **Pydantic Schemas** handle validation. They assert that email strings are well-formed, values match boundaries, and response structures strip sensitive properties (e.g. passwords).
3.  **Services** contain business rules. They check state, perform calculation sequences, verify policies, and invoke repositories.
4.  **Repositories** encapsulate database access. If the application migrates from MySQL to PostgreSQL, only the repository implementations change; the service logic remains untouched.
5.  **SQLAlchemy Models** define data tables. They are pure data containers and do not contain business logic.

---

## 3. Folder Responsibilities

*   `app/api/`: Manages API routing and validation middleware.
*   `app/core/`: Application settings, security utilities (password hashing), exceptions, and configuration modules.
*   `app/database/`: Establishes async database connections and session factories.
*   `app/models/`: Declarative representation of database structures.
*   `app/schemas/`: Validates inputs and serializes outputs.
*   `app/repositories/`: Implements async data queries.
*   `app/services/`: Implements core business logic.
*   `app/engines/`: Houses mathematical algorithms and LLM prompt compilers.
*   `app/background/`: Manages background task queues for asynchronous processing.
*   `migrations/`: Tracks database schema changes over time via Alembic scripts.
*   `tests/`: Validates application behavior using Pytest.

---

## 4. Module Responsibilities

*   **Auth Module:** Verifies credentials, generates JWT access and refresh tokens, registers devices, and handles session lifecycles.
*   **Profile Module:** Manages user bios, preferences, emergency contacts, and opt-in medical logs.
*   **Lifestyle Module:** Logs daily aggregates and schedules sub-logs (sleep, exercise, water).
*   **Mood & Journal Modules:** Logs emotional status check-ins and processes encrypted journal entries, routing journals through sentiment extraction.
*   **Focus & Meditation Modules:** Manages Pomodoro sessions, meditation category indexing, and user progression logs.
*   **Digital Lifestyle Twin Engine:** Computes baseline rolling averages using Exponentially Weighted Moving Averages (EWMA) and generates snapshots.
*   **Behavior Analysis Engine:** Calculates habit deviations by measuring observed logs against baseline parameters.
*   **Stress Estimation Engine:** Evaluates daily deviations and journal sentiment to generate a stress likelihood score and natural-language explanation.
*   **AI Coach Module:** Integrates with LLMs to deliver safe wellness feedback, using prompt builders to format context.

---

## 5. Service Layer Design

Services are classes that encapsulate related business operations. They are instantiated with the required repositories via Dependency Injection, keeping them decoupled from framework implementations.

### Transaction Management (Unit of Work)
Database transactions are managed at the Service Layer. The service uses an async session context to ensure that multiple repository calls succeed or fail together.

```python
class JournalService:
    def __init__(self, journal_repo: JournalRepository, sentiment_engine: SentimentEngine):
        self.journal_repo = journal_repo
        self.sentiment_engine = sentiment_engine

    async def create_daily_entry(
        self, db: AsyncSession, user_id: int, schema: JournalCreateSchema
    ) -> JournalEntry:
        async with db.begin():  # Starts database transaction boundary
            # 1. Check for existing entry
            existing = await self.journal_repo.get_by_date(db, user_id, schema.log_date)
            if existing:
                raise DuplicateRecordException("Journal already exists for this date.")
            
            # 2. Encrypt journal body (omitted for brevity)
            encrypted_body = encrypt_text(schema.body)
            
            # 3. Create entry
            entry = await self.journal_repo.create(
                db, user_id=user_id, log_date=schema.log_date, body=encrypted_body
            )
            
            # 4. Extract sentiment
            sentiment = await self.sentiment_engine.analyze(schema.body)
            await self.journal_repo.save_sentiment(db, entry.id, sentiment)
            
            return entry  # Transaction commits automatically at block exit
```

---

## 6. Repository Layer Design

Repositories encapsulate database access, abstracting raw SQL queries into a clean interface. We use a base generic class to handle standard CRUD operations.

```python
class BaseRepository(Generic[ModelType]):
    def __init__(self, model: Type[ModelType]):
        self.model = model

    async def get(self, db: AsyncSession, id: int) -> Optional[ModelType]:
        result = await db.execute(select(self.model).where(self.model.id == id, self.model.is_deleted == False))
        return result.scalars().first()

    async def create(self, db: AsyncSession, obj_in: Dict[str, Any]) -> ModelType:
        obj = self.model(**obj_in)
        db.add(obj)
        await db.flush()  # Populates id but defers transaction commit to Service Layer
        return obj
```

Specialized repositories inherit from this base class to implement specific query patterns (e.g. range queries, user lookups).

---

## 7. Dependency Injection Strategy

We leverage FastAPI’s native dependency injection system (`Depends`) to manage the lifecycle of database sessions and service instances.

```
       [HTTP Client Request]
                 │
                 ▼
     [FastAPI Endpoint Router]
                 │
                 ├─► Depends(get_current_user)
                 ├─► Depends(get_async_session)
                 └─► Depends(get_lifestyle_service)
                               │
                               ▼
            [Service & Repository Instantiation]
```

### Injection Sequence:
1.  `get_async_session` yields a scoped database session, closing it once the request completes.
2.  `get_current_user` extracts and decodes the JWT access token from the request header, verifying active status and injecting the current user object.
3.  `get_lifestyle_service` instantiates the repository and service classes, injecting the database session.

---

## 8. Configuration Strategy

We manage environment configurations using Pydantic Settings V2, validating environment variables at application startup.

```python
class BaseSettings(BaseSettings):
    PROJECT_NAME: str = "MindGuard AI"
    API_V1_STR: str = "/api/v1"
    DATABASE_URL: str
    JWT_SECRET_KEY: str
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 15
    REFRESH_TOKEN_EXPIRE_DAYS: int = 7
    LLM_API_KEY: str

    model_config = SettingsConfigDict(env_file=".env", case_sensitive=True)

class DevelopmentSettings(BaseSettings):
    DEBUG: bool = True

class ProductionSettings(BaseSettings):
    DEBUG: bool = False
    # Enforces production security policies (e.g. secure cookies)
```

The appropriate configuration is instantiated at runtime based on the value of the `APP_ENV` environment variable.

---

## 9. Security Strategy

*   **Authentication Flow:** Standard OAuth2 password flow. Access tokens use short lifetimes (15 minutes) to mitigate key exposure risks, while refresh tokens (7-day lifetime) are used to obtain new access tokens.
*   **Password Hashing:** Passwords are salted and hashed using `bcrypt` (12 work factors) before database insertion. Cleartext passwords are never stored.
*   **Encryption at Rest:** Sensitive text fields (such as journal entry bodies and messages) are encrypted using symmetric AES-256-GCM before database insertion. The encryption key is sourced from environment variables.
*   **Rate Limiting:** IP-based rate limiting is applied to authentication endpoints (e.g., maximum of 5 login attempts per minute per IP address) to prevent brute-force attacks.
*   **CORS Configuration:** CORS origins are restricted to validated domains (Next.js website domain and localhost for development), blocking cross-origin requests.

---

## 10. API Versioning Strategy

All API endpoints are versioned to support seamless clients updates.
*   **URL Prefix:** `/api/v1/`
*   **Router Organization:**
    *   `app/api/v1/endpoints/auth.py` -> `/api/v1/auth/login`
    *   `app/api/v1/endpoints/lifestyle.py` -> `/api/v1/lifestyle/log`
    *   `app/api/v1/endpoints/coach.py` -> `/api/v1/coach/chat`

This directory structure allows us to deploy `/api/v2/` endpoints side-by-side with `/api/v1/` during major updates, preventing breaking changes on active client devices.

---

## 11. Logging Strategy

Structured logging is implemented using `structlog`, outputting logs in JSON format for easy ingestion by log managers.

```json
{"timestamp": "2026-07-22T14:20:10.512Z", "level": "info", "event": "user_logged_in", "user_id": 841, "ip_address": "198.51.100.42", "correlation_id": "8a4f9b1c-3d7e-4f6a-8b2c-9d0e1f2a3b4c"}
```

*   **Correlation ID Middleware:** Inject a unique UUID into the request header. This ID is passed to all log outputs generated during the request lifecycle, simplifying thread tracing.
*   **Database Query Logging:** Slow queries (execution times exceeding 500ms) are logged with warning-level severity.

---

## 12. Testing Strategy

We implement automated testing using Pytest, asserting code correctness before deployment.

```
tests/
├── conftest.py               # Handles mock DB creation and client fixtures
├── api/
│   ├── test_auth.py          # Validates sign-in and token endpoints
│   └── test_lifestyle.py     # Validates log submission schema compliance
├── services/
│   └── test_twin_service.py  # Validates baseline update transactions
└── units/
    └── test_twin_math.py     # Validates EWMA calculations against test vectors
```

*   **Database Isolation:** Tests run against a dedicated test database schema. Each test execution is wrapped in a transaction that is rolled back upon completion, ensuring a clean slate for subsequent tests.
*   **External Service Mocking:** LLM completions and external notification handlers are mocked during test execution to prevent rate limit issues and network dependencies.

---

## 13. Deployment Strategy

The application is deployed on Render as a containerized web service.

*   **Containerization:** A multi-stage Docker build is used to build dependencies and run the application, reducing the final image size.
*   **Database Migrations:** Alembic migrations run automatically during Render's build and release phase before the web service goes live.
*   **Horizontal Scaling:** Multiple instances are deployed behind Render's load balancer. In-memory data states are avoided, ensuring stateless instances can scale horizontally.

---

## 14. Render Deployment Configuration Files

### `Dockerfile`
```dockerfile
# Stage 1: Build dependencies
FROM python:3.13-slim AS builder

WORKDIR /build
RUN apt-get update && apt-get install -y --no-install-recommends build-essential libmariadb-dev pkg-config
COPY requirements.txt .
RUN pip install --no-cache-dir --user -r requirements.txt

# Stage 2: Final runtime container
FROM python:3.13-slim AS runner

WORKDIR /app
RUN apt-get update && apt-get install -y --no-install-recommends libmariadb3 && rm -rf /var/lib/apt/lists/*
COPY --from=builder /root/.local /root/.local
COPY . .

ENV PATH=/root/.local/bin:$PATH
EXPOSE 10000

# Start command
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "10000"]
```

### `render.yaml`
```yaml
services:
  - type: web
    name: mindguard-api
    runtime: docker
    plan: standard
    envVars:
      - key: APP_ENV
        value: production
      - key: DATABASE_URL
        sync: false  # Loaded securely via Render Dashboard
      - key: JWT_SECRET_KEY
        sync: false
      - key: LLM_API_KEY
        sync: false
    buildCommand: docker build -t app .
    startCommand: alembic upgrade head && uvicorn app.main:app --host 0.0.0.0 --port 10000
    healthCheckPath: /api/v1/health
```

### `requirements.txt`
```
fastapi>=0.110.0
uvicorn[standard]>=0.28.0
sqlalchemy[asyncio]>=2.0.28
aiomysql>=0.2.0
alembic>=1.13.1
pydantic[email]>=2.6.4
pydantic-settings>=2.2.1
python-jose[cryptography]>=3.3.0
passlib[bcrypt]>=1.7.4
bcrypt>=4.1.2
structlog>=24.1.0
httpx>=0.27.0
pytest>=8.1.1
pytest-asyncio>=0.23.5
```

---

## 15. Backend Development Roadmap

```
   ┌────────────────────────────────────────────────────────┐
   │ Phase 1: Environment Setup & Auth Foundations          │
   └───────────────────────────┬────────────────────────────┘
                               │
                               ▼
   ┌────────────────────────────────────────────────────────┐
   │ Phase 2: Logging and Synchronization APIs              │
   └───────────────────────────┬────────────────────────────┘
                               │
                               ▼
   ┌────────────────────────────────────────────────────────┐
   │ Phase 3: Twin, Deviation and Stress Engines            │
   └───────────────────────────┬────────────────────────────┘
                               │
                               ▼
   ┌────────────────────────────────────────────────────────┐
   │ Phase 4: AI Coach, Chat Module and Admin Dashboard     │
   └───────────────────────────┬────────────────────────────┘
                               │
                               ▼
   ┌────────────────────────────────────────────────────────┐
   │ Phase 5: Testing, Security Auditing and Deployment     │
   └────────────────────────────────────────────────────────┘
```

*   **Phase 1 (Setup & Auth):** Define the base SQLAlchemy models, configure Alembic migrations, and implement JWT authentication and user registration endpoints.
*   **Phase 2 (Logging & Sync APIs):** Create CRUD routers for daily metrics, sleep logs, and encrypted journal entries. Implement bulk sync routes with conflict resolution policies.
*   **Phase 3 (Calculation Engines):** Build the math engines for baseline calculations (EWMA), daily behavior deviation tracking ($Z$-scoring), and stress estimation scoring.
*   **Phase 4 (AI Coach & Admin):** Implement the AI Coach completion routers, prompt templates, feedback tracking schemas, and admin reports.
*   **Phase 5 (Testing & Deploy):** Perform integration testing using Pytest, execute load tests on database connections, configure production environment variables on Render, and deploy to staging.
