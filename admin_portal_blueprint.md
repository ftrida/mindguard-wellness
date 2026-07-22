# MindGuard AI Admin Portal Blueprint
## Enterprise Administration, Operational Monitoring, and System Governance Console

This document details the production-ready architecture for the **Enterprise Admin Portal** of MindGuard AI. The portal provides administrators with tools to govern users, monitor AI pipelines, check system health, audit security events, and manage feature rollouts.

---

## 1. Admin Portal Architecture

The Admin Portal operates as a secure, isolated routing group on the Vercel frontend, communicating with dedicated, role-protected admin routers on the FastAPI backend.

```
                   [ Admin Web Browser ]
                             │
                             ▼
              [ Next.js Admin Route Guard ]
              (Restricts access to active admins)
                             │
                             ▼
                 [ FastAPI Admin Endpoints ]
              (Enforces Role-Based Access Controls)
                             │
         ┌───────────────────┼───────────────────┐
         ▼                   ▼                   ▼
┌─────────────────┐ ┌─────────────────┐ ┌─────────────────┐
│ Database Query  │ │ Render API      │ │ Redis Cache     │
│ User Management │ │ Deployments     │ │ Feature Flags   │
└─────────────────┘ └─────────────────┘ └─────────────────┘
```

---

## 2. Folder Structure

### 2.1 Next.js Console Folder Structure (`/src/app/(admin)`)
```
src/app/(admin)/
├── layout.tsx                    # Shared admin header, sidebar, and theme provider
├── dashboard/                    # KPI panels, system alerts, and user traffic charts
├── users/                        # User search, activity details, and deactivation tables
├── ai-monitor/                   # Prompt management, token count, and LLM providers
├── health/                       # CPU logs, database pool usage, and container statuses
├── security/                     # Failed login alerts and IP whitelist tables
└── feature-flags/                # System config values and rollout controls
```

### 2.2 FastAPI Backend Admin Services (`app/api/v1/endpoints/admin.py`)
```
app/
├── api/v1/endpoints/
│   └── admin.py                  # Declares routes for users, audit logs, and configs
├── schemas/
│   └── admin.py                  # Pydantic schemas validating configuration updates
└── services/
    └── admin_service.py          # Coordinates admin operations and audit logging
```

---

## 3. Role-Based Access Design

We implement a strict Role-Based Access Control (RBAC) matrix to restrict administrative capabilities:

```
  Admin Role            Allowed Modules                        Prohibited Actions
  ┌────────────────────┬──────────────────────────────────────┬──────────────────────────────────┐
  │ Support Admin      │ User management, chat histories      │ Cannot delete users or edit logs │
  ├────────────────────┼──────────────────────────────────────┼──────────────────────────────────┤
  │ AI Admin           │ Prompt builder, LLM provider settings│ Cannot view billing or user logs │
  ├────────────────────┼──────────────────────────────────────┼──────────────────────────────────┤
  │ Analytics Manager  │ Dashboard stats, exports             │ Cannot edit security settings    │
  ├────────────────────┼──────────────────────────────────────┼──────────────────────────────────┤
  │ Super Admin        │ All modules                          │ None                             │
  └────────────────────┴──────────────────────────────────────┴──────────────────────────────────┘
```

### Authorization Middleware:
Custom route decorators verify the administrator's role before executing request handlers:

```python
@router.get("/security/logs")
@require_admin_role(["superadmin", "security_admin"])
async def get_security_audit_logs(db: AsyncSession = Depends(get_db)):
    ...
```

---

## 4. Dashboard Layout

The Admin Dashboard provides a real-time overview of system performance and user activity:
*   **KPI Summary Metrics:** Displays total registered accounts, daily active users (DAU), monthly active users (MAU), and user growth trends.
*   **System Health Panel:** Displays API latency averages, server error rates, database connection pool usage, and background job queues.
*   **Engine Status Cards:** Displays active connection statuses for the Digital Twin, Behavior Analysis, Stress Estimation, and AI Coach engines.

---

## 5. User Management Architecture

The User Management module provides tools to govern user accounts:
1.  **Search & Filters:** Enables searching users by email or ID, and filtering by registration date, role, verification status, and account state (active, suspended, deleted).
2.  **Account Controls:** Provides endpoints to deactivate, suspend, reactivate, or delete user accounts.
3.  **Data Export:** Generates compressed zip files containing the user's complete data profile (logs, journals, mood checks) to comply with data privacy regulations (e.g. GDPR).

---

## 6. AI Monitoring Dashboard

Provides visibility into our LLM pipelines and prompt configurations:
*   **Token Metrics:** Monitors input and output token counts to track model API costs.
*   **Provider Latency:** Measures the response time of different LLM providers (Google Gemini, Anthropic Claude, OpenAI).
*   **Conversation Volumes:** Tracks the number of active conversations managed by the AI Coach.

---

## 7. System Health Dashboard

Monitors backend server and database performance:
*   **Resource Utilization:** Tracks CPU and memory usage of the FastAPI containers on Render.
*   **Database Health:** Monitors active connection counts, slow query logs, and transaction commit rates.
*   **Job Queue Status:** Monitors Celery background queues, displaying completed, active, and failed tasks.

