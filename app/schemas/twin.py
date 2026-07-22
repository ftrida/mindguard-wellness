from datetime import date, datetime
from pydantic import BaseModel, ConfigDict

class DigitalLifestyleTwinBase(BaseModel):
    snapshot_date: date
    sleep_baseline: float
    activity_baseline: float
    screen_baseline: float
    mood_baseline: float
    wellness_score: float
    version: int = 1

class DigitalLifestyleTwinResponse(DigitalLifestyleTwinBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime

class TwinComparisonResponse(BaseModel):
    snapshot_date: date
    wellness_score: float
    current_metrics: dict
    baseline_metrics: dict
    deviations: dict
