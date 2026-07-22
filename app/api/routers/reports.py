from datetime import date, timedelta
from typing import Dict, Any, Optional
from fastapi import APIRouter, Depends, Query
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.twin_repo import TwinRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.behavior_repo import BehaviorRepository
from app.repositories.stress_repo import StressRepository
from app.repositories.focus_repo import FocusRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.recommendation_repo import RecommendationRepository
from app.services.twin_service import TwinService
from app.services.behavior_service import BehaviorService
from app.services.stress_service import StressService
from app.services.recommendation_service import RecommendationService

router = APIRouter(prefix="/reports", tags=["Report Engine API"])

async def get_services(db: AsyncSession = Depends(get_db)) -> Dict[str, Any]:
    twin = TwinService(TwinRepository(db), LifestyleRepository(db), MoodRepository(db))
    behavior = BehaviorService(BehaviorRepository(db), LifestyleRepository(db), MoodRepository(db), twin)
    stress = StressService(StressRepository(db), LifestyleRepository(db), MoodRepository(db), FocusRepository(db), JournalRepository(db))
    recommendation = RecommendationService(RecommendationRepository(db), LifestyleRepository(db), MoodRepository(db))
    return {
        "twin": twin,
        "behavior": behavior,
        "stress": stress,
        "recommendation": recommendation,
        "lifestyle": LifestyleRepository(db)
    }

@router.get("/daily")
async def get_daily_report(
    target_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    services: Dict[str, Any] = Depends(get_services)
):
    t_date = target_date or date.today()
    comp = await services["twin"].compare_twin(current_user.id, t_date)
    stress = await services["stress"].assess_stress_likelihood(current_user.id, t_date)
    recs = await services["recommendation"].get_active_recommendations(current_user.id)

    # Format contributing factors and recommendations
    import json
    factors = json.loads(stress.contributing_factors) if stress.contributing_factors else {}
    suggestions = json.loads(stress.recommendations) if stress.recommendations else []

    return {
        "report_type": "Daily Wellness Report",
        "date": t_date,
        "wellness_score": comp.wellness_score,
        "metrics_summary": comp.current_metrics,
        "baselines": comp.baseline_metrics,
        "deviations": comp.deviations,
        "stress_likelihood": {
            "score": stress.stress_score,
            "confidence": stress.confidence_score,
            "factors": factors,
            "suggestions": suggestions
        },
        "active_recommendations": [
            {"title": r.title, "content": r.content, "category": r.category} for r in recs
        ]
    }

@router.get("/weekly")
async def get_weekly_report(
    current_user: User = Depends(get_current_active_user),
    services: Dict[str, Any] = Depends(get_services)
):
    today = date.today()
    twin = await services["twin"].get_latest_twin(current_user.id)
    drift = await services["behavior"].get_latest_analysis(current_user.id)
    if not drift:
        drift = await services["behavior"].analyze_behavior_drift(current_user.id, today)

    # Get weekly averages
    weekly_averages = await services["twin"].get_snapshot_averages(current_user.id, 7)

    import json
    risks = json.loads(drift.risk_indicators) if drift.risk_indicators else []

    return {
        "report_type": "Weekly Analytical Summary",
        "date_generated": today,
        "twin_baseline": {
            "wellness_score": twin.wellness_score if twin else 75.0,
            "sleep_baseline": twin.sleep_baseline if twin else 7.5,
            "activity_baseline": twin.activity_baseline if twin else 30.0,
            "screen_baseline": twin.screen_baseline if twin else 4.0
        },
        "weekly_averages": weekly_averages,
        "drift_analysis": {
            "drift_score": drift.drift_score,
            "consistency_score": drift.consistency_score,
            "lifestyle_change_index": drift.lifestyle_change_index,
            "risk_indicators": risks,
            "explanation": drift.explanation
        }
    }

@router.get("/monthly")
async def get_monthly_report(
    current_user: User = Depends(get_current_active_user),
    services: Dict[str, Any] = Depends(get_services)
):
    today = date.today()
    # Monthly averages
    monthly_averages = await services["twin"].get_snapshot_averages(current_user.id, 30)
    twin = await services["twin"].get_latest_twin(current_user.id)

    return {
        "report_type": "Monthly Wellness Trend Summary",
        "date_generated": today,
        "baseline_wellness_score": twin.wellness_score if twin else 75.0,
        "monthly_averages": monthly_averages,
        "status": "Healthy routine bounds maintained." if monthly_averages["sleep_hours"] >= 7.0 else "Action recommended: Sleep average is below threshold."
    }

@router.get("/export")
async def export_raw_data(
    current_user: User = Depends(get_current_active_user),
    services: Dict[str, Any] = Depends(get_services)
):
    # Fetch past 180 days logs for export
    today = date.today()
    logs = await services["lifestyle"].get_range(current_user.id, today - timedelta(days=180), today)
    
    export_data = []
    for log in logs:
        export_data.append({
            "date": log.log_date,
            "sleep_hours": log.sleep_hours,
            "water_intake_l": log.water_intake,
            "exercise_minutes": log.exercise_minutes,
            "walking_steps": log.walking_steps,
            "screen_time_h": log.screen_time,
            "energy_level": log.energy_level
        })

    return {
        "user_id": current_user.id,
        "export_date": today,
        "records_count": len(export_data),
        "data": export_data
    }
