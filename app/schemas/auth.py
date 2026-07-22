import re
from typing import Any, Optional
from datetime import datetime
from pydantic import BaseModel, EmailStr, Field, field_validator, ConfigDict

class RegisterRequest(BaseModel):
    email: EmailStr
    username: str = Field(..., min_length=3, max_length=50)
    password: str = Field(..., min_length=8)
    phone: Optional[str] = Field(None, description="Optional phone number")

    @field_validator("username")
    @classmethod
    def validate_username(cls, v: str) -> str:
        if not re.match(r"^[a-zA-Z0-9_-]+$", v):
            raise ValueError("Username must contain only letters, numbers, underscores, or hyphens")
        return v

    @field_validator("password")
    @classmethod
    def validate_password(cls, v: str) -> str:
        if not re.search(r"[A-Z]", v):
            raise ValueError("Password must contain at least one uppercase letter")
        if not re.search(r"[a-z]", v):
            raise ValueError("Password must contain at least one lowercase letter")
        if not re.search(r"\d", v):
            raise ValueError("Password must contain at least one digit")
        if not re.search(r"[!@#$%^&*(),.?\":{}|<>]", v):
            raise ValueError("Password must contain at least one special character")
        return v

class LoginRequest(BaseModel):
    email_or_username: str = Field(..., min_length=3)
    password: str = Field(..., min_length=8)

class RefreshRequest(BaseModel):
    refresh_token: str

class TokenResponse(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str = "bearer"
    expires_in: int = 1800 # 30 minutes in seconds

class ProfileResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    email: EmailStr
    username: str
    phone: Optional[str] = None
    is_active: bool
    is_verified: bool
    role: str
    created_at: datetime
    updated_at: datetime

    @field_validator("role", mode="before")
    @classmethod
    def get_role_name(cls, v: Any) -> str:
        if hasattr(v, "name"):
            return v.name
        return str(v)

class ForgotPasswordRequest(BaseModel):
    email: EmailStr

class ResetPasswordRequest(BaseModel):
    token: str
    new_password: str = Field(..., min_length=8)

    @field_validator("new_password")
    @classmethod
    def validate_new_password(cls, v: str) -> str:
        if not re.search(r"[A-Z]", v):
            raise ValueError("Password must contain at least one uppercase letter")
        if not re.search(r"[a-z]", v):
            raise ValueError("Password must contain at least one lowercase letter")
        if not re.search(r"\d", v):
            raise ValueError("Password must contain at least one digit")
        if not re.search(r"[!@#$%^&*(),.?\":{}|<>]", v):
            raise ValueError("Password must contain at least one special character")
        return v

class VerifyEmailRequest(BaseModel):
    token: str

class ProfileUpdateRequest(BaseModel):
    phone: Optional[str] = Field(None, description="Update phone number")
    username: Optional[str] = Field(None, min_length=3, max_length=50)

    @field_validator("username")
    @classmethod
    def validate_username(cls, v: Optional[str]) -> Optional[str]:
        if v is not None and not re.match(r"^[a-zA-Z0-9_-]+$", v):
            raise ValueError("Username must contain only letters, numbers, underscores, or hyphens")
        return v
