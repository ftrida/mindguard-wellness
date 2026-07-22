# MindGuard AI Enterprise Security Blueprint
## Enterprise Security Platform, Threat Modeling, Data Privacy, and DevSecOps Architecture

This document details the production-ready **Enterprise Security Architecture** for MindGuard AI. Built on the principles of **Defense in Depth** and **Privacy by Design**, this system protects user data across mobile and web clients, API gateways, databases, and AI pipelines.

---

## 1. Security Architecture Layers

The platform uses a layered security model to protect systems and data at all levels:

```
  ┌────────────────────────────────────────────────────────┐
  │ 1. Boundary & Network Layer                            │
  │    - Vercel/Render Edge WAF, HTTPS TLS 1.3, CORS       │
  └────────────────────────┬───────────────────────────────┘
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 2. API Gateway & Authentication Layer                  │
  │    - JWT verification, rate limiting, session guards   │
  └────────────────────────┬───────────────────────────────┘
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 3. Application & AI Logic Layer                        │
  │    - RBAC middlewares, prompt filters, input checks    │
  └────────────────────────┬───────────────────────────────┘
                           ▼
  ┌────────────────────────────────────────────────────────┐
  │ 4. Data Persistence Layer                              │
  │    - SQLAlchemy 2.0 ORM, AES-256 field encryption      │
  └────────────────────────────────────────────────────────┘
```

---

## 2. STRIDE Threat Model

We use the **STRIDE** methodology to evaluate and address security risks across the application:

| STRIDE Category | Threat Description | Remediation Strategy |
| :--- | :--- | :--- |
| **Spoofing Identity** | Unauthorized login attempts, session hijacking | Enforce JWT Refresh Token Rotation (RTR), device-linked sessions, and lockout policies. |
| **Tampering with Data** | Injection attacks (SQLi, XSS, Path Traversal) | Enforce SQLAlchemy parameterized queries, input sanitization, and strict Pydantic schemas. |
| **Repudiation** | Denying administrative actions or security updates | Implement immutable, write-only audit logs to track sensitive database actions. |
| **Information Disclosure** | Leakage of sensitive health metrics or secrets | Encrypt sensitive text fields (journals, messages) at rest using AES-256-GCM. |
| **Denial of Service** | Resource exhaustion, API rate abuse | Deploy IP-based and token-based rate limiting using FastAPI middleware. |
| **Elevation of Privilege**| Horizontal or vertical privilege escalation | Implement strict role-based permission verification on every backend API endpoint. |

---

## 3. Authentication Security

*   **Token Rotation:** Enforces **Refresh Token Rotation (RTR)**. Using a refresh token invalidates it and issues a successor, preventing token reuse.
*   **Session Whitelist:** Active sessions are tracked in the database, enabling users to view connected devices and revoke sessions remotely.
*   **Failed Logins Lockout:** Accounts are suspended for 15 minutes after 5 consecutive failed login attempts within a 10-minute window to protect against brute-force attacks.

---

## 4. Authorization Security

We implement **Role-Based Access Control (RBAC)** on the backend:
1.  **Identity Injection:** Middleware decodes incoming JWT headers, retrieves the associated database user, and injects their context (ID, role, permissions) into the request state.
2.  **Permission Check Hooks:** Scoped decorators verify permissions on every endpoint:
    ```python
    @router.delete("/users/{id}")
    @require_permission("delete:users")
    async def delete_user_account(id: int, db: AsyncSession = Depends(get_db)):
        ...
    ```

---

## 5. API Security

*   **HTTPS Enforced:** All traffic is routed over HTTPS, using TLS 1.3 with a fallback to TLS 1.2. The backend redirects HTTP requests and includes HSTS (`Strict-Transport-Security`) headers.
*   **Input Sanitization Pipeline:** Request interceptors sanitize incoming string parameters, escaping characters to prevent Cross-Site Scripting (XSS) and command injection.
*   **Correlation ID Logging:** Inject a unique `X-Correlation-ID` header into every request, tracing requests across application logs.

---

## 6. Database Security

*   **Parameterized Queries:** The SQLAlchemy 2.0 ORM executes all database queries using parameterized values, preventing SQL Injection (SQLi) vulnerabilities.
*   **Principle of Least Privilege:** Database credentials used by the FastAPI backend are restricted, blocking access to administrative tables and system commands.
*   **Storage Encryption:** Database backups are encrypted at rest using AES-256 keys before being uploaded to secure cloud storage.

---

## 7. AI Pipeline Security

The AI Wellness Coach includes security layers to prevent LLM abuse and protect user data:

```
 [User Input] ──► [Prompt Injection Filter] ──► [Context Injection]
                                                        │
                                                        ▼
 [Output Validation] ◄── [Sensitive Term Filter] ◄── [LLM Execution]
```

1.  **Prompt Injection Mitigation:** Input filters detect and strip prompt injection commands (e.g. *"Ignore previous instructions"*) before sending queries to the LLM.
2.  **Sensitive Context Protection:** User activity logs are summarized and stripped of personally identifiable information (PII) before being injected into prompt contexts.
3.  **Output Verification:** Output filters verify that generated LLM responses do not contain diagnostic clinical terminology.

---

## 8. Privacy Architecture (GDPR Compliance)

MindGuard AI is built on **Privacy by Design** principles, ensuring compliance with data privacy regulations:
*   **Data Minimization:** We collect only the metrics required to establish lifestyle baselines. Optional features (such as emergency contacts) require explicit user consent.
*   **Data Portability (Right to Access):** Users can request an export of their complete profile, generating a secure ZIP file containing their historical logs, mood data, and journals.
*   **Right to be Forgotten (Account Deletion):** Deleting an account triggers a soft delete (`is_deleted = TRUE`) to immediately suspend access, followed by a permanent purge of all associated records from database tables after 30 days.

