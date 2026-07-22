# MindGuard AI Database Design Blueprint
## Relational MySQL 8.0+ Production Schema

This document defines the production-grade, 3rd Normal Form (3NF) relational database schema for MindGuard AI. The architecture supports a single shared MySQL instance, serving both the Next.js Web Frontend and Jetpack Compose Android Client via the FastAPI Backend.

---

## 1. Database Design Philosophy

To sustain 100,000+ active users and millions of monthly logs, we establish these foundational database patterns:
1.  **Single Source of Truth:** A single normalized MySQL database cluster. Clients store data locally only as an offline cache.
2.  **3rd Normal Form (3NF) Normalization:** Minimize data redundancy to prevent update anomalies. Any analytical aggregations are cached in memory or isolated in dedicated snapshot tables.
3.  **Surrogate Keys:** We use `BIGINT UNSIGNED AUTO_INCREMENT` for high-throughput daily logs to ensure rapid sequential inserts. For secure tokens or entity references needing protection against ID-scraping, we utilize `VARCHAR(36)` (UUIDv4) or standard binary UUID types.
4.  **Enforced Auditing & Soft Deletion:** Every user-facing table implements logical soft delete (`is_deleted`) and audit fields (`created_at`, `updated_at`, `created_by`, `updated_by`).
5.  **Strict Temporal Partitioning & Indexing:** Given the time-series nature of lifestyle logging, all indexes prioritize composite keys containing `(user_id, log_date)` or `(user_id, created_at)`.

---

## 2. Complete Database Architecture

The architecture segregates concerns into modular tables:
*   **Transactional Engine:** Handles real-time logs, journaling, messaging, and authentication. Optimized for rapid writes and simple indexed reads.
*   **Analytical & Baseline Engine (Digital Twin):** Captures daily baseline aggregates and weekly/monthly performance snapshots. Pre-aggregated to keep dashboard load times `< 100ms`.
*   **System & Governance Engine:** Captures security logs, administrator actions, audit traces, and feature flags.

---

## 3. Module-Wise Table List

