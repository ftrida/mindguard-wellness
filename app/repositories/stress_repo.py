from datetime import date
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.stress import StressLikelihood

class StressRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_latest_by_user_id(self, user_id: int) -> Optional[StressLikelihood]:
        stmt = select(StressLikelihood).where(
            StressLikelihood.user_id == user_id,
            StressLikelihood.is_deleted == False
        ).order_by(StressLikelihood.assessment_date.desc())
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_date: date, end_date: date) -> List[StressLikelihood]:
        stmt = select(StressLikelihood).where(
            StressLikelihood.user_id == user_id,
            StressLikelihood.assessment_date >= start_date,
            StressLikelihood.assessment_date <= end_date,
            StressLikelihood.is_deleted == False
        ).order_by(StressLikelihood.assessment_date.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, stress: StressLikelihood) -> StressLikelihood:
        self.db.add(stress)
        await self.db.flush()
        await self.db.refresh(stress)
        return stress
