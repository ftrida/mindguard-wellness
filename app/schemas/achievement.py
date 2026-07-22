from datetime import datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict

class AchievementResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    badge_name: str
    badge_type: str
    description: str
    created_at: datetime

class StreakResponse(BaseModel):
    current_streak: int
    longest_streak: int
    last_log_date: Optional[datetime] = None
