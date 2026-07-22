from datetime import date
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.behavior import BehaviorLog

class BehaviorRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_latest_by_user_id(self, user_id: int) -> Optional[BehaviorLog]:
        stmt = select(BehaviorLog).where(
            BehaviorLog.user_id == user_id,
            BehaviorLog.is_deleted == False
        ).order_by(BehaviorLog.analysis_date.desc())
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_range(self, user_id: int, start_date: date, end_date: date) -> List[BehaviorLog]:
        stmt = select(BehaviorLog).where(
            BehaviorLog.user_id == user_id,
            BehaviorLog.analysis_date >= start_date,
            BehaviorLog.analysis_date <= end_date,
            BehaviorLog.is_deleted == False
        ).order_by(BehaviorLog.analysis_date.asc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, log: BehaviorLog) -> BehaviorLog:
        self.db.add(log)
        await self.db.flush()
        await self.db.refresh(log)
        return log
