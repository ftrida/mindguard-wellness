from datetime import datetime
from typing import Optional, List, Dict, Any
from pydantic import BaseModel, Field, field_validator, ConfigDict

MOOD_CATEGORIES = ["Calm", "Happy", "Energetic", "Sad", "Anxious", "Angry", "Tired", "Stressed"]

class MoodEntryBase(BaseModel):
    mood_score: int = Field(..., ge=1, le=10, description="Mood score from 1 (very low) to 10 (very high)")
    notes: Optional[str] = None
    category: str = Field(..., max_length=50)

    @field_validator("category")
    @classmethod
    def validate_category(cls, v: str) -> str:
        if v not in MOOD_CATEGORIES:
            raise ValueError(f"Category must be one of {MOOD_CATEGORIES}")
        return v

class MoodEntryCreate(MoodEntryBase):
    entry_time: Optional[datetime] = None

    @field_validator("entry_time")
    @classmethod
    def validate_entry_time(cls, v: Optional[datetime]) -> Optional[datetime]:
        if v and v > datetime.utcnow():
            raise ValueError("Mood entry time cannot be in the future")
        return v

class MoodEntryUpdate(BaseModel):
    mood_score: Optional[int] = Field(None, ge=1, le=10)
    notes: Optional[str] = None
    category: Optional[str] = Field(None, max_length=50)

    @field_validator("category")
    @classmethod
    def validate_category(cls, v: Optional[str]) -> Optional[str]:
        if v is None:
            return v
        if v not in MOOD_CATEGORIES:
            raise ValueError(f"Category must be one of {MOOD_CATEGORIES}")
        return v

class MoodEntryResponse(MoodEntryBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    entry_time: datetime
    created_at: datetime
    updated_at: datetime

# Stats and trends response structures
class MoodStatsResponse(BaseModel):
    average_score: float
    entries_count: int
    category_distribution: Dict[str, int]
    highest_score: int
    lowest_score: int
