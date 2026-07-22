from datetime import date
from sqlalchemy import Integer, ForeignKey, Date, Float
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class DigitalLifestyleTwin(Base, AuditMixin):
    __tablename__ = "digital_lifestyle_twins"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    snapshot_date: Mapped[date] = mapped_column(Date, nullable=False, index=True)
    sleep_baseline: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    activity_baseline: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    screen_baseline: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    mood_baseline: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    wellness_score: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    version: Mapped[int] = mapped_column(Integer, default=1, nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="lifestyle_twins")
