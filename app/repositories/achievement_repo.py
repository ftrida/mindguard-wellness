from typing import List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.achievement import Achievement

class AchievementRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_unlocked_by_user_id(self, user_id: int) -> List[Achievement]:
        stmt = select(Achievement).where(
            Achievement.user_id == user_id,
            Achievement.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, achievement: Achievement) -> Achievement:
        self.db.add(achievement)
        await self.db.flush()
        await self.db.refresh(achievement)
        return achievement
