from datetime import date
from typing import List, Optional
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.twin_repo import TwinRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.services.twin_service import TwinService
from app.schemas.twin import DigitalLifestyleTwinResponse, TwinComparisonResponse

router = APIRouter(prefix="/twin", tags=["Digital Lifestyle Twin API"])

async def get_twin_service(db: AsyncSession = Depends(get_db)) -> TwinService:
    return TwinService(
        twin_repo=TwinRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db)
    )

@router.get("", response_model=DigitalLifestyleTwinResponse)
async def get_latest_twin(
    current_user: User = Depends(get_current_active_user),
    service: TwinService = Depends(get_twin_service)
):
    twin = await service.get_latest_twin(current_user.id)
    if not twin:
        # Generate initial baseline twin
        twin = await service.calculate_and_save_twin(current_user.id, date.today())
    return twin

@router.get("/compare", response_model=TwinComparisonResponse)
async def compare_twin_with_today(
    target_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: TwinService = Depends(get_twin_service)
):
    t_date = target_date or date.today()
    return await service.compare_twin(current_user.id, t_date)

@router.get("/history", response_model=List[DigitalLifestyleTwinResponse])
async def get_twin_history(
    limit: int = Query(30, ge=1, le=100),
    current_user: User = Depends(get_current_active_user),
    service: TwinService = Depends(get_twin_service)
):
    return await service.get_history(current_user.id, limit)

@router.post("/regenerate", response_model=DigitalLifestyleTwinResponse, status_code=status.HTTP_201_CREATED)
async def regenerate_twin_baseline(
    current_user: User = Depends(get_current_active_user),
    service: TwinService = Depends(get_twin_service)
):
    return await service.calculate_and_save_twin(current_user.id, date.today())
