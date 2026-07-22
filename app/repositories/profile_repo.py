from typing import Optional, List
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from app.models.profile import UserProfile, EmergencyContact

class ProfileRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_user_id(self, user_id: int) -> Optional[UserProfile]:
        stmt = select(UserProfile).where(UserProfile.user_id == user_id, UserProfile.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def save(self, profile: UserProfile) -> UserProfile:
        self.db.add(profile)
        await self.db.flush()
        await self.db.refresh(profile)
        return profile

class EmergencyContactRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, contact_id: int) -> Optional[EmergencyContact]:
        stmt = select(EmergencyContact).where(EmergencyContact.id == contact_id, EmergencyContact.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_all_by_user_id(self, user_id: int) -> List[EmergencyContact]:
        stmt = select(EmergencyContact).where(EmergencyContact.user_id == user_id, EmergencyContact.is_deleted == False)
        result = await self.db.execute(stmt)
        return list(result.scalars().all())

    async def save(self, contact: EmergencyContact) -> EmergencyContact:
        self.db.add(contact)
        await self.db.flush()
        await self.db.refresh(contact)
        return contact
