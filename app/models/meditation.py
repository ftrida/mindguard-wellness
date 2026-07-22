from datetime import datetime
from sqlalchemy import ForeignKey, DateTime, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class MeditationSession(Base, AuditMixin):
    __tablename__ = "meditation_sessions"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    category: Mapped[str] = mapped_column(String(100), default="Mindfulness", nullable=False)
    duration_seconds: Mapped[int] = mapped_column(Integer, nullable=False)
    completed_at: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow, index=True, nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="meditation_sessions")
