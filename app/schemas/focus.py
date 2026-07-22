from datetime import datetime
from typing import Optional
from pydantic import BaseModel, Field, field_validator, ConfigDict

class FocusSessionBase(BaseModel):
    custom_duration_seconds: int = Field(..., ge=1, le=86400, description="Session duration in seconds")
    break_duration_seconds: int = Field(0, ge=0, le=86400, description="Break duration in seconds")
    completed_sessions_count: int = Field(1, ge=1, le=100, description="Number of completed cycles")

class FocusSessionCreate(FocusSessionBase):
    completed_at: Optional[datetime] = None

    @field_validator("completed_at")
    @classmethod
    def validate_completed_at(cls, v: Optional[datetime]) -> Optional[datetime]:
        if v and v > datetime.utcnow():
            raise ValueError("Focus session completed time cannot be in the future")
        return v

class FocusSessionResponse(FocusSessionBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    completed_at: datetime
    created_at: datetime
    updated_at: datetime
