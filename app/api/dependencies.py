from typing import List
from fastapi import Depends
from fastapi.security import OAuth2PasswordBearer
from sqlalchemy.ext.asyncio import AsyncSession

from app.database.session import get_db
from app.exceptions.handlers import AuthenticationError, PermissionDeniedError
from app.models.auth import User
from app.repositories.auth_repo import UserRepository
from app.services.auth_service import JWTService

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="api/v1/auth/login")

async def get_current_user(
    token: str = Depends(oauth2_scheme),
    db: AsyncSession = Depends(get_db)
) -> User:
    jwt_service = JWTService()
    user_id = jwt_service.verify_access_token(token)

    user_repo = UserRepository(db)
    user = await user_repo.get_by_id(user_id)
    if not user:
        raise AuthenticationError("User session not found")
    return user

async def get_current_active_user(
    current_user: User = Depends(get_current_user)
) -> User:
    if not current_user.is_active:
        raise AuthenticationError("User account is inactive")
    return current_user

class RoleChecker:
    def __init__(self, allowed_roles: List[str]):
        self.allowed_roles = allowed_roles

    def __call__(self, current_user: User = Depends(get_current_active_user)) -> User:
        # Check current user role name
        if current_user.role.name not in self.allowed_roles:
            raise PermissionDeniedError("You do not have permission to access this resource")
        return current_user
