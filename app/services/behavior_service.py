import json
import numpy as np
import pandas as pd
from datetime import date, timedelta
from typing import Optional, List, Dict, Any
from app.repositories.behavior_repo import BehaviorRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.services.twin_service import TwinService
from app.models.behavior import BehaviorLog
from app.schemas.behavior import BehaviorLogResponse

class BehaviorService:
    def __init__(
        self,
        behavior_repo: BehaviorRepository,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository,
        twin_service: TwinService
    ):
        self.behavior_repo = behavior_repo
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo
        self.twin_service = twin_service

    async def get_latest_analysis(self, user_id: int) -> Optional[BehaviorLog]:
        return await self.behavior_repo.get_latest_by_user_id(user_id)

    async def get_history(self, user_id: int, start_date: date, end_date: date) -> List[BehaviorLog]:
        return await self.behavior_repo.get_range(user_id, start_date, end_date)

    async def analyze_behavior_drift(self, user_id: int, analysis_date: date) -> BehaviorLog:
        # Get Latest Digital Lifestyle Twin for baselines
        twin = await self.twin_service.get_latest_twin(user_id)
        if not twin:
            # Generate twin baseline
            twin = await self.twin_service.calculate_and_save_twin(user_id, analysis_date)

        # Get past 7 days of lifestyle logs to measure drift
        start_date = analysis_date - timedelta(days=7)
        logs = await self.lifestyle_repo.get_range(user_id, start_date, analysis_date)

        # Default values if no logs
        drift_score = 0.0
        consistency_score = 100.0
        lifestyle_change_index = 0.0
        risk_indicators = []
        explanation_items = []

        if logs:
            # Convert to Pandas DataFrame for analysis
            data = []
            for log in logs:
                data.append({
                    "sleep": log.sleep_hours or 0.0,
                    "exercise": log.exercise_minutes or 0.0,
                    "screen": log.screen_time or 0.0
                })
            df = pd.DataFrame(data)

            # Averages over last 7 days
            mean_sleep = df["sleep"].mean()
            mean_exercise = df["exercise"].mean()
            mean_screen = df["screen"].mean()

            # Std deviations to compute Consistency Score
            # consistency = 100 - (mean(std_deviations) * scale)
            std_sleep = df["sleep"].std() if len(df) > 1 else 0.0
            std_exercise = df["exercise"].std() if len(df) > 1 else 0.0
            
            # Replace NaNs with 0.0
            std_sleep = 0.0 if np.isnan(std_sleep) else std_sleep
            std_exercise = 0.0 if np.isnan(std_exercise) else std_exercise

            # Normalize consistency
            avg_std = (std_sleep + (std_exercise / 10.0)) / 2.0
            consistency_score = max(0.0, min(100.0, 100.0 - (avg_std * 15.0)))

            # Calculate drift against baseline
            # drift = mean(|current_mean - baseline| / (baseline or 1))
            baselines = {
                "sleep": twin.sleep_baseline or 7.5,
                "exercise": twin.activity_baseline or 30.0,
                "screen": twin.screen_baseline or 4.0
            }

            sleep_drift = abs(mean_sleep - baselines["sleep"]) / max(baselines["sleep"], 1.0)
            exercise_drift = abs(mean_exercise - baselines["exercise"]) / max(baselines["exercise"], 1.0)
            screen_drift = abs(mean_screen - baselines["screen"]) / max(baselines["screen"], 1.0)

            drift_score = ((sleep_drift + exercise_drift + screen_drift) / 3.0) * 100.0
            drift_score = min(max(drift_score, 0.0), 100.0)

            # Change index (absolute difference sum)
            lifestyle_change_index = (abs(mean_sleep - baselines["sleep"]) + 
                                     (abs(mean_exercise - baselines["exercise"]) / 10.0) + 
                                     abs(mean_screen - baselines["screen"]))
            lifestyle_change_index = min(max(lifestyle_change_index * 10.0, 0.0), 100.0)

            # Generate risk indicators and explanations
            if mean_sleep < baselines["sleep"] - 1.5:
                risk_indicators.append("Significant Sleep Deprivation")
                explanation_items.append(f"Sleep duration has dropped by {round(baselines['sleep'] - mean_sleep, 1)} hours below your baseline.")
            if mean_screen > baselines["screen"] + 2.0:
                risk_indicators.append("Elevated Screen Time")
                explanation_items.append(f"Screen time is {round(mean_screen - baselines['screen'], 1)} hours higher than your baseline.")
            if mean_exercise < baselines["exercise"] - 15.0:
                risk_indicators.append("Reduced Physical Activity")
                explanation_items.append("Physical activity is significantly lower than your usual baseline.")

        if not risk_indicators:
            risk_indicators.append("None")
            explanation_items.append("Your daily lifestyle habits remain highly aligned with your baseline.")

        explanation = " ".join(explanation_items)

        behavior_log = BehaviorLog(
            user_id=user_id,
            analysis_date=analysis_date,
            drift_score=round(drift_score, 2),
            consistency_score=round(consistency_score, 2),
            lifestyle_change_index=round(lifestyle_change_index, 2),
            risk_indicators=json.dumps(risk_indicators),
            explanation=explanation
        )
        return await self.behavior_repo.save(behavior_log)