| Module | Table Name | Purpose | Key Type |
| :--- | :--- | :--- | :--- |
| **1. Auth** | `roles` | Defines RBAC system roles | `BIGINT` PK |
| | `permissions` | Defines system privileges | `BIGINT` PK |
| | `role_permissions` | Many-to-many join for RBAC privileges | Composite PK |
| | `users` | Core credentials and account states | `BIGINT` PK |
| | `refresh_tokens` | Secure token rotation storage | `BIGINT` PK / UUID Index |
| | `password_reset_tokens`| Handles password reset flow | `BIGINT` PK |
| | `otp_verifications` | OTP management for auth changes | `BIGINT` PK |
| | `user_sessions` | Active login sessions across devices | `BIGINT` PK |
| | `devices` | Registers device tokens for push alerts | `BIGINT` PK |
| | `login_history` | Historical audit logs of logins | `BIGINT` PK |
| | `security_logs` | Logs sensitive security actions (e.g., password resets) | `BIGINT` PK |
| **2. Profile** | `user_profiles` | User biographical profiles | `BIGINT` PK / User FK |
| | `emergency_contacts` | Trusted contact details for high-stress alerts | `BIGINT` PK |
| | `medical_informations` | Opt-in medical alerts (allergies, conditions) | `BIGINT` PK |
| | `user_preferences` | App configuration preferences | `BIGINT` PK |
| | `privacy_settings` | Consent and sharing settings | `BIGINT` PK |
| | `notification_settings`| Push/Email channel preferences | `BIGINT` PK |
| **3. Lifestyle**| `lifestyle_logs_daily`| Daily aggregate index of user habits | `BIGINT` PK / Composite Index |
| | `sleep_logs` | Granular sleep session logs | `BIGINT` PK |
| | `exercise_logs` | Physical activity logging | `BIGINT` PK |
| | `water_intake_logs` | Periodic hydration events | `BIGINT` PK |
| | `app_usage_logs` | Categorized screen-time statistics | `BIGINT` PK |
| | `custom_lifestyle_metrics`| Custom tracking definitions (e.g., caffeine, steps) | `BIGINT` PK |
| | `custom_lifestyle_logs`| Entries for custom metrics | `BIGINT` PK |
| **4. Mood** | `mood_categories` | Metadata defining emotional states | `BIGINT` PK |
| | `mood_logs` | Emotional check-in instances | `BIGINT` PK |
| **5. Journal** | `journal_entries` | Textual journal entries (encrypted) | `BIGINT` PK |
| | `journal_tags` | Tag vocabulary metadata | `BIGINT` PK |
| | `journal_entry_tags` | Many-to-many join table | Composite PK |
| | `journal_sentiment_results`| Sentiment extraction scores (-1.0 to +1.0) | `BIGINT` PK / Journal FK |
| **6. Meditation**| `meditation_categories`| Catalog of sessions (guided/unguided) | `BIGINT` PK |
| | `meditation_sessions`| Logged user meditation events | `BIGINT` PK |
| **7. Focus** | `task_categories` | Categories for focus sessions | `BIGINT` PK |
| | `focus_sessions` | Time-blocked focus sessions | `BIGINT` PK |
| | `pomodoro_sessions` | Specialized pomodoro tracking values | `BIGINT` PK / Focus FK |
| | `task_completions` | Specific tasks finished during focus block | `BIGINT` PK |
| **8. Twin** | `user_baselines` | Current baseline EWMA averages | `BIGINT` PK / User FK |
| | `user_baselines_weekly`| Historical weekly twin models | `BIGINT` PK |
| | `user_baselines_monthly`| Historical monthly twin models | `BIGINT` PK |
| | `twin_snapshots` | Archived baseline profile snapshots | `BIGINT` PK |
| | `twin_insights` | Textual analytics summaries from the twin | `BIGINT` PK |
| **9. Behavior** | `behavior_analysis_results`| Daily deviation index analysis | `BIGINT` PK |
| | `behavior_deviations`| Individual metric deviation records ($Z$-scores) | `BIGINT` PK |
| | `behavior_alerts` | Triggered habit drift warning notices | `BIGINT` PK |
| **10. Stress** | `stress_estimations`| Multi-factor stress likelihood results | `BIGINT` PK / Composite Index |
| | `stress_factors` | Contributing deviation vectors | `BIGINT` PK |
| **11. AI** | `ai_conversations` | Conversation thread instances | `BIGINT` PK |
| | `ai_messages` | Dynamic message history logs (encrypted) | `BIGINT` PK |
| | `ai_recommendations`| Wellness coach recommendations | `BIGINT` PK |
| | `ai_recommendation_feedbacks`| Feedback loop rating for LLM output | `BIGINT` PK |
| **12. Dashboard**| `dashboard_configs` | Dynamic layout options | `BIGINT` PK |
| | `daily_summaries` | Cached day stats for dashboard rendering | `BIGINT` PK |
| **13. Analytics**| `analytics_reports` | PDF/HTML reporting summaries | `BIGINT` PK |
| | `analytics_metrics` | Graph-optimized time-series buckets | `BIGINT` PK |
| **14. Alerts** | `notifications` | Message queue records | `BIGINT` PK |
| | `notification_templates`| Notification templates | `BIGINT` PK |
| **15. Admin** | `admins` | Administrative access definitions | `BIGINT` PK |
| | `audit_logs` | Changes to critical tables | `BIGINT` PK |
| | `feature_flags` | Dynamic system flags | `BIGINT` PK |
| | `system_settings` | Application setting parameters | `BIGINT` PK |

---

## 4 & 5. Module Schema Definitions

All tables use `BIGINT UNSIGNED AUTO_INCREMENT` for Primary Keys, standard foreign key mapping constraints, and auditing fields. Time fields utilize the `DATETIME` format (saved in UTC).

### Module 1: Authentication

#### `users`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `email`: `VARCHAR(255) UNIQUE` [NOT NULL]
*   `password_hash`: `VARCHAR(255)` [NOT NULL]
*   `role_id`: `BIGINT UNSIGNED` [FK -> `roles.id`]
*   `is_active`: `BOOLEAN` [DEFAULT TRUE]
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

