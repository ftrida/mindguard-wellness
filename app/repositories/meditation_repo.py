from datetime import datetime
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.meditation import MeditationSession

class MeditationRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, session_id: int) -> Optional[MeditationSession]:
        stmt = select(MeditationSession).where(
            MeditationSession.id == session_id,
            MeditationSession.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[MeditationSession]:
        stmt = select(MeditationSession).where(
            MeditationSession.user_id == user_id,
            MeditationSession.completed_at >= start_time,
            MeditationSession.completed_at <= end_time,
            MeditationSession.is_deleted == False
        ).order_by(MeditationSession.completed_at.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, session: MeditationSession) -> MeditationSession:
        self.db.add(session)
        await self.db.flush()
        await self.db.refresh(session)
        return session
