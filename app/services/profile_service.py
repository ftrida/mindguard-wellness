from typing import Optional, List
from fastapi import HTTPException, status
from app.models.profile import UserProfile, EmergencyContact
from app.repositories.profile_repo import ProfileRepository, EmergencyContactRepository
from app.schemas.profile import UserProfileCreate, UserProfileUpdate, EmergencyContactCreate, EmergencyContactUpdate

class ProfileService:
    def __init__(self, profile_repo: ProfileRepository, emergency_repo: EmergencyContactRepository):
        self.profile_repo = profile_repo
        self.emergency_repo = emergency_repo

    async def get_profile(self, user_id: int) -> UserProfile:
        profile = await self.profile_repo.get_by_user_id(user_id)
        if not profile:
            # Auto-create empty profile for new users
            profile = UserProfile(user_id=user_id, timezone="UTC", language="en")
            profile = await self.profile_repo.save(profile)
        return profile

    async def update_profile(self, user_id: int, update_data: UserProfileUpdate) -> UserProfile:
        profile = await self.get_profile(user_id)
        for field, value in update_data.model_dump(exclude_unset=True).items():
            setattr(profile, field, value)
        return await self.profile_repo.save(profile)

    async def get_emergency_contacts(self, user_id: int) -> List[EmergencyContact]:
        return await self.emergency_repo.get_all_by_user_id(user_id)

    async def create_emergency_contact(self, user_id: int, contact_data: EmergencyContactCreate) -> EmergencyContact:
        contacts = await self.emergency_repo.get_all_by_user_id(user_id)
        
        # If it is the first contact or marked as primary, handle resets
        is_primary = contact_data.is_primary or len(contacts) == 0
        if is_primary:
            for c in contacts:
                if c.is_primary:
                    c.is_primary = False
                    await self.emergency_repo.save(c)

        contact = EmergencyContact(
            user_id=user_id,
            name=contact_data.name,
            relationship=contact_data.relationship,
            phone=contact_data.phone,
            is_primary=is_primary
        )
        return await self.emergency_repo.save(contact)

    async def update_emergency_contact(self, user_id: int, contact_id: int, update_data: EmergencyContactUpdate) -> EmergencyContact:
        contact = await self.emergency_repo.get_by_id(contact_id)
        if not contact or contact.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Emergency contact not found")

        # If marking this one as primary, unset other primary contacts
        if update_data.is_primary:
            contacts = await self.emergency_repo.get_all_by_user_id(user_id)
            for c in contacts:
                if c.id != contact.id and c.is_primary:
                    c.is_primary = False
                    await self.emergency_repo.save(c)

        for field, value in update_data.model_dump(exclude_unset=True).items():
            setattr(contact, field, value)
        return await self.emergency_repo.save(contact)

    async def delete_emergency_contact(self, user_id: int, contact_id: int) -> None:
        contact = await self.emergency_repo.get_by_id(contact_id)
        if not contact or contact.user_id != user_id:
            raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Emergency contact not found")
        
        contact.is_deleted = True
        await self.emergency_repo.save(contact)
