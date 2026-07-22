from typing import List
from fastapi import APIRouter, Depends, Query, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.journal_repo import JournalRepository
from app.services.journal_service import JournalService
from app.schemas.journal import (
    JournalEntryResponse,
    JournalEntryCreate,
    JournalEntryUpdate
)

router = APIRouter(prefix="/journals", tags=["Journal Entries"])

async def get_journal_service(db: AsyncSession = Depends(get_db)) -> JournalService:
    journal_repo = JournalRepository(db)
    return JournalService(journal_repo)

@router.get("", response_model=List[JournalEntryResponse])
async def get_all_entries(
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    return await service.get_all_entries(current_user.id)

@router.get("/search", response_model=List[JournalEntryResponse])
async def search_entries(
    q: str = Query(..., min_length=1),
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    return await service.search_entries(current_user.id, q)

@router.get("/{entry_id}", response_model=JournalEntryResponse)
async def get_entry(
    entry_id: int,
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    return await service.get_entry(current_user.id, entry_id)

@router.post("", response_model=JournalEntryResponse, status_code=status.HTTP_201_CREATED)
async def create_entry(
    entry_data: JournalEntryCreate,
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    return await service.create_entry(current_user.id, entry_data)

@router.put("/{entry_id}", response_model=JournalEntryResponse)
async def update_entry(
    entry_id: int,
    update_data: JournalEntryUpdate,
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    return await service.update_entry(current_user.id, entry_id, update_data)

@router.delete("/{entry_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_entry(
    entry_id: int,
    current_user: User = Depends(get_current_active_user),
    service: JournalService = Depends(get_journal_service)
):
    await service.delete_entry(current_user.id, entry_id)
