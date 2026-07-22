from datetime import date, datetime
from typing import Optional
from pydantic import BaseModel, Field, field_validator, ConfigDict

class EmergencyContactBase(BaseModel):
    name: str = Field(..., min_length=1, max_length=100)
    relationship: str = Field(..., min_length=1, max_length=50)
    phone: str = Field(..., min_length=5, max_length=20)
    is_primary: bool = False

    @field_validator("phone")
    @classmethod
    def validate_phone(cls, v: str) -> str:
        # Allow only digits, spaces, hyphens, plus sign
        import re
        if not re.match(r"^\+?[\d\s\-]+$", v):
            raise ValueError("Phone number must contain only digits, spaces, hyphens, or a leading '+'")
        return v

class EmergencyContactCreate(EmergencyContactBase):
    pass

class EmergencyContactUpdate(BaseModel):
    name: Optional[str] = Field(None, min_length=1, max_length=100)
    relationship: Optional[str] = Field(None, min_length=1, max_length=50)
    phone: Optional[str] = Field(None, min_length=5, max_length=20)
    is_primary: Optional[bool] = None

    @field_validator("phone")
    @classmethod
    def validate_phone(cls, v: Optional[str]) -> Optional[str]:
        if v is None:
            return v
        import re
        if not re.match(r"^\+?[\d\s\-]+$", v):
            raise ValueError("Phone number must contain only digits, spaces, hyphens, or a leading '+'")
        return v

class EmergencyContactResponse(EmergencyContactBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime

class UserProfileBase(BaseModel):
    profile_photo_url: Optional[str] = Field(None, max_length=500)
    dob: Optional[date] = None
    gender: Optional[str] = Field(None, max_length=20)
    height: Optional[float] = Field(None, ge=30.0, le=300.0, description="Height in cm")
    weight: Optional[float] = Field(None, ge=10.0, le=600.0, description="Weight in kg")
    timezone: str = Field("UTC", max_length=100)
    country: Optional[str] = Field(None, max_length=100)
    language: str = Field("en", max_length=10)
    occupation: Optional[str] = Field(None, max_length=100)
    emergency_preferences: Optional[str] = None

    @field_validator("dob")
    @classmethod
    def validate_dob(cls, v: Optional[date]) -> Optional[date]:
        if v and v >= date.today():
            raise ValueError("Date of birth must be in the past")
        return v

class UserProfileCreate(UserProfileBase):
    pass

class UserProfileUpdate(BaseModel):
    profile_photo_url: Optional[str] = Field(None, max_length=500)
    dob: Optional[date] = None
    gender: Optional[str] = Field(None, max_length=20)
    height: Optional[float] = Field(None, ge=30.0, le=300.0)
    weight: Optional[float] = Field(None, ge=10.0, le=600.0)
    timezone: Optional[str] = Field(None, max_length=100)
    country: Optional[str] = Field(None, max_length=100)
    language: Optional[str] = Field(None, max_length=10)
    occupation: Optional[str] = Field(None, max_length=100)
    emergency_preferences: Optional[str] = None

    @field_validator("dob")
    @classmethod
    def validate_dob(cls, v: Optional[date]) -> Optional[date]:
        if v and v >= date.today():
            raise ValueError("Date of birth must be in the past")
        return v

class UserProfileResponse(UserProfileBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime
