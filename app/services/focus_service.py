from datetime import datetime
from typing import List
from fastapi import HTTPException, status
from app.models.focus import FocusSession
from app.repositories.focus_repo import FocusRepository
from app.schemas.focus import FocusSessionCreate

class FocusService:
    def __init__(self, focus_repo: FocusRepository):
        self.focus_repo = focus_repo

    async def get_session(self, user_id: int, session_id: int) -> FocusSession:
        session = await self.focus_repo.get_by_id(session_id)
        if not session or session.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Focus session not found")
        return session

    async def get_sessions_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[FocusSession]:
        return await self.focus_repo.get_range(user_id, start_time, end_time)

    async def create_session(self, user_id: int, session_data: FocusSessionCreate) -> FocusSession:
        session = FocusSession(
            user_id=user_id,
            custom_duration_seconds=session_data.custom_duration_seconds,
            break_duration_seconds=session_data.break_duration_seconds,
            completed_sessions_count=session_data.completed_sessions_count,
            completed_at=session_data.completed_at or datetime.utcnow()
        )
        return await self.focus_repo.save(session)

    async def delete_session(self, user_id: int, session_id: int) -> None:
        session = await self.get_session(user_id, session_id)
        session.is_deleted = True
        await self.focus_repo.save(session)
