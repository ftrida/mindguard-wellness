from datetime import date, datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict

class BehaviorLogBase(BaseModel):
    analysis_date: date
    drift_score: float
    consistency_score: float
    lifestyle_change_index: float
    risk_indicators: Optional[str] = None # JSON string
    explanation: Optional[str] = None

class BehaviorLogResponse(BehaviorLogBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime
