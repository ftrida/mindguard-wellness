from typing import List
from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.achievement_repo import AchievementRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.meditation_repo import MeditationRepository
from app.services.achievement_service import AchievementService
from app.schemas.achievement import AchievementResponse, StreakResponse

router = APIRouter(prefix="/achievements", tags=["Achievements & Streaks API"])

async def get_achievement_service(db: AsyncSession = Depends(get_db)) -> AchievementService:
    return AchievementService(
        achievement_repo=AchievementRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        meditation_repo=MeditationRepository(db)
    )

@router.get("", response_model=List[AchievementResponse])
async def list_unlocked_achievements(
    current_user: User = Depends(get_current_active_user),
    service: AchievementService = Depends(get_achievement_service)
):
    # Runs checks on badges eligibility and unlocks new ones
    return await service.check_and_unlock_achievements(current_user.id)

@router.get("/streak", response_model=StreakResponse)
async def get_streak_metrics(
    current_user: User = Depends(get_current_active_user),
    service: AchievementService = Depends(get_achievement_service)
):
    return await service.get_streak(current_user.id)
