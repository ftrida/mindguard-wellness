from datetime import date, timedelta
from typing import List, Optional
from fastapi import APIRouter, Depends, Query
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.behavior_repo import BehaviorRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.twin_repo import TwinRepository
from app.services.twin_service import TwinService
from app.services.behavior_service import BehaviorService
from app.schemas.behavior import BehaviorLogResponse

router = APIRouter(prefix="/behavior", tags=["Behavior Analysis Engine API"])

async def get_behavior_service(db: AsyncSession = Depends(get_db)) -> BehaviorService:
    twin_service = TwinService(
        twin_repo=TwinRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db)
    )
    return BehaviorService(
        behavior_repo=BehaviorRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db),
        twin_service=twin_service
    )

@router.get("/drift", response_model=BehaviorLogResponse)
async def get_current_drift_analysis(
    current_user: User = Depends(get_current_active_user),
    service: BehaviorService = Depends(get_behavior_service)
):
    analysis = await service.get_latest_analysis(current_user.id)
    if not analysis:
        # Run live drift analysis
        analysis = await service.analyze_behavior_drift(current_user.id, date.today())
    return analysis

@router.get("/history", response_model=List[BehaviorLogResponse])
async def get_drift_history(
    start_date: Optional[date] = Query(None),
    end_date: Optional[date] = Query(None),
    current_user: User = Depends(get_current_active_user),
    service: BehaviorService = Depends(get_behavior_service)
):
    end_val = end_date or date.today()
    start_val = start_date or (end_val - timedelta(days=30))
    return await service.get_history(current_user.id, start_val, end_val)
