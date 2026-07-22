from datetime import date
from typing import List, Dict, Optional
from pydantic import BaseModel

class DailyTrendPoint(BaseModel):
    date: date
    value: float

class TrendAnalyticsResponse(BaseModel):
    metric: str
    points: List[DailyTrendPoint]
    average: float
    maximum: float
    minimum: float

class SleepAnalyticsResponse(BaseModel):
    average_hours: float
    consistency_score: float # Percentage of days with 7-9 hours of sleep
    history: List[DailyTrendPoint]

class WaterAnalyticsResponse(BaseModel):
    average_liters: float
    target_met_count: int # Days with >= 2.0 liters
    history: List[DailyTrendPoint]

class ExerciseAnalyticsResponse(BaseModel):
    average_minutes: float
    total_steps: int
    daily_average_steps: float
    history_steps: List[DailyTrendPoint]
    history_minutes: List[DailyTrendPoint]

class FocusAnalyticsResponse(BaseModel):
    total_focus_minutes: float
    sessions_completed: int
    daily_history: List[DailyTrendPoint]

class MeditationAnalyticsResponse(BaseModel):
    total_meditation_minutes: float
    sessions_completed: int
    category_distribution: Dict[str, int]
    daily_history: List[DailyTrendPoint]

class JournalActivityResponse(BaseModel):
    total_entries: int
    category_distribution: Dict[str, int]
    tag_cloud: Dict[str, int]
    weekly_frequency: Dict[str, int]
