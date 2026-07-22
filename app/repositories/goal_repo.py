from datetime import date
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.goal import Goal

class GoalRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, goal_id: int) -> Optional[Goal]:
        stmt = select(Goal).where(
            Goal.id == goal_id,
            Goal.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_all_by_user_id(self, user_id: int, status: Optional[str] = None) -> List[Goal]:
        if status:
            stmt = select(Goal).where(
                Goal.user_id == user_id,
                Goal.status == status,
                Goal.is_deleted == False
            )
        else:
            stmt = select(Goal).where(
                Goal.user_id == user_id,
                Goal.is_deleted == False
            )
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, goal: Goal) -> Goal:
        self.db.add(goal)
        await self.db.flush()
        await self.db.refresh(goal)
        return goal
