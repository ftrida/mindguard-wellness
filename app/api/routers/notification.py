from typing import List
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.notification_repo import NotificationRepository, NotificationPreferenceRepository
from app.services.notification_service import NotificationService
from app.schemas.notification import (
    NotificationResponse,
    NotificationPreferenceResponse,
    NotificationPreferenceUpdate
)

router = APIRouter(prefix="/notifications", tags=["System Notifications"])

async def get_notification_service(db: AsyncSession = Depends(get_db)) -> NotificationService:
    notification_repo = NotificationRepository(db)
    preference_repo = NotificationPreferenceRepository(db)
    return NotificationService(notification_repo, preference_repo)

@router.get("", response_model=List[NotificationResponse])
async def get_notifications(
    unread_only: bool = Query(False),
    current_user: User = Depends(get_current_active_user),
    service: NotificationService = Depends(get_notification_service)
):
    return await service.get_notifications(current_user.id, unread_only)

@router.put("/{notification_id}/read", response_model=NotificationResponse)
async def mark_as_read(
    notification_id: int,
    current_user: User = Depends(get_current_active_user),
    service: NotificationService = Depends(get_notification_service)
):
    return await service.mark_as_read(current_user.id, notification_id)

@router.get("/preferences", response_model=NotificationPreferenceResponse)
async def get_preferences(
    current_user: User = Depends(get_current_active_user),
    service: NotificationService = Depends(get_notification_service)
):
    return await service.get_preferences(current_user.id)

@router.put("/preferences", response_model=NotificationPreferenceResponse)
async def update_preferences(
    update_data: NotificationPreferenceUpdate,
    current_user: User = Depends(get_current_active_user),
    service: NotificationService = Depends(get_notification_service)
):
    return await service.update_preferences(current_user.id, update_data)
