from datetime import date
from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.twin import DigitalLifestyleTwin

class TwinRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_latest_by_user_id(self, user_id: int) -> Optional[DigitalLifestyleTwin]:
        stmt = select(DigitalLifestyleTwin).where(
            DigitalLifestyleTwin.user_id == user_id,
            DigitalLifestyleTwin.is_deleted == False
        ).order_by(DigitalLifestyleTwin.snapshot_date.desc(), DigitalLifestyleTwin.version.desc())
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_by_date(self, user_id: int, snapshot_date: date) -> Optional[DigitalLifestyleTwin]:
        stmt = select(DigitalLifestyleTwin).where(
            DigitalLifestyleTwin.user_id == user_id,
            DigitalLifestyleTwin.snapshot_date == snapshot_date,
            DigitalLifestyleTwin.is_deleted == False
        ).order_by(DigitalLifestyleTwin.version.desc())
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_history(self, user_id: int, limit: int = 30) -> List[DigitalLifestyleTwin]:
        stmt = select(DigitalLifestyleTwin).where(
            DigitalLifestyleTwin.user_id == user_id,
            DigitalLifestyleTwin.is_deleted == False
        ).order_by(DigitalLifestyleTwin.snapshot_date.desc()).limit(limit)
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, twin: DigitalLifestyleTwin) -> DigitalLifestyleTwin:
        self.db.add(twin)
        await self.db.flush()
        await self.db.refresh(twin)
        return twin
