from datetime import date, time
from sqlalchemy import ForeignKey, Date, Time, Integer, Float, UniqueConstraint
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class DailyLifestyleLog(Base, AuditMixin):
    __tablename__ = "daily_lifestyle_logs"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    log_date: Mapped[date] = mapped_column(Date, index=True, nullable=False)
    sleep_hours: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    wake_time: Mapped[time] = mapped_column(Time, nullable=True)
    bed_time: Mapped[time] = mapped_column(Time, nullable=True)
    water_intake: Mapped[float] = mapped_column(Float, default=0.0, nullable=False) # In liters
    exercise_minutes: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    walking_steps: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    screen_time: Mapped[float] = mapped_column(Float, default=0.0, nullable=False) # In hours
    phone_unlock_count: Mapped[int] = mapped_column(Integer, default=0, nullable=False)
    study_hours: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    work_hours: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    social_time: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    outdoor_time: Mapped[float] = mapped_column(Float, default=0.0, nullable=False)
    energy_level: Mapped[int] = mapped_column(Integer, default=5, nullable=False) # Scale 1-10

    # Relationships
    user: Mapped["User"] = relationship(back_populates="lifestyle_logs")

    # Add UniqueConstraint to ensure only one log per day per user
    __table_args__ = (
        UniqueConstraint("user_id", "log_date", name="uq_user_daily_log"),
    )
