from datetime import date, datetime
from typing import Optional
from pydantic import BaseModel, ConfigDict, Field

class StressLikelihoodBase(BaseModel):
    assessment_date: date
    stress_score: float = Field(..., ge=0.0, le=100.0)
    confidence_score: float = Field(..., ge=0.0, le=1.0)
    contributing_factors: Optional[str] = None # JSON string
    recommendations: Optional[str] = None # JSON string

class StressLikelihoodResponse(StressLikelihoodBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime
