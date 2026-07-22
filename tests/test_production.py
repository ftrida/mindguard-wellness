import pytest
from httpx import AsyncClient
from fastapi import status
from app.core.rate_limiter import InMemoryRateLimiter
from app.core.ai_provider import AIProviderRegistry, MockAIProvider, GeminiAIProvider

@pytest.mark.asyncio
async def test_health_endpoints(client: AsyncClient):
    # 1. Consolidated Health
    response = await client.get("/health")
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["database"] in ["connected", "disconnected"]

    # 2. Database Health
    response = await client.get("/health/database")
    assert response.status_code == status.HTTP_200_OK
    assert "status" in response.json()

    # 3. System Health
    response = await client.get("/health/system")
    assert response.status_code == status.HTTP_200_OK
    assert "cpu_usage_percent" in response.json()

    # 4. Scheduler Health
    response = await client.get("/health/scheduler")
    assert response.status_code == status.HTTP_200_OK
    assert "scheduler_running" in response.json()

    # 5. AI Health
    response = await client.get("/health/ai")
    assert response.status_code == status.HTTP_200_OK
    assert "configured_provider" in response.json()

    # 6. Version Check
    response = await client.get("/version")
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["version"] == "1.0.0"

@pytest.mark.asyncio
async def test_security_headers_and_payload_limit(client: AsyncClient):
    response = await client.get("/health")
    assert response.status_code == status.HTTP_200_OK
    # Verify presence of generated Request & Correlation IDs
    assert "X-Request-ID" in response.headers
    assert "X-Correlation-ID" in response.headers
    
    # Verify security headers
    assert response.headers["X-Frame-Options"] == "DENY"
    assert response.headers["X-Content-Type-Options"] == "nosniff"

    # Verify request payload size limit (using a mock large request)
    large_headers = {"Content-Length": "20000000"}  # ~20MB
    response_large = await client.post("/api/v1/auth/login", headers=large_headers, json={})
    assert response_large.status_code == status.HTTP_413_REQUEST_ENTITY_TOO_LARGE

def test_rate_limiter_logic():
    limiter = InMemoryRateLimiter(limit=2, window_seconds=2)
    assert limiter.is_allowed("127.0.0.1") is True
    assert limiter.is_allowed("127.0.0.1") is True
    assert limiter.is_allowed("127.0.0.1") is False

def test_ai_provider_registry():
    provider_mock = AIProviderRegistry.get_provider("mock")
    assert isinstance(provider_mock, MockAIProvider)

    provider_gemini = AIProviderRegistry.get_provider("gemini")
    assert isinstance(provider_gemini, GeminiAIProvider)
