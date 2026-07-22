import pytest
from httpx import AsyncClient
from fastapi import status

@pytest.mark.asyncio
async def test_register_user_success(client: AsyncClient):
    payload = {
        "email": "testuser@example.com",
        "username": "testuser",
        "password": "Password123!",
        "phone": "+1234567890"
    }
    response = await client.post("/api/v1/auth/register", json=payload)
    assert response.status_code == status.HTTP_201_CREATED
    data = response.json()
    assert data["email"] == "testuser@example.com"
    assert data["username"] == "testuser"
    assert "id" in data
    assert data["is_active"] is True
    assert data["is_verified"] is False

@pytest.mark.asyncio
async def test_register_user_duplicate_email(client: AsyncClient):
    payload = {
        "email": "duplicate@example.com",
        "username": "user1",
        "password": "Password123!",
    }
    # Register once
    res1 = await client.post("/api/v1/auth/register", json=payload)
    assert res1.status_code == status.HTTP_201_CREATED

    # Register twice
    payload2 = {
        "email": "duplicate@example.com",
        "username": "user2",
        "password": "Password123!",
    }
    res2 = await client.post("/api/v1/auth/register", json=payload2)
    assert res2.status_code == status.HTTP_409_CONFLICT

@pytest.mark.asyncio
async def test_register_user_invalid_password(client: AsyncClient):
    payload = {
        "email": "invalidpass@example.com",
        "username": "invalidpass",
        "password": "123", # Too short, no upper/lowercase/symbol
    }
    response = await client.post("/api/v1/auth/register", json=payload)
    assert response.status_code == status.HTTP_422_UNPROCESSABLE_ENTITY

@pytest.mark.asyncio
async def test_login_user_success(client: AsyncClient):
    # Register
    reg_payload = {
        "email": "loginuser@example.com",
        "username": "loginuser",
        "password": "Password123!"
    }
    await client.post("/api/v1/auth/register", json=reg_payload)

    # Login
    login_payload = {
        "email_or_username": "loginuser",
        "password": "Password123!"
    }
    response = await client.post("/api/v1/auth/login", json=login_payload)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "access_token" in data
    assert "refresh_token" in data
    assert data["token_type"] == "bearer"

@pytest.mark.asyncio
async def test_login_user_incorrect_credentials(client: AsyncClient):
    login_payload = {
        "email_or_username": "nonexistent",
        "password": "WrongPassword1!"
    }
    response = await client.post("/api/v1/auth/login", json=login_payload)
    assert response.status_code == status.HTTP_401_UNAUTHORIZED

@pytest.mark.asyncio
async def test_get_me_success(client: AsyncClient):
    # Register
    reg_payload = {
        "email": "meuser@example.com",
        "username": "meuser",
        "password": "Password123!"
    }
    await client.post("/api/v1/auth/register", json=reg_payload)

    # Login
    login_payload = {
        "email_or_username": "meuser",
        "password": "Password123!"
    }
    login_res = await client.post("/api/v1/auth/login", json=login_payload)
    token = login_res.json()["access_token"]

    # Access /auth/me
    headers = {"Authorization": f"Bearer {token}"}
    response = await client.get("/api/v1/auth/me", headers=headers)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert data["email"] == "meuser@example.com"
    assert data["username"] == "meuser"

@pytest.mark.asyncio
async def test_refresh_token_success(client: AsyncClient):
    # Register
    reg_payload = {
        "email": "refreshuser@example.com",
        "username": "refreshuser",
        "password": "Password123!"
    }
    await client.post("/api/v1/auth/register", json=reg_payload)

    # Login
    login_payload = {
        "email_or_username": "refreshuser",
        "password": "Password123!"
    }
    login_res = await client.post("/api/v1/auth/login", json=login_payload)
    refresh_token = login_res.json()["refresh_token"]

    # Refresh
    refresh_payload = {
        "refresh_token": refresh_token
    }
    response = await client.post("/api/v1/auth/refresh", json=refresh_payload)
    assert response.status_code == status.HTTP_200_OK
    data = response.json()
    assert "access_token" in data
    assert "refresh_token" in data

@pytest.mark.asyncio
async def test_logout_success(client: AsyncClient):
    # Register
    reg_payload = {
        "email": "logoutuser@example.com",
        "username": "logoutuser",
        "password": "Password123!"
    }
    await client.post("/api/v1/auth/register", json=reg_payload)

    # Login
    login_payload = {
        "email_or_username": "logoutuser",
        "password": "Password123!"
    }
    login_res = await client.post("/api/v1/auth/login", json=login_payload)
    refresh_token = login_res.json()["refresh_token"]

    # Logout
    logout_payload = {
        "refresh_token": refresh_token
    }
    response = await client.post("/api/v1/auth/logout", json=logout_payload)
    assert response.status_code == status.HTTP_200_OK
