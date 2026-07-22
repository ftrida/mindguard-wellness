from datetime import datetime
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.mood import MoodEntry

class MoodRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, entry_id: int) -> Optional[MoodEntry]:
        stmt = select(MoodEntry).where(
            MoodEntry.id == entry_id,
            MoodEntry.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[MoodEntry]:
        stmt = select(MoodEntry).where(
            MoodEntry.user_id == user_id,
            MoodEntry.entry_time >= start_time,
            MoodEntry.entry_time <= end_time,
            MoodEntry.is_deleted == False
        ).order_by(MoodEntry.entry_time.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, entry: MoodEntry) -> MoodEntry:
        self.db.add(entry)
        await self.db.flush()
        await self.db.refresh(entry)
        return entry