---

## 8. Audit Logging Architecture

Every administrative action is audited and written to an immutable log table:

```json
{
  "timestamp": "2026-07-22T14:30:05.102Z",
  "admin_id": 841,
  "action": "user_suspended",
  "target_user_id": 8491,
  "ip_address": "198.51.100.42",
  "reason": "Suspicious login activity detected",
  "changes": {
    "before": { "is_active": true },
    "after": { "is_active": false }
  }
}
```

*   **Immutable Storage:** The database user profile restricts the `audit_logs` table to `INSERT` queries only. Updates or deletions are blocked.

---

## 9. Notification Management

Enables administrators to broadcast notifications across client devices:
*   **Broadcast Alerts:** Enqueues push notifications to be sent to all registered devices.
*   **Targeted Alerts:** Sends notifications to targeted cohorts (e.g. users who have not logged sleep metrics for three consecutive days).
*   **Announcement Banners:** Controls dynamic announcement banners rendered on the Next.js website and Android app home screen.

---

## 10. Feature Flag System

Feature flags enable safe, progressive releases of new components:

```
  ┌────────────────────────────────────────────────────────┐
  │ 1. Feature Definition: Flag defined in admin settings  │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ 2. Target Assignment:                                  │
  │    - globally_enabled = FALSE                          │
  │    - beta_testers_only = TRUE                          │
  │    - rollout_percentage = 10%                          │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ 3. Client Verification:                                │
  │    - Check if User ID matches target group             │
  │    - If valid, render feature in UI                    │
  └────────────────────────────────────────────────────────┘
```

---

## 11. Security Center

The Security Center monitors potential security threats:
*   **Brute-Force Detection:** Flags accounts with multiple failed login attempts.
*   **IP Whitelisting:** Restricts administrative access to whitelisted IP addresses.
*   **Suspicious Activity Logs:** Flags logins originating from unexpected locations or devices.

---

## 12. Reports Architecture

Enables administrators to generate and export system reports:
*   **Report Generation:** Reports are generated asynchronously via Celery background tasks.
*   **Export Formats:** Exports data in PDF, CSV, and Excel formats.
*   **Report Categories:**
    *   *User Growth:* Tracks new registrations and active user trends.
    *   *Usage Reports:* Identifies popular and under-utilized features.
    *   *Security Reports:* Lists audited events and failed login attempts.

---

## 13. Database Integration

Admin operations interact with the following database tables:
*   `admins`: Stores administrative user accounts and roles.
*   `audit_logs`: Stores audited administrative actions.
*   `feature_flags`: Stores feature flag configurations.
*   `system_settings`: Stores global application configurations.

---

## 14. FastAPI Integration

The admin module is exposed via a dedicated API router prefix:
*   `GET /api/v1/admin/dashboard`: Returns the main admin dashboard metrics.
*   `GET /api/v1/admin/users`: Returns a paginated list of user records.
*   `POST /api/v1/admin/flags`: Updates feature flag configurations.

---

## 15. Render Monitoring Strategy

The admin panel integrates with Render's REST API to monitor deployment statuses:
1.  **Deployment Tracking:** Tracks active deployments and build statuses.
2.  **Environment Variables:** Provides tools to view and update application environment variables.
3.  **Logs Streams:** Integrates with Render's logging endpoints to display container logs directly in the admin console.

---

## 16. Scalability Plan

*   **Paginated Queries:** All list endpoints use cursor-based pagination to prevent query latency when loading large datasets.
*   **Pre-Aggregated Metrics:** Metric counters are compiled by daily background tasks, avoiding expensive `COUNT` queries on user log tables.
*   **Redis Caching:** Feature flags and global system settings are cached in Redis to minimize database traffic.

---

## 17. Enterprise Expansion Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: Multi-Tenant Architecture (B2B Accounts)      │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: Subscription & Stripe Billing Integration     │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: Developer API keys & third-party integrations │
  └────────────────────────────────────────────────────────┘
```

*   **Phase 1 (Multi-Tenancy):** Updates the database schema to support organization-level accounts, allowing corporations to manage wellness benefits for employees.
*   **Phase 2 (Billing):** Integrates Stripe to manage B2B subscriptions, seat counts, and payment gateways.
*   **Phase 3 (Integrations):** Implements developer API keys, allowing external applications to query anonymized user statistics.

---

## 18. Development Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: Admin tables migrations & base auth routers   │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: User management tables & controls             │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: System health, logging, & feature flags       │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 4: Report generation & export pipelines          │
  └────────────────────────────────────────────────────────┘
```

1.  **Phase 1 (Foundations):** Configure admin role schemas, run database migrations, and write authentication route guards.
2.  **Phase 2 (User Control):** Build user search interfaces, paginated list endpoints, and suspension controls.
3.  **Phase 3 (Monitoring):** Build system health graphs, integrate feature flags, and implement the administrative audit logger.
4.  **Phase 4 (Reporting):** Configure Celery background tasks for report generation and build export download links.
