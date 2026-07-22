from datetime import date, datetime, time, timedelta
from typing import List, Dict
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.meditation_repo import MeditationRepository
from app.repositories.focus_repo import FocusRepository
from app.schemas.analytics import (
    DailyTrendPoint,
    TrendAnalyticsResponse,
    SleepAnalyticsResponse,
    WaterAnalyticsResponse,
    ExerciseAnalyticsResponse,
    FocusAnalyticsResponse,
    MeditationAnalyticsResponse,
    JournalActivityResponse
)

class AnalyticsService:
    def __init__(
        self,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository,
        journal_repo: JournalRepository,
        meditation_repo: MeditationRepository,
        focus_repo: FocusRepository
    ):
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo
        self.journal_repo = journal_repo
        self.meditation_repo = meditation_repo
        self.focus_repo = focus_repo

    async def get_lifestyle_trends(self, user_id: int, metric: str, start_date: date, end_date: date) -> TrendAnalyticsResponse:
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        points = []
        
        for l in logs:
            val = 0.0
            if metric == "sleep":
                val = l.sleep_hours
            elif metric == "water":
                val = l.water_intake
            elif metric == "exercise":
                val = float(l.exercise_minutes)
            elif metric == "steps":
                val = float(l.walking_steps)
            elif metric == "screen":
                val = l.screen_time
            points.append(DailyTrendPoint(date=l.log_date, value=val))

        values = [p.value for p in points]
        return TrendAnalyticsResponse(
            metric=metric,
            points=points,
            average=round(sum(values) / len(values), 2) if values else 0.0,
            maximum=max(values) if values else 0.0,
            minimum=min(values) if values else 0.0
        )

    async def get_sleep_analytics(self, user_id: int, start_date: date, end_date: date) -> SleepAnalyticsResponse:
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        history = [DailyTrendPoint(date=l.log_date, value=l.sleep_hours) for l in logs]
        
        avg_hours = sum(l.sleep_hours for l in logs) / len(logs) if logs else 0.0
        
        # Consistency score: % of days with sleep between 7 and 9 hours inclusive
        consistent_days = sum(1 for l in logs if 7.0 <= l.sleep_hours <= 9.0)
        consistency = (consistent_days / len(logs)) * 100.0 if logs else 0.0

        return SleepAnalyticsResponse(
            average_hours=round(avg_hours, 2),
            consistency_score=round(consistency, 2),
            history=history
        )

    async def get_water_analytics(self, user_id: int, start_date: date, end_date: date) -> WaterAnalyticsResponse:
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        history = [DailyTrendPoint(date=l.log_date, value=l.water_intake) for l in logs]
        
        avg_liters = sum(l.water_intake for l in logs) / len(logs) if logs else 0.0
        target_met = sum(1 for l in logs if l.water_intake >= 2.0) # target: 2 liters

        return WaterAnalyticsResponse(
            average_liters=round(avg_liters, 2),
            target_met_count=target_met,
            history=history
        )

    async def get_exercise_analytics(self, user_id: int, start_date: date, end_date: date) -> ExerciseAnalyticsResponse:
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        history_steps = [DailyTrendPoint(date=l.log_date, value=float(l.walking_steps)) for l in logs]
        history_min = [DailyTrendPoint(date=l.log_date, value=float(l.exercise_minutes)) for l in logs]

        avg_exercise = sum(l.exercise_minutes for l in logs) / len(logs) if logs else 0.0
        total_steps = sum(l.walking_steps for l in logs)
        avg_steps = total_steps / len(logs) if logs else 0.0

        return ExerciseAnalyticsResponse(
            average_minutes=round(avg_exercise, 2),
            total_steps=total_steps,
            daily_average_steps=round(avg_steps, 2),
            history_steps=history_steps,
            history_minutes=history_min
        )

    async def get_focus_analytics(self, user_id: int, start_date: date, end_date: date) -> FocusAnalyticsResponse:
        start_dt = datetime.combine(start_date, time.min)
        end_dt = datetime.combine(end_date, time.max)
        sessions = await self.focus_repo.get_range(user_id, start_dt, end_dt)

        total_sec = sum(s.custom_duration_seconds * s.completed_sessions_count for s in sessions)
        total_sessions = sum(s.completed_sessions_count for s in sessions)

        # Generate daily history
        daily_map = {}
        for s in sessions:
            d = s.completed_at.date()
            daily_map[d] = daily_map.get(d, 0.0) + (s.custom_duration_seconds * s.completed_sessions_count / 60.0)
        
        history = [DailyTrendPoint(date=d, value=round(val, 2)) for d, val in sorted(daily_map.items())]

        return FocusAnalyticsResponse(
            total_focus_minutes=round(total_sec / 60.0, 2),
            sessions_completed=total_sessions,
            daily_history=history
        )

    async def get_meditation_analytics(self, user_id: int, start_date: date, end_date: date) -> MeditationAnalyticsResponse:
        start_dt = datetime.combine(start_date, time.min)
        end_dt = datetime.combine(end_date, time.max)
        sessions = await self.meditation_repo.get_range(user_id, start_dt, end_dt)

        total_sec = sum(s.duration_seconds for s in sessions)
        total_sessions = len(sessions)

        # Category distribution
        categories = {}
        for s in sessions:
            categories[s.category] = categories.get(s.category, 0) + 1

        # Generate daily history
        daily_map = {}
        for s in sessions:
            d = s.completed_at.date()
            daily_map[d] = daily_map.get(d, 0.0) + (s.duration_seconds / 60.0)

        history = [DailyTrendPoint(date=d, value=round(val, 2)) for d, val in sorted(daily_map.items())]

        return MeditationAnalyticsResponse(
            total_meditation_minutes=round(total_sec / 60.0, 2),
            sessions_completed=total_sessions,
            category_distribution=categories,
            daily_history=history
        )

    async def get_journal_activity(self, user_id: int, start_date: date, end_date: date) -> JournalActivityResponse:
        entries = await self.journal_repo.get_all_by_user_id(user_id)
        filtered = [e for e in entries if start_date <= e.created_at.date() <= end_date]

        categories = {}
        tags = {}
        weekly = {"Mon": 0, "Tue": 0, "Wed": 0, "Thu": 0, "Fri": 0, "Sat": 0, "Sun": 0}

        for e in filtered:
            # Categories
            categories[e.category] = categories.get(e.category, 0) + 1
            # Tags
            for t in e.tags:
                tags[t.name] = tags.get(t.name, 0) + 1
            # Weekly distribution (by day name abbreviation)
            day_name = e.created_at.strftime("%a")
            if day_name in weekly:
                weekly[day_name] += 1

        return JournalActivityResponse(
            total_entries=len(filtered),
            category_distribution=categories,
            tag_cloud=tags,
            weekly_frequency=weekly
        )
