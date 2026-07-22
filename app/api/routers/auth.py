from fastapi import APIRouter, Depends, Request, Response, status
from sqlalchemy.ext.asyncio import AsyncSession

from app.api.dependencies import get_current_active_user, get_db
from app.models.auth import User
from app.schemas.auth import (
    RegisterRequest,
    LoginRequest,
    RefreshRequest,
    TokenResponse,
    ProfileResponse,
    ForgotPasswordRequest,
    ResetPasswordRequest,
    VerifyEmailRequest,
    ProfileUpdateRequest
)
from app.services.auth_service import AuthenticationService, UserService

router = APIRouter(prefix="/auth", tags=["Authentication"])

@router.post("/register", response_model=ProfileResponse, status_code=status.HTTP_201_CREATED)
async def register(
    data: RegisterRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    user = await auth_service.register(
        email=data.email,
        username=data.username,
        password=data.password,
        phone=data.phone
    )
    return user

@router.post("/login", response_model=TokenResponse)
async def login(
    request: Request,
    data: LoginRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    user_agent = request.headers.get("User-Agent")
    ip_address = request.client.host if request.client else None

    user, access_token, refresh_token = await auth_service.login(
        email_or_username=data.email_or_username,
        password=data.password,
        ip_address=ip_address,
        user_agent=user_agent
    )
    # 30 minutes access token lifetime, returned as JSON response parameters
    return TokenResponse(
        access_token=access_token,
        refresh_token=refresh_token
    )

@router.post("/refresh", response_model=TokenResponse)
async def refresh(
    data: RefreshRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    new_access, new_refresh = await auth_service.refresh(data.refresh_token)
    return TokenResponse(
        access_token=new_access,
        refresh_token=new_refresh
    )

@router.post("/logout", status_code=status.HTTP_200_OK)
async def logout(
    data: RefreshRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    await auth_service.logout(data.refresh_token)
    return {"detail": "Successfully logged out"}

@router.post("/logout-all", status_code=status.HTTP_200_OK)
async def logout_all(
    current_user: User = Depends(get_current_active_user),
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    await auth_service.logout_all(current_user.id)
    return {"detail": "Successfully logged out of all devices"}

@router.post("/forgot-password", status_code=status.HTTP_200_OK)
async def forgot_password(
    data: ForgotPasswordRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    await auth_service.forgot_password(data.email)
    return {"detail": "Password reset instructions sent if email exists"}

@router.post("/reset-password", status_code=status.HTTP_200_OK)
async def reset_password(
    data: ResetPasswordRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    await auth_service.reset_password(data.token, data.new_password)
    return {"detail": "Password reset successfully"}

@router.post("/verify-email", status_code=status.HTTP_200_OK)
async def verify_email(
    data: VerifyEmailRequest,
    db: AsyncSession = Depends(get_db)
):
    auth_service = AuthenticationService(db)
    await auth_service.verify_email(data.token)
    return {"detail": "Email verified successfully"}

@router.get("/me", response_model=ProfileResponse)
async def get_me(
    current_user: User = Depends(get_current_active_user)
):
    return current_user

@router.patch("/profile", response_model=ProfileResponse)
async def update_profile(
    data: ProfileUpdateRequest,
    current_user: User = Depends(get_current_active_user),
    db: AsyncSession = Depends(get_db)
):
    user_service = UserService(db)
    updated_user = await user_service.update_profile(
        user_id=current_user.id,
        username=data.username,
        phone=data.phone
    )
    return updated_user

@router.delete("/account", status_code=status.HTTP_200_OK)
async def delete_account(
    current_user: User = Depends(get_current_active_user),
    db: AsyncSession = Depends(get_db)
):
    user_service = UserService(db)
    await user_service.delete_account(current_user.id)
    return {"detail": "Account successfully scheduled for deletion"}
