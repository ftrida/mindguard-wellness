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
async def test_user_profile_crud_and_validation(client: AsyncClient):
    headers = await get_auth_headers(client, "profiletest@example.com", "profiletest")

    # 1. Get profile (should auto-create empty one)
    response = await client.get("/api/v1/profile", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert data["timezone"] == "UTC"
    assert data["language"] == "en"

    # 2. Update profile (valid details)
    update_payload = {
        "dob": "1995-05-15",
        "gender": "Female",
        "height": 168.5,
        "weight": 58.2,
        "country": "Germany",
        "occupation": "Software Engineer"
    }
    response = await client.put("/api/v1/profile", json=update_payload, headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert data["dob"] == "1995-05-15"
    assert data["gender"] == "Female"
    assert data["height"] == 168.5
    assert data["weight"] == 58.2

    # 3. Update profile with invalid DOB (future)
    invalid_payload = {
        "dob": str(date.today() + timedelta(days=1))
    }
    response = await client.put("/api/v1/profile", json=invalid_payload, headers=headers)
    assert response.status_code == status.HTTP_422_UNPROCESSABLE_ENTITY

    # 4. Update profile with invalid height/weight
    invalid_payload = {
        "height": -5.0
    }
    response = await client.put("/api/v1/profile", json=invalid_payload, headers=headers)
    assert response.status_code == status.HTTP_422_UNPROCESSABLE_ENTITY

@pytest.mark.asyncio
async def test_emergency_contacts_crud_and_validation(client: AsyncClient):
    headers = await get_auth_headers(client, "contacttest@example.com", "contacttest")

    # 1. Create primary contact
    contact_payload = {
        "name": "Jane Doe",
        "relationship": "Sister",
        "phone": "+1234567890",
        "is_primary": True
    }
    response = await client.post("/api/v1/profile/emergency-contacts", json=contact_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    data = response.json()
    assert data["name"] == "Jane Doe"
    assert data["is_primary"] is True
    contact_id = data["id"]

    # 2. Get contacts list
    response = await client.get("/api/v1/profile/emergency-contacts", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) == 1

    # 3. Create contact with invalid phone format
    invalid_payload = {
        "name": "John Doe",
        "relationship": "Brother",
        "phone": "invalid-phone-abc"
    }
    response = await client.post("/api/v1/profile/emergency-contacts", json=invalid_payload, headers=headers)
    assert response.status_code == status.HTTP_422_UNPROCESSABLE_ENTITY

    # 4. Update primary status
    update_payload = {
        "is_primary": False
    }
    response = await client.put(f"/api/v1/profile/emergency-contacts/{contact_id}", json=update_payload, headers=headers)
    assert response.status_code == status.HTTP_200_OK

    # 5. Delete contact
    response = await client.delete(f"/api/v1/profile/emergency-contacts/{contact_id}", headers=headers)
    assert response.status_code == status.HTTP_204_NO_CONTENT

@pytest.mark.asyncio
async def test_daily_lifestyle_tracker(client: AsyncClient):
    headers = await get_auth_headers(client, "lifetest@example.com", "lifetest")

    # 1. Create daily log
    log_date = str(date.today())
    log_payload = {
        "log_date": log_date,
        "sleep_hours": 7.5,
        "water_intake": 2.2,
        "exercise_minutes": 45,
        "walking_steps": 8500,
        "screen_time": 4.0,
        "phone_unlock_count": 42,
        "work_hours": 8.0,
        "social_time": 2.0,
        "outdoor_time": 1.0,
        "energy_level": 8
    }
    response = await client.post("/api/v1/lifestyle", json=log_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    data = response.json()
    assert data["sleep_hours"] == 7.5
    assert data["water_intake"] == 2.2

    # 2. Get lifestyle log by date
    response = await client.get(f"/api/v1/lifestyle/{log_date}", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["energy_level"] == 8

    # 3. Create daily log exceeding 24 hours sum (sleep + screen + study + work + social + outdoor)
    invalid_log = {
        "log_date": str(date.today() - timedelta(days=1)),
        "sleep_hours": 10.0,
        "screen_time": 8.0,
        "work_hours": 10.0 # 10+8+10 = 28 hours (exceeds 24)
    }
    response = await client.post("/api/v1/lifestyle", json=invalid_log, headers=headers)
    assert response.status_code == status.HTTP_422_UNPROCESSABLE_ENTITY

@pytest.mark.asyncio
async def test_mood_tracker_and_stats(client: AsyncClient):
    headers = await get_auth_headers(client, "moodtest@example.com", "moodtest")

    # 1. Create mood entries
    entry_payload1 = {
        "mood_score": 8,
        "notes": "Feeling focused and productive",
        "category": "Energetic"
    }
    entry_payload2 = {
        "mood_score": 6,
        "notes": "A bit tired in the evening",
        "category": "Tired"
    }
    res1 = await client.post("/api/v1/mood", json=entry_payload1, headers=headers)
    assert res1.status_code == status.HTTP_201_CREATED
    await client.post("/api/v1/mood", json=entry_payload2, headers=headers)

    # 2. Get mood entries list
    response = await client.get("/api/v1/mood", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) == 2

    # 3. Get stats
    response = await client.get("/api/v1/mood/stats", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    stats = response.json()
    assert stats["average_score"] == 7.0
    assert stats["entries_count"] == 2
    assert stats["category_distribution"]["Energetic"] == 1
    assert stats["category_distribution"]["Tired"] == 1

@pytest.mark.asyncio
async def test_journal_entries_and_tag_indexing(client: AsyncClient):
    headers = await get_auth_headers(client, "journaltest@example.com", "journaltest")

    # 1. Create journal entry
    journal_payload = {
        "title": "Reflecting on my habits",
        "content": "<p>Today I meditated for 10 minutes and slept well.</p>",
        "category": "Reflection",
        "tags": ["MEDITATION", "sleep ", "meditation"] # duplicates and whitespaces
    }
    response = await client.post("/api/v1/journals", json=journal_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    data = response.json()
    assert data["title"] == "Reflecting on my habits"
    assert data["category"] == "Reflection"
    # Normalization check: tags should be lowercased, unique, and stripped
    tag_names = [t["name"] for t in data["tags"]]
    assert len(tag_names) == 2
    assert "meditation" in tag_names
    assert "sleep" in tag_names

    # 2. Search journal entries
    response = await client.get("/api/v1/journals/search?q=habits", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) == 1

    response = await client.get("/api/v1/journals/search?q=nonexistent", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert len(response.json()) == 0

@pytest.mark.asyncio
async def test_meditation_and_focus_sessions(client: AsyncClient):
    headers = await get_auth_headers(client, "sessionstest@example.com", "sessionstest")

    # 1. Log Meditation session
    med_payload = {
        "category": "Mindfulness",
        "duration_seconds": 600
    }
    response = await client.post("/api/v1/meditations", json=med_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    assert response.json()["duration_seconds"] == 600

    # 2. Log Focus session
    focus_payload = {
        "custom_duration_seconds": 1500,
        "break_duration_seconds": 300,
        "completed_sessions_count": 2
    }
    response = await client.post("/api/v1/focus", json=focus_payload, headers=headers)
    assert response.status_code == status.HTTP_201_CREATED
    assert response.json()["completed_sessions_count"] == 2

@pytest.mark.asyncio
async def test_notifications_and_preferences(client: AsyncClient):
    headers = await get_auth_headers(client, "notiftest@example.com", "notiftest")

    # 1. Get default preferences
    response = await client.get("/api/v1/notifications/preferences", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["email_enabled"] is True

    # 2. Update preferences
    pref_payload = {
        "email_enabled": False,
        "quiet_hours_start": "22:00:00",
        "quiet_hours_end": "07:00:00"
    }
    response = await client.put("/api/v1/notifications/preferences", json=pref_payload, headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["email_enabled"] is False

@pytest.mark.asyncio
async def test_dashboard_and_analytics_aggregation(client: AsyncClient):
    headers = await get_auth_headers(client, "aggtest@example.com", "aggtest")

    # Log some lifestyle data
    today_str = str(date.today())
    log_payload = {
        "log_date": today_str,
        "sleep_hours": 8.0,
        "water_intake": 2.5,
        "exercise_minutes": 30,
        "walking_steps": 10000,
        "screen_time": 3.5,
        "phone_unlock_count": 30,
        "work_hours": 6.0,
        "social_time": 2.0,
        "energy_level": 9
    }
    await client.post("/api/v1/lifestyle", json=log_payload, headers=headers)

    # 1. Fetch Today's Dashboard summary
    response = await client.get("/api/v1/dashboard/today", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert data["sleep_hours"] == 8.0
    assert data["walking_steps"] == 10000

    # 2. Fetch Sleep Analytics
    response = await client.get("/api/v1/analytics/sleep", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["average_hours"] == 8.0

    # 3. Fetch Water Analytics
    response = await client.get("/api/v1/analytics/water", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    assert response.json()["average_liters"] == 2.5
