from datetime import datetime
from sqlalchemy import ForeignKey, DateTime, Integer, String, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class MoodEntry(Base, AuditMixin):
    __tablename__ = "mood_entries"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    entry_time: Mapped[datetime] = mapped_column(DateTime, default=datetime.utcnow, index=True, nullable=False)
    mood_score: Mapped[int] = mapped_column(Integer, nullable=False) # 1 to 10
    notes: Mapped[str] = mapped_column(Text, nullable=True)
    category: Mapped[str] = mapped_column(String(50), index=True, nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="mood_entries")
