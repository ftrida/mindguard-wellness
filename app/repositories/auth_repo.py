from typing import Optional, List
from datetime import datetime
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.future import select
from sqlalchemy import update
from sqlalchemy.orm import selectinload
from app.models.auth import User, Role, RefreshToken, PasswordResetToken, EmailVerification

class UserRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_id(self, user_id: int) -> Optional[User]:
        stmt = select(User).options(selectinload(User.role)).where(User.id == user_id, User.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_by_email(self, email: str) -> Optional[User]:
        stmt = select(User).options(selectinload(User.role)).where(User.email == email, User.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def get_by_username(self, username: str) -> Optional[User]:
        stmt = select(User).options(selectinload(User.role)).where(User.username == username, User.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def create(self, user: User) -> User:
        self.db.add(user)
        await self.db.flush()
        stmt = select(User).options(selectinload(User.role)).where(User.id == user.id)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def save(self, user: User) -> User:
        self.db.add(user)
        await self.db.flush()
        stmt = select(User).options(selectinload(User.role)).where(User.id == user.id)
        result = await self.db.execute(stmt)
        return result.scalars().first()

class RoleRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    async def get_by_name(self, name: str) -> Optional[Role]:
        stmt = select(Role).where(Role.name == name, Role.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def create(self, role: Role) -> Role:
        self.db.add(role)
        await self.db.flush()
        return role

class TokenRepository:
    def __init__(self, db: AsyncSession):
        self.db = db

    # Refresh Tokens
    async def create_refresh_token(self, refresh_token: RefreshToken) -> RefreshToken:
        self.db.add(refresh_token)
        await self.db.flush()
        return refresh_token

    async def get_refresh_token(self, token: str) -> Optional[RefreshToken]:
        stmt = select(RefreshToken).where(RefreshToken.token == token, RefreshToken.is_revoked == False, RefreshToken.is_deleted == False)
        result = await self.db.execute(stmt)
        return result.scalars().first()

    async def revoke_refresh_token(self, token: str) -> None:
        stmt = update(RefreshToken).where(RefreshToken.token == token).values(is_revoked=True)
        await self.db.execute(stmt)

    async def revoke_all_user_tokens(self, user_id: int) -> None:
        stmt = update(RefreshToken).where(RefreshToken.user_id == user_id).values(is_revoked=True)
        await self.db.execute(stmt)

    # Password Reset Tokens
    async def create_password_reset_token(self, reset_token: PasswordResetToken) -> PasswordResetToken:
        self.db.add(reset_token)
        await self.db.flush()
        return reset_token

    async def get_password_reset_token(self, token: str) -> Optional[PasswordResetToken]:
        stmt = select(PasswordResetToken).where(
            PasswordResetToken.token == token,
            PasswordResetToken.is_used == False,
            PasswordResetToken.expires_at > datetime.utcnow(),
            PasswordResetToken.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()

    # Email Verification Tokens
    async def create_email_verification(self, verification: EmailVerification) -> EmailVerification:
        self.db.add(verification)
        await self.db.flush()
        return verification

    async def get_email_verification(self, token: str) -> Optional[EmailVerification]:
        stmt = select(EmailVerification).where(
            EmailVerification.token == token,
            EmailVerification.is_verified == False,
            EmailVerification.expires_at > datetime.utcnow(),
            EmailVerification.is_deleted == False
        )
        result = await self.db.execute(stmt)
        return result.scalars().first()
