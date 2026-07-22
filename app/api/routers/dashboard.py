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
from app.services.dashboard_service import DashboardService
from app.schemas.dashboard import TodaySummaryResponse, HistoricalSummaryResponse

router = APIRouter(prefix="/dashboard", tags=["Dashboard Summary API"])

async def get_dashboard_service(db: AsyncSession = Depends(get_db)) -> DashboardService:
    lifestyle_repo = LifestyleRepository(db)
    mood_repo = MoodRepository(db)
    journal_repo = JournalRepository(db)
    meditation_repo = MeditationRepository(db)
    focus_repo = FocusRepository(db)
    return DashboardService(
        lifestyle_repo=lifestyle_repo,
        mood_repo=mood_repo,
        journal_repo=journal_repo,
        meditation_repo=meditation_repo,
        focus_repo=focus_repo
    )

@router.get("/today", response_model=TodaySummaryResponse)
async def get_today_summary(
    current_user: User = Depends(get_current_active_user),
    service: DashboardService = Depends(get_dashboard_service)
):
    return await service.get_today_summary(current_user.id)

@router.get("/historical", response_model=HistoricalSummaryResponse)
async def get_historical_summary(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: DashboardService = Depends(get_dashboard_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=7))
    return await service.get_historical_summary(current_user.id, start_val, end_val)
