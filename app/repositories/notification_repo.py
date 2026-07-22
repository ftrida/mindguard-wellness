from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.notification import Notification, NotificationPreference

class NotificationRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, notification_id: int) -> Optional[Notification]:
        stmt = select(Notification).where(
            Notification.id == notification_id,
            Notification.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_all_by_user_id(self, user_id: int, unread_only: bool = False) -> List[Notification]:
        if unread_only:
            stmt = select(Notification).where(
                Notification.user_id == user_id,
                Notification.is_read == False,
                Notification.is_deleted == False
            ).order_by(Notification.created_at.desc())
        else:
            stmt = select(Notification).where(
                Notification.user_id == user_id,
                Notification.is_deleted == False
            ).order_by(Notification.created_at.desc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, notification: Notification) -> Notification:
        self.db.add(notification)
        await self.db.flush()
        await self.db.refresh(notification)
        return notification

class NotificationPreferenceRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_user_id(self, user_id: int) -> Optional[NotificationPreference]:
        stmt = select(NotificationPreference).where(
            NotificationPreference.user_id == user_id,
            NotificationPreference.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def save(self, pref: NotificationPreference) -> NotificationPreference:
        self.db.add(pref)
        await self.db.flush()
        await self.db.refresh(pref)
        return pref
