from datetime import date
from typing import Optional, List
from app.repositories.goal_repo import GoalRepository
from app.models.goal import Goal
from app.schemas.goal import GoalCreate, GoalUpdate

class GoalService:
    def __init__(self, goal_repo: GoalRepository):
        self.goal_repo = goal_repo

    async def get_goal(self, goal_id: int) -> Optional[Goal]:
        return await self.goal_repo.get_by_id(goal_id)

    async def get_user_goals(self, user_id: int, status: Optional[str] = None) -> List[Goal]:
        return await self.goal_repo.get_all_by_user_id(user_id, status)

    async def create_goal(self, user_id: int, goal_data: GoalCreate) -> Goal:
        goal = Goal(
            user_id=user_id,
            title=goal_data.title,
            category=goal_data.category,
            target_value=goal_data.target_value,
            current_value=0.0,
            start_date=goal_data.start_date,
            target_date=goal_data.target_date,
            status="active"
        )
        return await self.goal_repo.save(goal)

    async def update_goal(self, goal_id: int, update_data: GoalUpdate) -> Optional[Goal]:
        goal = await self.goal_repo.get_by_id(goal_id)
        if not goal:
            return None

        if update_data.title is not None:
            goal.title = update_data.title
        
        if update_data.current_value is not None:
            goal.current_value = update_data.current_value
            if goal.current_value >= goal.target_value:
                goal.status = "completed"
        
        if update_data.status is not None:
            goal.status = update_data.status

        return await self.goal_repo.save(goal)

    async def delete_goal(self, goal_id: int) -> bool:
        goal = await self.goal_repo.get_by_id(goal_id)
        if goal:
            goal.is_deleted = True
            await self.goal_repo.save(goal)
            return True
        return False