#### `refresh_tokens`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `token_hash`: `VARCHAR(255) UNIQUE` [NOT NULL]
*   `ip_address`: `VARCHAR(45)` [NOT NULL]
*   `user_agent`: `VARCHAR(512)`
*   `expires_at`: `DATETIME` [NOT NULL]
*   `revoked_at`: `DATETIME` [NULL]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

#### `user_sessions`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `device_id`: `VARCHAR(255)`
*   `ip_address`: `VARCHAR(45)`
*   `user_agent`: `VARCHAR(512)`
*   `last_activity_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]
*   `is_revoked`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

---

### Module 2: Profiles

#### `user_profiles`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED UNIQUE` [FK -> `users.id`]
*   `first_name`: `VARCHAR(100)` [NOT NULL]
*   `last_name`: `VARCHAR(100)` [NOT NULL]
*   `birth_date`: `DATE`
*   `timezone`: `VARCHAR(100)` [DEFAULT 'UTC']
*   `avatar_url`: `VARCHAR(512)`
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]

#### `user_preferences`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED UNIQUE` [FK -> `users.id`]
*   `theme`: `VARCHAR(50)` [DEFAULT 'dark']
*   `language`: `VARCHAR(10)` [DEFAULT 'en']
*   `focus_duration_default`: `INT` [DEFAULT 25] (in minutes)
*   `offline_mode_enabled`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

---

### Module 3: Lifestyle Module

#### `lifestyle_logs_daily`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `log_date`: `DATE` [NOT NULL]
*   `total_sleep_minutes`: `INT` [DEFAULT 0]
*   `total_exercise_minutes`: `INT` [DEFAULT 0]
*   `total_screen_time_minutes`: `INT` [DEFAULT 0]
*   `total_unlock_count`: `INT` [DEFAULT 0]
*   `total_water_ml`: `INT` [DEFAULT 0]
*   `total_work_minutes`: `INT` [DEFAULT 0]
*   `total_study_minutes`: `INT` [DEFAULT 0]
*   `is_synchronized`: `BOOLEAN` [DEFAULT TRUE]
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]
*   *Index/Constraint:* Unique composite index on `(user_id, log_date)` to prevent duplicate logs for a single day.

#### `sleep_logs`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `sleep_start_time`: `DATETIME` [NOT NULL]
*   `sleep_end_time`: `DATETIME` [NOT NULL]
*   `quality_rating`: `TINYINT` (1 to 5 scale)
*   `deep_sleep_minutes`: `INT` [DEFAULT 0]
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

---

### Module 4: Mood Module

#### `mood_logs`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `mood_score`: `TINYINT` [NOT NULL] (1 to 5 scale)
*   `logged_at`: `DATETIME` [NOT NULL]
*   `note`: `VARCHAR(255)`
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

---

### Module 5: Journal Module

#### `journal_entries`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id`]
*   `log_date`: `DATE` [NOT NULL]
*   `title_encrypted`: `VARCHAR(512)`
*   `body_encrypted`: `TEXT` [NOT NULL]
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]
*   *Index/Constraint:* Unique composite index on `(user_id, log_date)` ensures a user has one journal entry per day.

#### `journal_sentiment_results`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `journal_entry_id`: `BIGINT UNSIGNED UNIQUE` [FK -> `journal_entries.id` ON DELETE CASCADE]
*   `sentiment_score`: `FLOAT` [NOT NULL] (-1.000 to +1.000 scale)
*   `dominant_emotion`: `VARCHAR(50)` (e.g. 'joy', 'anger', 'sadness')
*   `key_phrases_json`: `JSON` (List of words/topics analyzed)
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

---

### Module 8: Digital Lifestyle Twin (Most Important Module)

The Digital Twin system maintains the rolling averages that represent the user's normal baseline. 

