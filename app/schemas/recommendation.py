from datetime import datetime
from pydantic import BaseModel, ConfigDict

class RecommendationBase(BaseModel):
    category: str
    title: str
    content: str
    priority_score: float

class RecommendationResponse(RecommendationBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    is_read: bool
    is_completed: bool
    created_at: datetime
    updated_at: datetime
