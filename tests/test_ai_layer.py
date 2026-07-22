import pytest
from httpx import AsyncClient
from fastapi import status
from datetime import date, datetime, timedelta

async def get_auth_headers(client: AsyncClient, email: str, username: str) -> dict:
    reg_payload = {
        "email": email,
        "username": username,
        "password": "Password123!"
    }
    await client.post("/api/v1/auth/register", json=reg_payload)
    login_payload = {
        "email_or_username": username,
        "password": "Password123!"
    }
    login_res = await client.post("/api/v1/auth/login", json=login_payload)
    token = login_res.json()["access_token"]
    return {"Authorization": f"Bearer {token}"}

@pytest.mark.asyncio
async def test_digital_lifestyle_twin_apis(client: AsyncClient):
    headers = await get_auth_headers(client, "twintest@example.com", "twintest")

    # 1. Get twin baseline (should auto-create)
    response = await client.get("/api/v1/twin", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "wellness_score" in data
    assert data["sleep_baseline"] == 7.5

    # 2. Compare twin with today
    response = await client.get("/api/v1/twin/compare", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "deviations" in data
    assert "current_metrics" in data

    # 3. History
    response = await client.get("/api/v1/twin/history", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) >= 1

    # 4. Regenerate twin
    response = await client.post("/api/v1/twin/regenerate", headers=headers)
    assert response.status_code == status.HTTP_201_CREATED

@pytest.mark.asyncio
async def test_behavior_engine_apis(client: AsyncClient):
    headers = await get_auth_headers(client, "behaviortest@example.com", "behaviortest")

    # 1. Get current drift (should auto-create)
    response = await client.get("/api/v1/behavior/drift", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "drift_score" in data
    assert "consistency_score" in data
    assert "explanation" in data

    # 2. History
    response = await client.get("/api/v1/behavior/history", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) >= 1

@pytest.mark.asyncio
async def test_stress_likelihood_apis(client: AsyncClient):
    headers = await get_auth_headers(client, "stresstest@example.com", "stresstest")

    # 1. Get assessment (should auto-create)
    response = await client.get("/api/v1/stress/assessment", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "stress_score" in data
    assert "confidence_score" in data

    # 2. History
    response = await client.get("/api/v1/stress/history", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) >= 1

@pytest.mark.asyncio
async def test_coach_conversations_and_advice(client: AsyncClient):
    headers = await get_auth_headers(client, "coachtest@example.com", "coachtest")

    # 1. Ask chat question
    payload = {"content": "I am feeling stressed and tired today."}
    response = await client.post("/api/v1/coach/chat", json=payload, headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "reply" in data
    assert len(data["memory"]) == 2 # user message + assistant reply

    # 2. Get history memory
    response = await client.get("/api/v1/coach/conversation", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) == 2

    # 3. Get proactive coaching advice
    response = await client.get("/api/v1/coach/advice", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert "daily_advice" in response.json()

    # 4. Clear conversation memory
    response = await client.delete("/api/v1/coach/conversation", headers=headers)
    assert response.status_code == status.HTTP_204_NO_CONTENT

@pytest.mark.asyncio
async def test_recommendations_workflow(client: AsyncClient):
    headers = await get_auth_headers(client, "rectest@example.com", "rectest")

    # 1. Get active recommendations (should generate default or lifestyle cards)
    response = await client.get("/api/v1/recommendations", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert len(data) >= 1
    rec_id = data[0]["id"]

    # 2. Read recommendation
    response = await client.post(f"/api/v1/recommendations/{rec_id}/read", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["is_read"] is True

    # 3. Complete recommendation
    response = await client.post(f"/api/v1/recommendations/{rec_id}/complete", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["is_completed"] is True

@pytest.mark.asyncio
async def test_report_engine_endpoints(client: AsyncClient):
    headers = await get_auth_headers(client, "reportstest@example.com", "reportstest")

    # 1. Daily Report
    response = await client.get("/api/v1/reports/daily", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["report_type"] == "Daily Wellness Report"

    # 2. Weekly Report
    response = await client.get("/api/v1/reports/weekly", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["report_type"] == "Weekly Analytical Summary"

    # 3. Monthly Report
    response = await client.get("/api/v1/reports/monthly", headers=headers)
    assert response.status_code == status.HTTP_200_OK

    # 4. Export Raw logs
    response = await client.get("/api/v1/reports/export", headers=headers)
    assert response.status_code == status.HTTP_200_OK

@pytest.mark.asyncio
async def test_goals_crud(client: AsyncClient):
    headers = await get_auth_headers(client, "goalstest@example.com", "goalstest")

    # 1. Create a goal
    goal_payload = {
        "title": "Drink more water",
        "category": "water",
        "target_value": 2.5,
        "start_date": str(date.today()),
        "target_date": str(date.today() + timedelta(days=7))
    }
    response = await client.post("/api/v1/goals", json=goal_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    data = response.json()
    assert data["title"] == "Drink more water"
    assert data["status"] == "active"
    goal_id = data["id"]

    # 2. Update progress
    update_payload = {"current_value": 1.5}
    response = await client.put(f"/api/v1/goals/{goal_id}", json=update_payload, headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["current_value"] == 1.5
    assert response.json()["status"] == "active"

    # 3. Auto-complete when target is met
    update_payload2 = {"current_value": 2.5}
    response = await client.put(f"/api/v1/goals/{goal_id}", json=update_payload2, headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["status"] == "completed"

    # 4. Delete goal
    response = await client.delete(f"/api/v1/goals/{goal_id}", headers=headers)
    assert response.status_code == status.HTTP_204_NO_CONTENT

@pytest.mark.asyncio
async def test_achievements_and_streaks(client: AsyncClient):
    headers = await get_auth_headers(client, "achievementstest@example.com", "achievementstest")

    # 1. List achievements (should trigger automatic checks and return unlocked badges)
    response = await client.get("/api/v1/achievements", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    # Should start with empty list or unlocked defaults
    assert isinstance(response.json(), list)

    # 2. Get Streak info
    response = await client.get("/api/v1/achievements/streak", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["current_streak"] == 0

@pytest.mark.asyncio
async def test_scheduler_import():
    # Verify we can import scheduler controls
    from app.core.scheduler import start_scheduler, shutdown_scheduler
    assert callable(start_scheduler)
    assert callable(shutdown_scheduler)
