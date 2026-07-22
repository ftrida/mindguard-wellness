from typing import List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.coach import CoachConversationMemory

class CoachRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_memory_by_user_id(self, user_id: int, limit: int = 20) -> List[CoachConversationMemory]:
        stmt = select(CoachConversationMemory).where(
            CoachConversationMemory.user_id == user_id,
            CoachConversationMemory.is_deleted == False
        ).order_by(CoachConversationMemory.created_at.asc()).limit(limit)
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, memory: CoachConversationMemory) -> CoachConversationMemory:
        self.db.add(memory)
        await self.db.flush()
        await self.db.refresh(memory)
        return memory

    async def clear_memory_by_user_id(self, user_id: int) -> None:
        stmt = select(CoachConversationMemory).where(
            CoachConversationMemory.user_id == user_id,
            CoachConversationMemory.is_deleted == False
        )
        result = await self.db.execute(stmt)
        for record in result.scalars().all():
            record.is_deleted = True
        await self.db.flush()
