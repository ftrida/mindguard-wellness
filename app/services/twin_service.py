from datetime import date, timedelta
from typing import Optional, List, Dict, Any
from app.repositories.twin_repo import TwinRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.models.twin import DigitalLifestyleTwin
from app.schemas.twin import TwinComparisonResponse

class TwinService:
    def __init__(
        self,
        twin_repo: TwinRepository,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository
    ):
        self.twin_repo = twin_repo
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo

    async def get_latest_twin(self, user_id: int) -> Optional[DigitalLifestyleTwin]:
        return await self.twin_repo.get_latest_by_user_id(user_id)

    async def get_history(self, user_id: int, limit: int = 30) -> List[DigitalLifestyleTwin]:
        return await self.twin_repo.get_history(user_id, limit)

    async def calculate_and_save_twin(self, user_id: int, snapshot_date: date) -> DigitalLifestyleTwin:
        # Fetch lifestyle logs for past 30 days
        start_dt = snapshot_date - timedelta(days=30)
        logs = await self.lifestyle_repo.get_range(user_id, start_dt, snapshot_date)
        
        # Default baselines
        sleep_b = 7.5
        activity_b = 30.0
        screen_b = 4.0
        mood_b = 7.0

        if logs:
            sleep_vals = [log.sleep_hours for log in logs if log.sleep_hours is not None]
            activity_vals = [log.exercise_minutes for log in logs if log.exercise_minutes is not None]
            screen_vals = [log.screen_time for log in logs if log.screen_time is not None]
            if sleep_vals:
                sleep_b = sum(sleep_vals) / len(sleep_vals)
            if activity_vals:
                activity_b = sum(activity_vals) / len(activity_vals)
            if screen_vals:
                screen_b = sum(screen_vals) / len(screen_vals)

        # Fetch mood entries for past 30 days
        # We don't have a direct date range filter in MoodRepository, but we can get logs
        # and extract entries. Let's fetch all mood entries using get_range or get_by_date.
        # Wait, mood_repo has get_range (which returns List[MoodEntry] for a datetime range).
        # Let's import datetime
        from datetime import datetime, time
        start_datetime = datetime.combine(start_dt, time.min)
        end_datetime = datetime.combine(snapshot_date, time.max)
        moods = await self.mood_repo.get_range(user_id, start_datetime, end_datetime)
        if moods:
            mood_b = sum(m.mood_score for m in moods) / len(moods)

        # Calculate wellness score
        # Sleep: 30%, Exercise: 20%, Screen: 20%, Mood: 30%
        sleep_contrib = min(sleep_b / 8.0, 1.2) * 30
        activity_contrib = min(activity_b / 45.0, 1.2) * 20
        screen_contrib = max(1.0 - (screen_b / 12.0), 0.0) * 20
        mood_contrib = (mood_b / 10.0) * 30
        
        wellness_score = min(max(sleep_contrib + activity_contrib + screen_contrib + mood_contrib, 0.0), 100.0)

        # Check if a snapshot for today already exists to update or increment version
        existing = await self.twin_repo.get_by_date(user_id, snapshot_date)
        version = 1
        if existing:
            version = existing.version + 1

        twin = DigitalLifestyleTwin(
            user_id=user_id,
            snapshot_date=snapshot_date,
            sleep_baseline=round(sleep_b, 2),
            activity_baseline=round(activity_b, 2),
            screen_baseline=round(screen_b, 2),
            mood_baseline=round(mood_b, 2),
            wellness_score=round(wellness_score, 2),
            version=version
        )
        return await self.twin_repo.save(twin)

    async def get_snapshot_averages(self, user_id: int, days: int) -> Dict[str, float]:
        end_date = date.today()
        start_date = end_date - timedelta(days=days)
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        
        avg_metrics = {
            "sleep_hours": 0.0,
            "water_intake": 0.0,
            "exercise_minutes": 0.0,
            "screen_time": 0.0,
            "mood_score": 0.0
        }
        
        if not logs:
            return avg_metrics

        sleeps = [l.sleep_hours for l in logs if l.sleep_hours is not None]
        waters = [l.water_intake for l in logs if l.water_intake is not None]
        exercises = [l.exercise_minutes for l in logs if l.exercise_minutes is not None]
        screens = [l.screen_time for l in logs if l.screen_time is not None]

        if sleeps:
            avg_metrics["sleep_hours"] = sum(sleeps) / len(sleeps)
        if waters:
            avg_metrics["water_intake"] = sum(waters) / len(waters)
        if exercises:
            avg_metrics["exercise_minutes"] = sum(exercises) / len(exercises)
        if screens:
            avg_metrics["screen_time"] = sum(screens) / len(screens)

        # Average Mood
        from datetime import datetime, time
        start_dt = datetime.combine(start_date, time.min)
        end_dt = datetime.combine(end_date, time.max)
        moods = await self.mood_repo.get_range(user_id, start_dt, end_dt)
        if moods:
            avg_metrics["mood_score"] = sum(m.mood_score for m in moods) / len(moods)

        return {k: round(v, 2) for k, v in avg_metrics.items()}

    async def compare_twin(self, user_id: int, snapshot_date: date) -> TwinComparisonResponse:
        twin = await self.twin_repo.get_latest_by_user_id(user_id)
        if not twin:
            # Create a base twin log
            twin = await self.calculate_and_save_twin(user_id, snapshot_date)

        # Fetch today's log
        today_log = await self.lifestyle_repo.get_by_date(user_id, snapshot_date)
        current = {
            "sleep_hours": today_log.sleep_hours if today_log else 0.0,
            "exercise_minutes": today_log.exercise_minutes if today_log else 0.0,
            "screen_time": today_log.screen_time if today_log else 0.0,
            "mood_score": 0.0
        }

        # Today's mood
        from datetime import datetime, time
        start_dt = datetime.combine(snapshot_date, time.min)
        end_dt = datetime.combine(snapshot_date, time.max)
        moods = await self.mood_repo.get_range(user_id, start_dt, end_dt)
        if moods:
            current["mood_score"] = sum(m.mood_score for m in moods) / len(moods)

        baselines = {
            "sleep_hours": twin.sleep_baseline,
            "exercise_minutes": twin.activity_baseline,
            "screen_time": twin.screen_baseline,
            "mood_score": twin.mood_baseline
        }

        deviations = {
            "sleep_hours": round(current["sleep_hours"] - baselines["sleep_hours"], 2),
            "exercise_minutes": round(current["exercise_minutes"] - baselines["exercise_minutes"], 2),
            "screen_time": round(current["screen_time"] - baselines["screen_time"], 2),
            "mood_score": round(current["mood_score"] - baselines["mood_score"], 2)
        }

        return TwinComparisonResponse(
            snapshot_date=snapshot_date,
            wellness_score=twin.wellness_score,
            current_metrics=current,
            baseline_metrics=baselines,
            deviations=deviations
        )
