from datetime import date
from typing import Optional, List
from fastapi import HTTPException, status
from app.models.lifestyle import DailyLifestyleLog
from app.repositories.lifestyle_repo import LifestyleRepository
from app.schemas.lifestyle import DailyLifestyleLogCreate, DailyLifestyleLogUpdate

class LifestyleService:
    def __init__(self, lifestyle_repo: LifestyleRepository):
        self.lifestyle_repo = lifestyle_repo

    async def get_log_by_date(self, user_id: int, log_date: date) -> DailyLifestyleLog:
        log = await self.lifestyle_repo.get_by_date(user_id, log_date)
        if not log:
            raise HTTPException(
                status_code=status.HTTP_404_NOT_FOUND,
                detail=f"Daily lifestyle log not found for date {log_date}"
            )
        return log

    async def create_log(self, user_id: int, log_data: DailyLifestyleLogCreate) -> DailyLifestyleLog:
        # Check if log already exists
        existing = await self.lifestyle_repo.get_by_date(user_id, log_data.log_date)
        if existing:
            # Instead of crashing, let's update it! This is standard idempotent UX.
            for field, value in log_data.model_dump().items():
                if field != "log_date":
                    setattr(existing, field, value)
            return await self.lifestyle_repo.save(existing)

        log = DailyLifestyleLog(
            user_id=user_id,
            log_date=log_data.log_date,
            **log_data.model_dump(exclude={"log_date"})
        )
        return await self.lifestyle_repo.save(log)

    async def update_log(self, user_id: int, log_date: date, update_data: DailyLifestyleLogUpdate) -> DailyLifestyleLog:
        log = await self.get_log_by_date(user_id, log_date)
        for field, value in update_data.model_dump(exclude_unset=True).items():
            setattr(log, field, value)
        return await self.lifestyle_repo.save(log)

    async def delete_log(self, user_id: int, log_date: date) -> None:
        log = await self.get_log_by_date(user_id, log_date)
        log.is_deleted = True
        await self.lifestyle_repo.save(log)
