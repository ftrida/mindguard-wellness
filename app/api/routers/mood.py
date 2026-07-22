from datetime import datetime, timedelta
from typing import List, Optional
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.mood_repo import MoodRepository
from app.services.mood_service import MoodService
from app.schemas.mood import (
    MoodEntryResponse,
    MoodEntryCreate,
    MoodEntryUpdate,
    MoodStatsResponse
)

router = APIRouter(prefix="/mood", tags=["Mood Tracker"])

async def get_mood_service(db: AsyncSession = Depends(get_db)) -> MoodService:
    mood_repo = MoodRepository(db)
    return MoodService(mood_repo)

@router.get("", response_model=List[MoodEntryResponse])
async def get_entries(
    start_time: Optional[datetime] = Query(None),
    end_time: Optional[datetime] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    end_dt = end_time or datetime.utcnow()
    start_dt = start_time or (end_dt - timedelta(days=7))
    return await service.get_entries_range(current_user.id, start_dt, end_dt)

@router.get("/stats", response_model=MoodStatsResponse)
async def get_stats(
    start_time: Optional[datetime] = Query(None),
    end_time: Optional[datetime] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    end_dt = end_time or datetime.utcnow()
    start_dt = start_time or (end_dt - timedelta(days=7))
    return await service.get_stats(current_user.id, start_dt, end_dt)

@router.get("/{entry_id}", response_model=MoodEntryResponse)
async def get_entry(
    entry_id: int,
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    return await service.get_entry(current_user.id, entry_id)

@router.post("", response_model=MoodEntryResponse, status_code=status.HTTP_201_CREATED)
async def create_entry(
    entry_data: MoodEntryCreate,
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    return await service.create_entry(current_user.id, entry_data)

@router.put("/{entry_id}", response_model=MoodEntryResponse)
async def update_entry(
    entry_id: int,
    update_data: MoodEntryUpdate,
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    return await service.update_entry(current_user.id, entry_id, update_data)

@router.delete("/{entry_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_entry(
    entry_id: int,
    current_user: User = Depends(get_current_active_user),
    service: MoodService = Depends(get_mood_service)
):
    await service.delete_entry(current_user.id, entry_id)