---

## 9. Encryption Strategy

*   **In Transit:** Enforces TLS 1.3 with secure cipher suites for all network connections.
*   **At Rest:** Sensitive database columns (such as `body_encrypted` in journals and `message_text_encrypted` in chat logs) are encrypted using **AES-256-GCM**.
*   **Key Rotation:** Symmetric keys are managed using external key management systems (KMS) and rotated annually.

---

## 10. Audit Logging Strategy

Audit logs are written to an immutable, insert-only database table to ensure log integrity:
*   **Logged Events:** Tracks user authentication events (logins, logouts, failures), password changes, email verifications, and administrative updates.
*   **Non-Repudiation:** Database triggers reject `UPDATE` or `DELETE` requests on the `audit_logs` table, protecting logs from tampering.

---

## 11. Monitoring Strategy

*   **Real-time Alerts:** Triggered by security events (such as logins from new devices or multiple failed password resets).
*   **IP Blocking:** The system flags and blocks IP addresses that generate excessive unauthorized requests (`HTTP 401/403`) or exceed rate limits.

---

## 12. Compliance Strategy

MindGuard AI aligns with industry-standard security frameworks:
*   **OWASP Top 10:** The application is scanned during the build phase using static application security testing (SAST) tools to detect vulnerabilities (e.g. injection, broken access control).
*   **Privacy Frameworks:** Implements data access controls, consent flows, and encryption standards in compliance with GDPR guidelines.

---

## 13. Render Security Architecture

*   **Private Network Deployment:** The FastAPI backend and MySQL instance run within a Render Private Network, blocking direct database access from the public internet.
*   **Secure Headers:** Web services include secure headers in HTTP responses:
    ```
    X-Frame-Options: DENY
    X-Content-Type-Options: nosniff
    Referrer-Policy: strict-origin-when-cross-origin
    ```

---

## 14. Android Security

*   **Secure Storage:** JWT tokens and session data are stored using **Encrypted DataStore**, which automatically encrypts keys and values using Android KeyStore-backed AES-256 keys.
*   **Certificate Pinning:** The network client includes certificate pinning definitions, restricting API connection trust to the specific SSL certificate fingerprint of our FastAPI server.

---

## 15. Web Security

*   **Secure Cookies:** Session and refresh tokens are stored in secure cookies:
    *   `HttpOnly`: Prevents client-side scripts from reading the cookie.
    *   `Secure`: Ensures the cookie is only transmitted over encrypted (HTTPS) connections.
    *   `SameSite=Strict`: Protects against Cross-Site Request Forgery (CSRF) attacks.
*   **Content Security Policy (CSP):** Enforces strict CSP headers on the web client, blocking unauthorized scripts, stylesheets, and iframe loads.

---

## 16. Backup & Disaster Recovery

### 16.1 Backup Policy
*   **Daily Backups:** Encrypted database dumps are generated daily and stored in secure cloud storage.
*   **Retention:** Backups are retained for 30 days before being deleted.

### 16.2 Disaster Recovery Plan
*   **Database Failover:** If the primary database fails, the application switches traffic to a read replica while the primary instance is restored.
*   **Container Rollback:** If a deployment fails, Render automatically rolls back the container image to the previously active version.

---

## 17. Security Best Practices

*   **Secrets Management:** API keys, database connection strings, and encryption secrets are loaded via environment variables at runtime. Secrets are never hardcoded in source control.
*   **Dependency Scanning:** Build pipelines run automated vulnerability scans (e.g. Snyk or Dependabot) on all dependencies, blocking builds that contain high-severity vulnerabilities.

---

## 18. Future Zero Trust Architecture

To support future enterprise integrations, we design a Zero Trust roadmap:
1.  **Continuous Authentication:** Every request is authenticated and authorized dynamically, removing long-lived session trust.
2.  **Micro-segmentation:** Decouples core engines (Stress, Twin, AI Coach) into separate network zones, ensuring vulnerabilities in one component cannot compromise other systems.
3.  **Identity-Aware Proxy (IAP):** Restricts administrative console access to users authenticated through an Identity-Aware Proxy.

---

## 19. Scalability Strategy

*   **Distributed Rate Limiting:** Enforces rate limits using a Redis cluster, ensuring consistent rate limiting across scaled container instances.
*   **Asynchronous Cryptography:** Large text decryptions (such as rendering historical journal entries) are processed on client devices using local encryption keys where possible, reducing backend CPU load.

---

## 20. Security Development Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: JWT rotation, secrets, and secure cookies     │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: Column AES encryption & Immutable audit logs  │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: Android keystore & Web CSP headers setup      │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 4: WAF configuration, SAST, & Deployment checks  │
  └────────────────────────────────────────────────────────┘
```

1.  **Phase 1 (Foundations):** Configure secrets management, set up JWT refresh token rotation, and write secure cookie middleware.
2.  **Phase 2 (Data Security):** Implement database column-level AES encryption, write audit logging triggers, and verify database access controls.
3.  **Phase 3 (Client Security):** Implement Android Encrypted DataStore configurations, set up certificate pinning, and write CSP header templates.
4.  **Phase 4 (Infrastructure):** Set up edge WAF rules on Vercel and Render, configure dependency scanners in build pipelines, and run deployment validation tests.
