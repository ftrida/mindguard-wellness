from datetime import datetime
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.focus import FocusSession

class FocusRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, session_id: int) -> Optional[FocusSession]:
        stmt = select(FocusSession).where(
            FocusSession.id == session_id,
            FocusSession.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[FocusSession]:
        stmt = select(FocusSession).where(
            FocusSession.user_id == user_id,
            FocusSession.completed_at >= start_time,
            FocusSession.completed_at <= end_time,
            FocusSession.is_deleted == False
        ).order_by(FocusSession.completed_at.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, session: FocusSession) -> FocusSession:
        self.db.add(session)
        await self.db.flush()
        await self.db.refresh(session)
        return session
