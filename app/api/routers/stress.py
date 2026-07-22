from datetime import date, timedelta
from typing import List, Optional
from fastapi import APIRouter, Depends, Query
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.stress_repo import StressRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.focus_repo import FocusRepository
from app.repositories.journal_repo import JournalRepository
from app.services.stress_service import StressService
from app.schemas.stress import StressLikelihoodResponse

router = APIRouter(prefix="/stress", tags=["Stress Likelihood Engine API"])

async def get_stress_service(db: AsyncSession = Depends(get_db)) -> StressService:
    return StressService(
        stress_repo=StressRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db),
        focus_repo=FocusRepository(db),
        journal_repo=JournalRepository(db)
    )

@router.get("/assessment", response_model=StressLikelihoodResponse)
async def get_current_stress_assessment(
    current_user: User = Depends(get_current_active_user),
    service: StressService = Depends(get_stress_service)
):
    assessment = await service.get_latest_assessment(current_user.id)
    if not assessment:
        # Generate live assessment for today
        assessment = await service.assess_stress_likelihood(current_user.id, date.today())
    return assessment

@router.get("/history", response_model=List[StressLikelihoodResponse])
async def get_stress_history(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: StressService = Depends(get_stress_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=30))
    return await service.get_history(current_user.id, start_val, end_val)
