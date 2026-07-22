from typing import List
from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.profile_repo import ProfileRepository, EmergencyContactRepository
from app.services.profile_service import ProfileService
from app.schemas.profile import (
    UserProfileResponse,
    UserProfileUpdate,
    EmergencyContactResponse,
    EmergencyContactCreate,
    EmergencyContactUpdate
)

router = APIRouter(prefix="/profile", tags=["User Profile"])

async def get_profile_service(db: AsyncSession = Depends(get_db)) -> ProfileService:
    profile_repo = ProfileRepository(db)
    emergency_repo = EmergencyContactRepository(db)
    return ProfileService(profile_repo, emergency_repo)

@router.get("", response_model=UserProfileResponse)
async def get_profile(
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    return await service.get_profile(current_user.id)

@router.put("", response_model=UserProfileResponse)
async def update_profile(
    update_data: UserProfileUpdate,
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    return await service.update_profile(current_user.id, update_data)

@router.get("/emergency-contacts", response_model=List[EmergencyContactResponse])
async def get_emergency_contacts(
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    return await service.get_emergency_contacts(current_user.id)

@router.post("/emergency-contacts", response_model=EmergencyContactResponse, status_code=status.HTTP_201_CREATED)
async def create_emergency_contact(
    contact_data: EmergencyContactCreate,
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    return await service.create_emergency_contact(current_user.id, contact_data)

@router.put("/emergency-contacts/{contact_id}", response_model=EmergencyContactResponse)
async def update_emergency_contact(
    contact_id: int,
    update_data: EmergencyContactUpdate,
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    return await service.update_emergency_contact(current_user.id, contact_id, update_data)

@router.delete("/emergency-contacts/{contact_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_emergency_contact(
    contact_id: int,
    current_user: User = Depends(get_current_active_user),
    service: ProfileService = Depends(get_profile_service)
):
    await service.delete_emergency_contact(current_user.id, contact_id)