```
                               ┌──────────────────────────┐
                               │       users Table        │
                               └────────────┬─────────────┘
                                            │ 1:1
                                            ▼
                               ┌──────────────────────────┐
                               │  user_baselines Table    │
                               └────────────┬─────────────┘
                                            │ 1:Many
                                            ├──────────────────────┐
                                            ▼                      ▼
                               ┌──────────────────────────┐  ┌───────────┐
                               │ user_baselines_weekly    │  │ Snapshots │
                               └──────────────────────────┘  └───────────┘
```

#### `user_baselines`
This table represents the user's current baseline parameters. It is updated incrementally every night using an Exponentially Weighted Moving Average (EWMA).
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED UNIQUE` [FK -> `users.id` ON DELETE CASCADE]
*   `baseline_days_count`: `INT` [DEFAULT 0] (Tracks how many days of logs are incorporated)
*   `sleep_duration_avg`: `FLOAT` [DEFAULT 480.0] (minutes)
*   `sleep_quality_avg`: `FLOAT` [DEFAULT 3.0] (1-5 score)
*   `screen_time_avg`: `FLOAT` [DEFAULT 240.0] (minutes)
*   `unlocks_avg`: `FLOAT` [DEFAULT 60.0] (count)
*   `exercise_avg`: `FLOAT` [DEFAULT 30.0] (minutes)
*   `mood_avg`: `FLOAT` [DEFAULT 3.5] (1-5 score)
*   `focus_avg`: `FLOAT` [DEFAULT 60.0] (minutes)
*   `meditation_avg`: `FLOAT` [DEFAULT 15.0] (minutes)
*   `water_intake_avg`: `FLOAT` [DEFAULT 2000.0] (ml)
*   `work_hours_avg`: `FLOAT` [DEFAULT 480.0] (minutes)
*   `study_hours_avg`: `FLOAT` [DEFAULT 180.0] (minutes)
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

#### `user_baselines_weekly`
Caches historical twin baselines at the end of each week to track progression over time.
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `start_date`: `DATE` [NOT NULL]
*   `end_date`: `DATE` [NOT NULL]
*   `sleep_duration_avg`: `FLOAT`
*   `screen_time_avg`: `FLOAT`
*   `unlocks_avg`: `FLOAT`
*   `exercise_avg`: `FLOAT`
*   `mood_avg`: `FLOAT`
*   `focus_avg`: `FLOAT`
*   `meditation_avg`: `FLOAT`
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

#### `twin_snapshots`
Enables version control of baselines. Frozen profiles are saved prior to significant changes (e.g., graduation, new job) to capture distinct life phases.
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `snapshot_date`: `DATE` [NOT NULL]
*   `label`: `VARCHAR(100)` (e.g., 'Before Starting Night Shift')
*   `baseline_data_json`: `JSON` [NOT NULL] (Key-value dump of `user_baselines` fields)
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

---

### Module 9: Behavior Analysis

#### `behavior_analysis_results`
Stores the daily deviation metrics evaluated against the user's baseline.
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `analysis_date`: `DATE` [NOT NULL]
*   `overall_deviation_index`: `FLOAT` [NOT NULL] (Composite deviation percentage)
*   `confidence_score`: `FLOAT` [DEFAULT 1.0] (Computed based on completeness of daily logs)
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

#### `behavior_deviations`
Normalizes individual metric deviations to facilitate cross-habit comparisons.
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `analysis_result_id`: `BIGINT UNSIGNED` [FK -> `behavior_analysis_results.id` ON DELETE CASCADE]
*   `metric_key`: `VARCHAR(50)` (e.g. 'sleep_duration', 'unlock_count')
*   `observed_value`: `FLOAT` [NOT NULL]
*   `baseline_value`: `FLOAT` [NOT NULL]
*   `deviation_ratio`: `FLOAT` [NOT NULL] (Observed / Baseline)
*   `z_score`: `FLOAT` (Statistical deviation weight)
*   `classification`: `VARCHAR(50)` (e.g. 'deviant_high', 'deviant_low', 'normal')

---

### Module 10: Stress Estimation

#### `stress_estimations`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `log_date`: `DATE` [NOT NULL]
*   `stress_likelihood`: `FLOAT` [NOT NULL] (Range: 0.0 to 100.0)
*   `risk_level`: `VARCHAR(20)` (e.g., 'low', 'moderate', 'elevated', 'extreme')
*   `confidence_score`: `FLOAT` [NOT NULL] (Quality index of calculation inputs)
*   `explanation_text`: `TEXT` [NOT NULL] (Why the score changed, explaining statistical deviations)
*   `is_deleted`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

---

### Module 11: AI Module

#### `ai_conversations`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `title`: `VARCHAR(150)` [DEFAULT 'New Chat']
*   `is_archived`: `BOOLEAN` [DEFAULT FALSE]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]
*   `updated_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP]

