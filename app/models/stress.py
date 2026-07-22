from datetime import date
from sqlalchemy import Integer, ForeignKey, Date, Float, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class StressLikelihood(Base, AuditMixin):
    __tablename__ = "stress_likelihoods"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    assessment_date: Mapped[date] = mapped_column(Date, nullable=False, index=True)
    stress_score: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    confidence_score: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    contributing_factors: Mapped[str] = mapped_column(Text, nullable=True) # Stored as JSON string
    recommendations: Mapped[str] = mapped_column(Text, nullable=True) # Stored as JSON string

    # Relationships
    user: Mapped["User"] = relationship(back_populates="stress_likelihoods")
