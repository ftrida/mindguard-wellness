from datetime import datetime
from typing import List, Optional
from pydantic import BaseModel, ConfigDict

class CoachMessageCreate(BaseModel):
    content: str

class CoachMessageResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    role: str
    content: str
    created_at: datetime

class CoachChatResponse(BaseModel):
    reply: str
    memory: List[CoachMessageResponse]
    context_used: Optional[dict] = None

class CoachAdviceResponse(BaseModel):
    daily_advice: str
    weekly_advice: str
    monthly_advice: str
    lifestyle_suggestions: List[str]
    motivation_messages: List[str]
    goal_suggestions: List[str]
