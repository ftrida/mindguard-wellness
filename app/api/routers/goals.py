from typing import List, Optional
from fastapi import APIRouter, Depends, HTTPException, Query, status, Response
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.goal_repo import GoalRepository
from app.services.goal_service import GoalService
from app.schemas.goal import GoalCreate, GoalUpdate, GoalResponse

router = APIRouter(prefix="/goals", tags=["Goal Tracking API"])

async def get_goal_service(db: AsyncSession = Depends(get_db)) -> GoalService:
    return GoalService(GoalRepository(db))

@router.get("", response_model=List[GoalResponse])
async def list_goals(
    status: Optional[str] = Query(None, description="Filter by status: active, completed, failed"),
    current_user: User = Depends(get_current_active_user),
    service: GoalService = Depends(get_goal_service)
):
    return await service.get_user_goals(current_user.id, status)

@router.post("", response_model=GoalResponse, status_code=status.HTTP_201_CREATED)
async def create_goal(
    payload: GoalCreate,
    current_user: User = Depends(get_current_active_user),
    service: GoalService = Depends(get_goal_service)
):
    return await service.create_goal(current_user.id, payload)

@router.get("/{id}", response_model=GoalResponse)
async def get_goal_details(
    id: int,
    current_user: User = Depends(get_current_active_user),
    service: GoalService = Depends(get_goal_service)
):
    goal = await service.get_goal(id)
    if not goal or goal.user_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Goal not found")
    return goal

@router.put("/{id}", response_model=GoalResponse)
async def update_goal(
    id: int,
    payload: GoalUpdate,
    current_user: User = Depends(get_current_active_user),
    service: GoalService = Depends(get_goal_service)
):
    # Verify owner
    goal = await service.get_goal(id)
    if not goal or goal.user_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Goal not found")
    
    updated = await service.update_goal(id, payload)
    return updated

@router.delete("/{id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_goal(
    id: int,
    current_user: User = Depends(get_current_active_user),
    service: GoalService = Depends(get_goal_service)
):
    # Verify owner
    goal = await service.get_goal(id)
    if not goal or goal.user_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Goal not found")
    
    success = await service.delete_goal(id)
    if not success:
         raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Goal not found")
    return Response(status_code=status.HTTP_204_NO_CONTENT)
