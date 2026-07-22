from datetime import date, time, datetime
from typing import Optional
from pydantic import BaseModel, Field, model_validator, ConfigDict, field_validator

class DailyLifestyleLogBase(BaseModel):
    sleep_hours: float = Field(0.0, ge=0.0, le=24.0)
    wake_time: Optional[time] = None
    bed_time: Optional[time] = None
    water_intake: float = Field(0.0, ge=0.0, le=30.0, description="Water in Liters")
    exercise_minutes: int = Field(0, ge=0, le=1440)
    walking_steps: int = Field(0, ge=0, le=100000)
    screen_time: float = Field(0.0, ge=0.0, le=24.0)
    phone_unlock_count: int = Field(0, ge=0, le=2000)
    study_hours: float = Field(0.0, ge=0.0, le=24.0)
    work_hours: float = Field(0.0, ge=0.0, le=24.0)
    social_time: float = Field(0.0, ge=0.0, le=24.0)
    outdoor_time: float = Field(0.0, ge=0.0, le=24.0)
    energy_level: int = Field(5, ge=1, le=10)

    @model_validator(mode="after")
    def validate_total_hours(self) -> "DailyLifestyleLogBase":
        total_tracked_hours = (
            self.sleep_hours +
            self.screen_time +
            self.study_hours +
            self.work_hours +
            self.social_time +
            self.outdoor_time
        )
        if total_tracked_hours > 24.0:
            raise ValueError(f"Sum of daily tracked hours ({total_tracked_hours}h) cannot exceed 24 hours")
        return self

class DailyLifestyleLogCreate(DailyLifestyleLogBase):
    log_date: date

    @field_validator("log_date")
    @classmethod
    def validate_log_date(cls, v: date) -> date:
        if v > date.today():
            raise ValueError("Log date cannot be in the future")
        return v

class DailyLifestyleLogUpdate(BaseModel):
    sleep_hours: Optional[float] = Field(None, ge=0.0, le=24.0)
    wake_time: Optional[time] = None
    bed_time: Optional[time] = None
    water_intake: Optional[float] = Field(None, ge=0.0, le=30.0)
    exercise_minutes: Optional[int] = Field(None, ge=0, le=1440)
    walking_steps: Optional[int] = Field(None, ge=0, le=100000)
    screen_time: Optional[float] = Field(None, ge=0.0, le=24.0)
    phone_unlock_count: Optional[int] = Field(None, ge=0, le=2000)
    study_hours: Optional[float] = Field(None, ge=0.0, le=24.0)
    work_hours: Optional[float] = Field(None, ge=0.0, le=24.0)
    social_time: Optional[float] = Field(None, ge=0.0, le=24.0)
    outdoor_time: Optional[float] = Field(None, ge=0.0, le=24.0)
    energy_level: Optional[int] = Field(None, ge=1, le=10)

    @model_validator(mode="after")
    def validate_total_hours_update(self) -> "DailyLifestyleLogUpdate":
        # Values can be None, so we default to 0 for tracking check (since we don't have the DB state here,
        # we can only do a basic sanity check for the partial payload values themselves)
        h_sleep = self.sleep_hours or 0.0
        h_screen = self.screen_time or 0.0
        h_study = self.study_hours or 0.0
        h_work = self.work_hours or 0.0
        h_social = self.social_time or 0.0
        h_outdoor = self.outdoor_time or 0.0
        total_tracked_hours = h_sleep + h_screen + h_study + h_work + h_social + h_outdoor
        if total_tracked_hours > 24.0:
            raise ValueError(f"Sum of updated tracked hours ({total_tracked_hours}h) cannot exceed 24 hours")
        return self

class DailyLifestyleLogResponse(DailyLifestyleLogBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    log_date: date
    created_at: datetime
    updated_at: datetime
