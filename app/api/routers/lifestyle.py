from datetime import date
from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.lifestyle_repo import LifestyleRepository
from app.services.lifestyle_service import LifestyleService
from app.schemas.lifestyle import (
    DailyLifestyleLogResponse,
    DailyLifestyleLogCreate,
    DailyLifestyleLogUpdate
)

router = APIRouter(prefix="/lifestyle", tags=["Daily Lifestyle Tracker"])

async def get_lifestyle_service(db: AsyncSession = Depends(get_db)) -> LifestyleService:
    lifestyle_repo = LifestyleRepository(db)
    return LifestyleService(lifestyle_repo)

@router.get("/{log_date}", response_model=DailyLifestyleLogResponse)
async def get_log(
    log_date: date,
    current_user: User = Depends(get_current_active_user),
    service: LifestyleService = Depends(get_lifestyle_service)
):
    return await service.get_log_by_date(current_user.id, log_date)

@router.post("", response_model=DailyLifestyleLogResponse, status_code=status.HTTP_201_CREATED)
async def create_log(
    log_data: DailyLifestyleLogCreate,
    current_user: User = Depends(get_current_active_user),
    service: LifestyleService = Depends(get_lifestyle_service)
):
    return await service.create_log(current_user.id, log_data)

@router.put("/{log_date}", response_model=DailyLifestyleLogResponse)
async def update_log(
    log_date: date,
    update_data: DailyLifestyleLogUpdate,
    current_user: User = Depends(get_current_active_user),
    service: LifestyleService = Depends(get_lifestyle_service)
):
    return await service.update_log(current_user.id, log_date, update_data)

@router.delete("/{log_date}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_log(
    log_date: date,
    current_user: User = Depends(get_current_active_user),
    service: LifestyleService = Depends(get_lifestyle_service)
):
    await service.delete_log(current_user.id, log_date)
