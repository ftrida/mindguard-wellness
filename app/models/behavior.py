from datetime import date
from sqlalchemy import Integer, ForeignKey, Date, Float, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class BehaviorLog(Base, AuditMixin):
    __tablename__ = "behavior_logs"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    analysis_date: Mapped[date] = mapped_column(Date, nullable=False, index=True)
    drift_score: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    consistency_score: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    lifestyle_change_index: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    risk_indicators: Mapped[str] = mapped_column(Text, nullable=True) # Stored as JSON string
    explanation: Mapped[str] = mapped_column(Text, nullable=True)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="behavior_logs")
