from typing import List
from fastapi import HTTPException, status
from app.models.notification import Notification, NotificationPreference
from app.repositories.notification_repo import NotificationRepository, NotificationPreferenceRepository
from app.schemas.notification import NotificationPreferenceUpdate

class NotificationService:
    def __init__(self, notification_repo: NotificationRepository, preference_repo: NotificationPreferenceRepository):
        self.notification_repo = notification_repo
        self.preference_repo = preference_repo

    async def get_notifications(self, user_id: int, unread_only: bool = False) -> List[Notification]:
        return await self.notification_repo.get_all_by_user_id(user_id, unread_only)

    async def mark_as_read(self, user_id: int, notification_id: int) -> Notification:
        notification = await self.notification_repo.get_by_id(notification_id)
        if not notification or notification.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Notification not found")
        notification.is_read = True
        return await self.notification_repo.save(notification)

    async def create_notification(self, user_id: int, title: str, message: str, category: str = "System") -> Notification:
        notification = Notification(
            user_id=user_id,
            title=title,
            message=message,
            category=category
        )
        return await self.notification_repo.save(notification)

    async def get_preferences(self, user_id: int) -> NotificationPreference:
        pref = await self.preference_repo.get_by_user_id(user_id)
        if not pref:
            pref = NotificationPreference(user_id=user_id, email_enabled=True, push_enabled=True)
            pref = await self.preference_repo.save(pref)
        return pref

    async def update_preferences(self, user_id: int, update_data: NotificationPreferenceUpdate) -> NotificationPreference:
        pref = await self.get_preferences(user_id)
        for field, value in update_data.model_dump(exclude_unset=True).items():
            setattr(pref, field, value)
        return await self.preference_repo.save(pref)
