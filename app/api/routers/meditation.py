from datetime import datetime, timedelta
from typing import List, Optional
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.meditation_repo import MeditationRepository
from app.services.meditation_service import MeditationService
from app.schemas.meditation import (
    MeditationSessionResponse,
    MeditationSessionCreate
)

router = APIRouter(prefix="/meditations", tags=["Meditation Tracker"])

async def get_meditation_service(db: AsyncSession = Depends(get_db)) -> MeditationService:
    meditation_repo = MeditationRepository(db)
    return MeditationService(meditation_repo)

@router.get("", response_model=List[MeditationSessionResponse])
async def get_sessions(
    start_time: Optional[datetime] = Query(None),
    end_time: Optional[datetime] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: MeditationService = Depends(get_meditation_service)
):
    end_dt = end_time or datetime.utcnow()
    start_dt = start_time or (end_dt - timedelta(days=7))
    return await service.get_sessions_range(current_user.id, start_dt, end_dt)

@router.post("", response_model=MeditationSessionResponse, status_code=status.HTTP_201_CREATED)
async def create_session(
    session_data: MeditationSessionCreate,
    current_user: User = Depends(get_current_active_user),
    service: MeditationService = Depends(get_meditation_service)
):
    return await service.create_session(current_user.id, session_data)

@router.delete("/{session_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_session(
    session_id: int,
    current_user: User = Depends(get_current_active_user),
    service: MeditationService = Depends(get_meditation_service)
):
    await service.delete_session(current_user.id, session_id)
