import asyncio
from typing import AsyncGenerator
import pytest
from httpx import ASGITransport, AsyncClient
from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker, AsyncSession
from app.database.database import Base
from app.database.session import get_db
from app.main import app
from app.models.auth import Role
from app.core.constants import ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN

DATABASE_URL = "sqlite+aiosqlite:///:memory:"

engine = create_async_engine(DATABASE_URL, connect_args={"check_same_thread": False})
TestingSessionLocal = async_sessionmaker(
    bind=engine,
    autoflush=False,
    autocommit=False,
    expire_on_commit=False
)

@pytest.fixture(autouse=True)
async def setup_db():
    # Build database tables
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.create_all)
    
    # Seed security roles
    async with TestingSessionLocal() as session:
        for r_name in [ROLE_USER, ROLE_ADMIN, ROLE_SUPER_ADMIN]:
            role = Role(name=r_name, description=f"{r_name} role")
            session.add(role)
        await session.commit()

    yield

    # Teardown database tables
    async with engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)

@pytest.fixture
async def db_session() -> AsyncGenerator[AsyncSession, None]:
    async with TestingSessionLocal() as session:
        try:
            yield session
            await session.commit()
        except Exception:
            await session.rollback()
            raise
        finally:
            await session.close()

@pytest.fixture(autouse=True)
def override_db(db_session: AsyncSession):
    app.dependency_overrides[get_db] = lambda: db_session
    yield
    app.dependency_overrides.pop(get_db, None)

@pytest.fixture
async def client() -> AsyncGenerator[AsyncClient, None]:
    async with AsyncClient(transport=ASGITransport(app=app), base_url="http://test") as ac:
        yield ac
