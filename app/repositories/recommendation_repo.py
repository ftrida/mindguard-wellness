from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.recommendation import Recommendation

class RecommendationRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, rec_id: int) -> Optional[Recommendation]:
        stmt = select(Recommendation).where(
            Recommendation.id == rec_id,
            Recommendation.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_active_by_user_id(self, user_id: int) -> List[Recommendation]:
        stmt = select(Recommendation).where(
            Recommendation.user_id == user_id,
            Recommendation.is_completed == False,
            Recommendation.is_deleted == False
        ).order_by(Recommendation.priority_score.desc())
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, rec: Recommendation) -> Recommendation:
        self.db.add(rec)
        await self.db.flush()
        await self.db.refresh(rec)
        return rec
