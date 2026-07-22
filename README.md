# MindGuard AI - Enterprise Mental Wellness & Security Platform

MindGuard AI is a production-ready mental wellness tracking and analytics engine. The platform is architected around a secure FastAPI backend, a native Kotlin Android application, and a responsive Next.js 14 Web / Admin Portal.

---

## 1. Directory Structure

```
mindguard/
├── app/                     # FastAPI Backend Application Source
├── android/                 # Kotlin Android Application Source
├── website/                 # Next.js 14 Web & Admin Portal
├── migrations/              # Alembic Database Migrations
└── tests/                   # Backend Integration & Health Test Suites
```

---

## 2. Tech Stack

### FastAPI Backend
*   **Language & Core:** Python 3.13, FastAPI, SQLAlchemy 2.x (async/aiomysql)
*   **Task Automation:** APScheduler (AsyncIOScheduler) running background baseline snapshots
*   **Calculations & AI:** NumPy, Pandas, Scikit-learn ready
*   **Security:** JWT Access/Refresh tokens with bcrypt password hashing

### Android Native Client
*   **Language & Core UI:** Kotlin, Jetpack Compose, Material 3, MVVM
*   **Local Caching:** Room Database, Encrypted Preferences DataStore
*   **Network:** Retrofit + OkHttp with Token Refresh Authenticator loop
*   **Integrations:** Google Maps SDK, Play Services Location

### Web & Admin Portal
*   **Core:** Next.js 14, React 18, TypeScript, TailwindCSS
*   **Visualization:** Chart.js, React-Chartjs-2
*   **State:** Axios API instances + React AuthContext

---

## 3. Run Locally

### Using Docker Compose
1. Ensure Docker is running.
2. Spin up database, backend, and website:
   ```bash
   docker-compose up --build
   ```
3. Access:
   *   FastAPI backend: `http://localhost:10000` (Swagger UI at `/docs`)
   *   Next.js website: `http://localhost:3000`

### Manual Execution

#### Backend Setup
```bash
# Install packages
pip install -r requirements.txt

# Run migrations
$env:DATABASE_URL="sqlite+aiosqlite:///test_mindguard.db"
python -m alembic upgrade head

# Launch local server
uvicorn app.main:app --host 0.0.0.0 --port 10000 --reload
```

#### Next.js Website Setup
```bash
cd website
npm install
npm run dev
```

---

## 4. Production Deployments

### Backend (Render)
*   **Deployment Target:** Web Service (connected to Managed MySQL instance).
*   **Base URL:** `https://mindguard-api-gz19.onrender.com`
*   **Environment Variables:** Define `DATABASE_URL` (MySQL connection string), `JWT_SECRET_KEY`, and `JWT_REFRESH_SECRET_KEY`.

### Website (Vercel)
*   **Framework Preset:** Next.js.
*   **Environment Variables:** Set `NEXT_PUBLIC_API_URL` to `https://mindguard-api-gz19.onrender.com`.

---

## 5. API Reference Summary

*   `GET /health`: Consolidated health check.
*   `GET /health/database`: DB latency stats.
*   `GET /health/system`: RAM, CPU percent, and thread counts.
*   `POST /api/v1/auth/login`: Issue authentication tokens.
*   `GET /api/v1/twin`: Fetch 30-day baseline parameters.
*   `GET /api/v1/behavior/drift`: Habit index tracking metrics.
*   `GET /api/v1/stress/assessment`: Multi-factor stress likelihood score.
*   `POST /api/v1/coach/chat`: Converse with the AI wellness coach.
