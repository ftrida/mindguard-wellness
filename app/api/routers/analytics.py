from datetime import date, timedelta
from typing import Optional
from fastapi import APIRouter, Depends, Query
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.meditation_repo import MeditationRepository
from app.repositories.focus_repo import FocusRepository
from app.services.analytics_service import AnalyticsService
from app.schemas.analytics import (
    TrendAnalyticsResponse,
    SleepAnalyticsResponse,
    WaterAnalyticsResponse,
    ExerciseAnalyticsResponse,
    FocusAnalyticsResponse,
    MeditationAnalyticsResponse,
    JournalActivityResponse
)

router = APIRouter(prefix="/analytics", tags=["Dashboard Analytics API"])

async def get_analytics_service(db: AsyncSession = Depends(get_db)) -> AnalyticsService:
    lifestyle_repo = LifestyleRepository(db)
    mood_repo = MoodRepository(db)
    journal_repo = JournalRepository(db)
    meditation_repo = MeditationRepository(db)
    focus_repo = FocusRepository(db)
    return AnalyticsService(
        lifestyle_repo=lifestyle_repo,
        mood_repo=mood_repo,
        journal_repo=journal_repo,
        meditation_repo=meditation_repo,
        focus_repo=focus_repo
    )

@router.get("/trends", response_model=TrendAnalyticsResponse)
async def get_trends(
    metric: str = Query(..., description="Metrics: sleep, water, exercise, steps, screen"),
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_lifestyle_trends(current_user.id, metric, start_val, end_val)

@router.get("/sleep", response_model=SleepAnalyticsResponse)
async def get_sleep_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_sleep_analytics(current_user.id, start_val, end_val)

@router.get("/water", response_model=WaterAnalyticsResponse)
async def get_water_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_water_analytics(current_user.id, start_val, end_val)

@router.get("/exercise", response_model=ExerciseAnalyticsResponse)
async def get_exercise_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_exercise_analytics(current_user.id, start_val, end_val)

@router.get("/focus", response_model=FocusAnalyticsResponse)
async def get_focus_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_focus_analytics(current_user.id, start_val, end_val)

@router.get("/meditation", response_model=MeditationAnalyticsResponse)
async def get_meditation_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_meditation_analytics(current_user.id, start_val, end_val)

@router.get("/journals", response_model=JournalActivityResponse)
async def get_journals_analytics(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: AnalyticsService = Depends(get_analytics_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_journal_activity(current_user.id, start_val, end_val)