#### `ai_messages`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `conversation_id`: `BIGINT UNSIGNED` [FK -> `ai_conversations.id` ON DELETE CASCADE]
*   `sender`: `VARCHAR(20)` (e.g. 'user', 'assistant')
*   `message_text_encrypted`: `TEXT` [NOT NULL]
*   `token_count`: `INT` [DEFAULT 0]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

#### `ai_recommendations`
*   `id`: `BIGINT UNSIGNED AUTO_INCREMENT` [PK]
*   `user_id`: `BIGINT UNSIGNED` [FK -> `users.id` ON DELETE CASCADE]
*   `category`: `VARCHAR(50)` (e.g., 'sleep', 'exercise', 'focus')
*   `recommendation_text`: `TEXT` [NOT NULL]
*   `stress_estimation_id`: `BIGINT UNSIGNED` [NULL, FK -> `stress_estimations.id`]
*   `is_helpful`: `BOOLEAN` [NULL]
*   `feedback_comment`: `TEXT` [NULL]
*   `created_at`: `DATETIME` [DEFAULT CURRENT_TIMESTAMP]

---

## 6. Schema Relationships

```
                     ┌──────────────────┐
                     │      users       │
                     └────────┬─────────┘
                              │
         ┌────────────┬───────┴───────────────┬────────────────┐
         │ 1:1        │ 1:Many                │ 1:Many         │ 1:Many
         ▼            ▼                       ▼                ▼
   ┌───────────┐┌──────────────┐        ┌──────────┐     ┌───────────┐
   │ profiles  ││lifestyle_logs│        │mood_logs │     │ journals  │
   └───────────┘└──────────────┘        └──────────┘     └─────┬─────┘
                                                               │ 1:1
                                                               ▼
                                                         ┌───────────┐
                                                         │ sentiment │
                                                         └───────────┘
```

*   `users` has a 1-to-1 relationship with `user_profiles`, `user_preferences`, and `user_baselines`.
*   `users` has a 1-to-many relationship with `lifestyle_logs_daily`, `mood_logs`, `journal_entries`, `stress_estimations`, and `ai_conversations`.
*   `journal_entries` has a 1-to-1 relationship with `journal_sentiment_results`.
*   `ai_conversations` has a 1-to-many relationship with `ai_messages`.
*   `behavior_analysis_results` has a 1-to-many relationship with `behavior_deviations`.

---

## 9. Normalization Strategy

The schema is normalized to **3rd Normal Form (3NF)**:
1.  **First Normal Form (1NF):** All columns contain atomic values, and there are no repeating groups. For example, rather than storing app usage statistics as a comma-separated string inside `lifestyle_logs_daily`, we separate it into a dedicated `app_usage_logs` table.
2.  **Second Normal Form (2NF):** The schema meets 1NF criteria and all non-key columns depend entirely on the primary key. Composite primary keys are avoided for core records, relying instead on clean, single primary keys (`id`).
3.  **Third Normal Form (3NF):** The schema meets 2NF criteria and contains no transitive dependencies. Non-key columns do not depend on other non-key columns. For example, we do not store `mood_name` or `mood_color` inside `mood_logs`; we store `mood_category_id` referencing the `mood_categories` table.

---

## 10. Indexing Strategy

To support `100,000+ users` and millions of logs, indexes are applied to search paths, primary keys, and foreign keys.

