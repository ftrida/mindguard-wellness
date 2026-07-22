from datetime import datetime, timedelta
from typing import List, Optional
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.focus_repo import FocusRepository
from app.services.focus_service import FocusService
from app.schemas.focus import (
    FocusSessionResponse,
    FocusSessionCreate
)

router = APIRouter(prefix="/focus", tags=["Focus Session Tracker"])

async def get_focus_service(db: AsyncSession = Depends(get_db)) -> FocusService:
    focus_repo = FocusRepository(db)
    return FocusService(focus_repo)

@router.get("", response_model=List[FocusSessionResponse])
async def get_sessions(
    start_time: Optional[datetime] = Query(None),
    end_time: Optional[datetime] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: FocusService = Depends(get_focus_service)
):
    end_dt = end_time or datetime.utcnow()
    start_dt = start_time or (end_dt - timedelta(days=7))
    return await service.get_sessions_range(current_user.id, start_dt, end_dt)

@router.post("", response_model=FocusSessionResponse, status_code=status.HTTP_201_CREATED)
async def create_session(
    session_data: FocusSessionCreate,
    current_user: User = Depends(get_current_active_user),
    service: FocusService = Depends(get_focus_service)
):
    return await service.create_session(current_user.id, session_data)

@router.delete("/{session_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_session(
    session_id: int,
    current_user: User = Depends(get_current_active_user),
    service: FocusService = Depends(get_focus_service)
):
    await service.delete_session(current_user.id, session_id)
