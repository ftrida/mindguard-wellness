from typing import List, Optional
from fastapi import HTTPException, status
from app.models.journal import JournalEntry
from app.repositories.journal_repo import JournalRepository
from app.schemas.journal import JournalEntryCreate, JournalEntryUpdate

class JournalService:
    def __init__(self, journal_repo: JournalRepository):
        self.journal_repo = journal_repo

    async def get_entry(self, user_id: int, entry_id: int) -> JournalEntry:
        entry = await self.journal_repo.get_by_id(entry_id)
        if not entry or entry.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Journal entry not found")
        return entry

    async def get_all_entries(self, user_id: int) -> List[JournalEntry]:
        return await self.journal_repo.get_all_by_user_id(user_id)

    async def search_entries(self, user_id: int, query: str) -> List[JournalEntry]:
        return await self.journal_repo.search(user_id, query)

    async def create_entry(self, user_id: int, entry_data: JournalEntryCreate) -> JournalEntry:
        # Resolve tags
        resolved_tags = []
        for tag_name in entry_data.tags:
            tag = await self.journal_repo.get_or_create_tag(tag_name)
            resolved_tags.append(tag)

        entry = JournalEntry(
            user_id=user_id,
            title=entry_data.title,
            content=entry_data.content,
            category=entry_data.category,
            tags=resolved_tags
        )
        return await self.journal_repo.save(entry)

    async def update_entry(self, user_id: int, entry_id: int, update_data: JournalEntryUpdate) -> JournalEntry:
        entry = await self.get_entry(user_id, entry_id)

        # Update text fields
        for field, value in update_data.model_dump(exclude_unset=True, exclude={"tags"}).items():
            setattr(entry, field, value)

        # If tags are provided, update relationship
        if update_data.tags is not None:
            resolved_tags = []
            for tag_name in update_data.tags:
                tag = await self.journal_repo.get_or_create_tag(tag_name)
                resolved_tags.append(tag)
            entry.tags = resolved_tags

        return await self.journal_repo.save(entry)

    async def delete_entry(self, user_id: int, entry_id: int) -> None:
        entry = await self.get_entry(user_id, entry_id)
        entry.is_deleted = True
        await self.journal_repo.save(entry)
