from datetime import time
from sqlalchemy import ForeignKey, String, Text, Boolean, Integer, Time
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class Notification(Base, AuditMixin):
    __tablename__ = "notifications"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    title: Mapped[str] = mapped_column(String(200), nullable=False)
    message: Mapped[str] = mapped_column(Text, nullable=False)
    is_read: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False, index=True)
    category: Mapped[str] = mapped_column(String(50), default="System", nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="notifications")

class NotificationPreference(Base, AuditMixin):
    __tablename__ = "notification_preferences"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), unique=True, index=True, nullable=False)
    email_enabled: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    push_enabled: Mapped[bool] = mapped_column(Boolean, default=True, nullable=False)
    quiet_hours_start: Mapped[time] = mapped_column(Time, nullable=True)
    quiet_hours_end: Mapped[time] = mapped_column(Time, nullable=True)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="notification_preference")
