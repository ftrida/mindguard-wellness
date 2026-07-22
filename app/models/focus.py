from datetime import datetime
from sqlalchemy import ForeignKey, DateTime, Integer
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class FocusSession(Base, AuditMixin):
    __tablename__ = "focus_sessions"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    custom_duration_seconds: Mapped[int] = mapped_column(Integer, nullable=False)
    break_duration_seconds: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    completed_sessions_count: Mapped[int] = mapped_column(Integer, default=1, nullable=False)
    completed_at: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow, index=True, nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="focus_sessions")
