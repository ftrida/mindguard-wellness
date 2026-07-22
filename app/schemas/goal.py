from datetime import date, datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, Field

class GoalBase(BaseModel):
    title: str = Field(..., max_length=200)
    category: str = Field(..., max_length=50)
    target_value: float = Field(..., gt=0.0)
    start_date: date
    target_date: date

class GoalCreate(GoalBase):
    pass

class GoalUpdate(BaseModel):
    title: Optional[str] = None
    current_value: Optional[float] = None
    status: Optional[str] = None # "active", "completed", "failed"

class GoalResponse(GoalBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    current_value: float
    status: str
    created_at: datetime
    updated_at: datetime
