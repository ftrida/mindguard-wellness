from datetime import date
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.lifestyle import DailyLifestyleLog

class LifestyleRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_date(self, user_id: int, log_date: date) -> Optional[DailyLifestyleLog]:
        stmt = select(DailyLifestyleLog).where(
            DailyLifestyleLog.user_id == user_id,
            DailyLifestyleLog.log_date == log_date,
            DailyLifestyleLog.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_by_id(self, log_id: int) -> Optional[DailyLifestyleLog]:
        stmt = select(DailyLifestyleLog).where(
            DailyLifestyleLog.id == log_id,
            DailyLifestyleLog.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_date: date, end_date: date) -> List[DailyLifestyleLog]:
        stmt = select(DailyLifestyleLog).where(
            DailyLifestyleLog.user_id == user_id,
            DailyLifestyleLog.log_date >= start_date,
            DailyLifestyleLog.log_date <= end_date,
            DailyLifestyleLog.is_deleted == False
        ).order_by(DailyLifestyleLog.log_date.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, log: DailyLifestyleLog) -> DailyLifestyleLog:
        self.db.add(log)
        await self.db.flush()
        await self.db.refresh(log)
        return log
