from datetime import datetime, date
from typing import Optional, List
from sqlalchemy import String, ForeignKey, DateTime, Date, Boolean, Integer, Float, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship as orm_relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class UserProfile(Base, AuditMixin):
    __tablename__ = "user_profiles"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), unique=True, index=True, nullable=False)
    profile_photo_url: Mapped[Optional[str]] = mapped_column(String(500), nullable=True)
    dob: Mapped[Optional[date]] = mapped_column(Date, nullable=True)
    gender: Mapped[Optional[str]] = mapped_column(String(20), nullable=True)
    height: Mapped[Optional[float]] = mapped_column(Float, nullable=True)
    weight: Mapped[Optional[float]] = mapped_column(Float, nullable=True)
    timezone: Mapped[str] = mapped_column(String(100), default="UTC", nullable=False)
    country: Mapped[Optional[str]] = mapped_column(String(100), nullable=True)
    language: Mapped[str] = mapped_column(String(10), default="en", nullable=False)
    occupation: Mapped[Optional[str]] = mapped_column(String(100), nullable=True)
    emergency_preferences: Mapped[Optional[str]] = mapped_column(Text, nullable=True)

    # Relationships
    user: Mapped["User"] = orm_relationship(back_populates="profile")

class EmergencyContact(Base, AuditMixin):
    __tablename__ = "emergency_contacts"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    name: Mapped[str] = mapped_column(String(100), nullable=False)
    relationship: Mapped[str] = mapped_column(String(50), nullable=False)
    phone: Mapped[str] = mapped_column(String(20), nullable=False)
    is_primary: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)

    # Relationships
    user: Mapped["User"] = orm_relationship(back_populates="emergency_contacts")