### 10.1 Primary & Foreign Key Indexes
*   MySQL automatically indexes Primary Keys.
*   We explicitly declare single-column indexes on all Foreign Keys to optimize join queries (e.g. joining `user_id` across logging tables).

### 10.2 Time-Series Composite Indexes
Given the time-series nature of query patterns, we use composite indexes:
*   `lifestyle_logs_daily`: Index on `(user_id, log_date)`
*   `mood_logs`: Index on `(user_id, logged_at DESC)`
*   `journal_entries`: Index on `(user_id, log_date)`
*   `stress_estimations`: Index on `(user_id, log_date)`
*   `ai_messages`: Index on `(conversation_id, created_at ASC)`

---

## 11. Caching Strategy

```
  [Client App] ──> [FastAPI Endpoint] ──(Check Cache)──> [Redis Cache]
                           │                                  │
                           │ Cache Miss                       │ Cache Hit
                           ▼                                  ▼
                    [MySQL Query]                      [Return Payload]
```

1.  **Dashboard Aggregations:** FastAPI caches the calculated state of the home dashboard in a Redis cache for 1 hour. This avoids executing massive `COUNT` and `AVG` queries on every app launch.
2.  **Session & Token Caching:** JWT whitelist status and user session validity are cached in Redis with an expiration of 15 minutes.
3.  **Baseline Parameters:** A user's `user_baselines` metrics are cached in memory for rapid lookup by the Stress and Behavior engines during incoming daily logs.

---

## 12. Audit Strategy

To secure the health platform, we implement an audit layer:
*   **System Event Tracking:** Every insert or edit to critical security tables (`users`, `user_profiles`, `user_preferences`) records details about the modification agent.
*   **Logical Audits:** High-frequency transaction logs do not get physically deleted; instead, `is_deleted` flags are marked as `TRUE`, and the record is bypassed in application queries using database filters.
*   **Administrative Actions Logging:** All updates performed by accounts flagged as `admin` write historical change events to `audit_logs`, tracking the exact timestamp, admin ID, table altered, old values (in JSON format), and new values (in JSON format).

---

## 13. Data Retention & Archiving Strategy

To prevent database bloating over time, we use a tiered data retention plan:

```
  0 - 90 Days            91 - 365 Days          366+ Days
  ┌───────────────┐      ┌───────────────┐      ┌───────────────┐
  │ Active Table  │ ───> │ Cold Archive  │ ───> │ Aggregate &   │
  │ (MySQL Primary)      │ (Compressed)  │      │ Purge Detail  │
  └───────────────┘      └───────────────┘      └───────────────┘
```

1.  **Hot Tier (0-90 Days):** All logs (screen time, unlocks, mood logs, messages) are kept in active database tables for fast UI querying.
2.  **Warm Tier (91-365 Days):** Granular logs (e.g. raw phone unlock timestamps, individual app usage minutes) are migrated to compressed archive tables.
3.  **Cold Tier (366+ Days):** Individual raw log entries are aggregated into monthly baselines and then deleted from the active database. Journal texts are kept indefinitely (or until deleted by the user) to maintain the user's historical diary.

---

## 14. Database Scaling Strategy

1.  **Horizontal Read Scaling:** We deploy a primary-replica architecture on Render. Write queries are routed to the primary instance, while read-only dashboard fetches are directed to the read replica.
2.  **Database Partitioning:** The high-volume logging tables (`lifestyle_logs_daily`, `mood_logs`, `app_usage_logs`) are partitioned by range based on the log date (`log_date`).
3.  **Connection Pooling:** We configure the FastAPI SQLAlchemy engine using `QueuePool` with `pool_size=20` and `max_overflow=10` to reuse connections, avoiding connection overhead at scale.

---

## 15. Entity-Relationship Diagram (Text Representation)

This text-based diagram outlines the foreign key relationships mapping users to lifestyle and analytical modules.

