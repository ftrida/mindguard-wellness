from datetime import datetime, timedelta
from typing import List
from fastapi import HTTPException, status
from app.models.mood import MoodEntry
from app.repositories.mood_repo import MoodRepository
from app.schemas.mood import MoodEntryCreate, MoodEntryUpdate, MoodStatsResponse

class MoodService:
    def __init__(self, mood_repo: MoodRepository):
        self.mood_repo = mood_repo

    async def get_entry(self, user_id: int, entry_id: int) -> MoodEntry:
        entry = await self.mood_repo.get_by_id(entry_id)
        if not entry or entry.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Mood entry not found")
        return entry

    async def get_entries_range(self, user_id: int, start_time: datetime, end_time: datetime) -> List[MoodEntry]:
        return await self.mood_repo.get_range(user_id, start_time, end_time)

    async def create_entry(self, user_id: int, entry_data: MoodEntryCreate) -> MoodEntry:
        entry = MoodEntry(
            user_id=user_id,
            mood_score=entry_data.mood_score,
            notes=entry_data.notes,
            category=entry_data.category,
            entry_time=entry_data.entry_time or datetime.utcnow()
        )
        return await self.mood_repo.save(entry)

    async def update_entry(self, user_id: int, entry_id: int, update_data: MoodEntryUpdate) -> MoodEntry:
        entry = await self.get_entry(user_id, entry_id)
        for field, value in update_data.model_dump(exclude_unset=True).items():
            setattr(entry, field, value)
        return await self.mood_repo.save(entry)

    async def delete_entry(self, user_id: int, entry_id: int) -> None:
        entry = await self.get_entry(user_id, entry_id)
        entry.is_deleted = True
        await self.mood_repo.save(entry)

    async def get_stats(self, user_id: int, start_time: datetime, end_time: datetime) -> MoodStatsResponse:
        entries = await self.mood_repo.get_range(user_id, start_time, end_time)
        if not entries:
            return MoodStatsResponse(
                average_score=0.0,
                entries_count=0,
                category_distribution={},
                highest_score=0,
                lowest_score=0
            )

        total_score = sum(e.mood_score for e in entries)
        highest = max(e.mood_score for e in entries)
        lowest = min(e.mood_score for e in entries)

        dist = {}
        for e in entries:
            dist[e.category] = dist.get(e.category, 0) + 1

        return MoodStatsResponse(
            average_score=round(total_score / len(entries), 2),
            entries_count=len(entries),
            category_distribution=dist,
            highest_score=highest,
            lowest_score=lowest
        )
