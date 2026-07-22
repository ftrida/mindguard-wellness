from datetime import datetime
from typing import List
from fastapi import HTTPException, status
from app.models.meditation import MeditationSession
from app.repositories.meditation_repo import MeditationRepository
from app.schemas.meditation import MeditationSessionCreate

class MeditationService:
    def __init__(self, meditation_repo: MeditationRepository):
        self.meditation_repo = meditation_repo

    async def get_session(self, user_id: int, session_id: int) -> MeditationSession:
        session = await self.meditation_repo.get_by_id(session_id)
        if not session or session.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Meditation session not found")
        return session

    async def get_sessions_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[MeditationSession]:
        return await self.meditation_repo.get_range(user_id, start_time, end_time)

    async def create_session(self, user_id: int, session_data: MeditationSessionCreate) -> MeditationSession:
        session = MeditationSession(
            user_id=user_id,
            category=session_data.category,
            duration_seconds=session_data.duration_seconds,
            completed_at=session_data.completed_at or datetime.utcnow()
        )
        return await self.meditation_repo.save(session)

    async def delete_session(self, user_id: int, session_id: int) -> None:
        session = await self.get_session(user_id, session_id)
        session.is_deleted = True
        await self.meditation_repo.save(session)