```
+-------------+
|    users    | <------------------------------------+
+-------------+                                      |
       | 1:1                                         | 1:Many
       +-------------------+                         |
       |                   |                         |
       ▼ 1:1               ▼ 1:1                     ▼
+-------------+     +----------------+     +-------------------+
|  profiles   |     | user_baselines |     |  lifestyle_logs   |
+-------------+     +----------------+     +-------------------+
                           |                         |
                           | 1:Many                  | 1:Many
                           ▼                         ▼
                    +----------------+     +-------------------+
                    |baseline_weekly |     |    sleep_logs     |
                    +----------------+     +-------------------+
```

---

## 16. Future Machine Learning Compatibility

The database architecture supports training future ML models without schema adjustments:
1.  **Feature Store Capability:** The `user_baselines` and `behavior_analysis_results` tables act as an ML feature store, providing pre-processed targets for custom behavior prediction models.
2.  **Dynamic Log Storage:** The `custom_lifestyle_metrics` and `custom_lifestyle_logs` tables allow the application to track new lifestyle metrics (e.g., heart rate variations) dynamically, providing immediate training data for ML pipelines.
3.  **NLP Feedback Training:** The `journal_sentiment_results` and `ai_recommendation_feedbacks` tables collect labeled reinforcement training data (RLHF) to optimize future LLM recommendations.

---

## 17. Migration Strategy

1.  **Alembic Integrations:** We use Alembic to manage database migrations, tracking changes inside the database via the `alembic_version` table.
2.  **Non-Blocking Operations:** All migrations on large tables must use non-blocking operations. For example, adding indexes must use MySQL's `ALGORITHM=INPLACE, LOCK=NONE` to prevent table locks.
3.  **Migration Verification:** Every database migration is verified on a staging database populated with synthetic user records before deployment to the production Render instance.

---

## 18. Recommended Folder Structure for SQLAlchemy Models

We implement a modular model layout, grouping tables by functional domains.

```
backend/
└── app/
    └── models/
        ├── __init__.py           # Base metadata initialization
        ├── base.py               # Abstract base model containing id, created_at, etc.
        ├── auth.py               # User, Role, Permission, Token models
        ├── profile.py            # UserProfile, Preference, EmergencyContact models
        ├── lifestyle.py          # DailyLog, SleepLog, ExerciseLog models
        ├── mood.py               # MoodLog, MoodCategory models
        ├── journal.py            # JournalEntry, JournalSentiment models
        ├── twin.py               # UserBaseline, TwinSnapshot models
        ├── analysis.py           # BehaviorAnalysis, BehaviorDeviation models
        └── ai.py                 # Conversation, Message, Recommendation models
```

---

## 19. Database Implementation Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: Base Auth Schema & Migrations                 │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: Profiles, Preferences & Granular Logs         │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: Analytics, twin baselines, & AI modules       │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 4: Staging volume validation & Index Tuning      │
  └────────────────────────────────────────────────────────┘
```

1.  **Phase 1 (Base Schema):** Write the SQLAlchemy base classes and define models for `users`, `roles`, and `permissions`. Generate and run the initial migration script.
2.  **Phase 2 (Logging & Profiles):** Define models for profiles, configurations, and core logging tables. Generate migrations and verify foreign key constraints.
3.  **Phase 3 (Analytics & AI):** Write database models for baseline trackers, AI threads, and stress analyses. Verify cascade delete rules and composite unique constraints.
4.  **Phase 4 (Volume Verification):** Seed the staging database with 100,000 test users and millions of synthetic logs. Run execution plan queries (`EXPLAIN`) to confirm all queries use the intended indexes.

---

## 20. Architectural Suggestions for Pre-Implementation

Before starting the database implementation, consider the following optimizations:
*   **Utilize Native JSON Columns:** Use MySQL’s native `JSON` type for columns like `key_phrases_json` and `baseline_data_json`. This enables querying nested JSON keys directly using the `->` operator.
*   **Enforce Unicode Support:** Use the `utf8mb4` character set and `utf8mb4_unicode_ci` collation on all text tables. This is necessary to support emojis in journals, mood notes, and chat messages.
*   **Decouple Text Fields:** Separate large text blocks (`body_encrypted` and `message_text_encrypted`) from search indices. This keeps index files compact, maximizing the likelihood that they remain cached in RAM.
