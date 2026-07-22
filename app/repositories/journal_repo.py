from datetime import datetime
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from sqlalchemy.orm import selectinload
from app.models.journal import JournalEntry, JournalTag

class JournalRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, journal_id: int) -> Optional[JournalEntry]:
        stmt = select(JournalEntry).options(selectinload(JournalEntry.tags)).where(
            JournalEntry.id == journal_id,
            JournalEntry.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_all_by_user_id(self, user_id: int) -> List[JournalEntry]:
        stmt = select(JournalEntry).options(selectinload(JournalEntry.tags)).where(
            JournalEntry.user_id == user_id,
            JournalEntry.is_deleted == False
        ).order_by(JournalEntry.created_at.desc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def search(self, user_id: int, query: str) -> List[JournalEntry]:
        # Simple string containment search for title or content
        pattern = f"%{query}%"
        stmt = select(JournalEntry).options(selectinload(JournalEntry.tags)).where(
            JournalEntry.user_id == user_id,
            JournalEntry.is_deleted == False,
            (JournalEntry.title.like(pattern) | JournalEntry.content.like(pattern))
        ).order_by(JournalEntry.created_at.desc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def get_or_create_tag(self, name: str) -> JournalTag:
        stmt = select(JournalTag).where(JournalTag.name == name)
        result = await self.db.execute(stmt)
        tag = result.scalars().first()
        if not tag:
            tag = JournalTag(name=name)
            self.db.add(tag)
            await self.db.flush()
        return tag

    async def save(self, entry: JournalEntry) -> JournalEntry:
        self.db.add(entry)
        await self.db.flush()
        # Eagerly load tags after saving so it serializes correctly
        stmt = select(JournalEntry).options(selectinload(JournalEntry.tags)).where(JournalEntry.id == entry.id)
        res = await self.db.execute(stmt)
        return res.scalars().first()

    async def get_range(self, user_id: int, start_datetime: datetime, end_datetime: datetime) -> List[JournalEntry]:
        stmt = select(JournalEntry).options(selectinload(JournalEntry.tags)).where(
            JournalEntry.user_id == user_id,
            JournalEntry.created_at >= start_datetime,
            JournalEntry.created_at <= end_datetime,
            JournalEntry.is_deleted == False
        ).order_by(JournalEntry.created_at.desc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())
