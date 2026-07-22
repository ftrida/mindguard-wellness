from datetime import datetime
from typing import Optional
from pydantic import BaseModel, Field, field_validator, ConfigDict

class MeditationSessionBase(BaseModel):
    category: str = Field("Mindfulness", max_length=100)
    duration_seconds: int = Field(..., ge=1, le=86400, description="Duration of session in seconds")

class MeditationSessionCreate(MeditationSessionBase):
    completed_at: Optional[datetime] = None

    @field_validator("completed_at")
    @classmethod
    def validate_completed_at(cls, v: Optional[datetime]) -> Optional[datetime]:
        if v and v > datetime.utcnow():
            raise ValueError("Meditation completed time cannot be in the future")
        return v

class MeditationSessionResponse(MeditationSessionBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    completed_at: datetime
    created_at: datetime
    updated_at: datetime
