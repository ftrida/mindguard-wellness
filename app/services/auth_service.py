import secrets
import logging
from datetime import datetime, timedelta, timezone
from typing import Optional
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

from sqlalchemy.ext.asyncio import AsyncSession

from app.core import security
from app.core.config import settings
from app.core.constants import ROLE_USER, TOKEN_TYPE_ACCESS, TOKEN_TYPE_REFRESH
from app.exceptions.handlers import AuthenticationError, ConflictError, NotFoundError, APIException
from app.models.auth import User, Role, RefreshToken, PasswordResetToken, EmailVerification, LoginHistory, Device
from app.repositories.auth_repo import UserRepository, TokenRepository, RoleRepository

logger = logging.getLogger("mindguard")

class PasswordService:
    def hash_password(self, password: str) -> str:
        return security.get_password_hash(password)

    def verify_password(self, plain_password: str, hashed_password: str) -> bool:
        return security.verify_password(plain_password, hashed_password)

    def generate_random_token(self) -> str:
        return secrets.token_urlsafe(32)

class EmailService:
    """
    Service responsible for sending transaction emails (verifications, resets).
    Falls back to logger output if SMTP server properties are missing.
    """
    def send_email(self, to_email: str, subject: str, body: str) -> bool:
        if not settings.SMTP_USERNAME or not settings.SMTP_PASSWORD:
            logger.info(f"[EMAIL MOCK] To: {to_email} | Subject: {subject} | Body: {body}")
            return True

        try:
            msg = MIMEMultipart()
            msg["From"] = f"{settings.SMTP_FROM_NAME} <{settings.SMTP_FROM_EMAIL}>"
            msg["To"] = to_email
            msg["Subject"] = subject
            msg.attach(MIMEText(body, "html"))

            with smtplib.SMTP(settings.SMTP_HOST, settings.SMTP_PORT) as server:
                server.starttls()
                server.login(settings.SMTP_USERNAME, settings.SMTP_PASSWORD)
                server.sendmail(settings.SMTP_FROM_EMAIL, to_email, msg.as_string())
            logger.info(f"Email successfully sent to {to_email}")
            return True
        except Exception as e:
            logger.error(f"Failed to send email to {to_email}: {str(e)}")
            return False

    def send_verification_email(self, to_email: str, token: str) -> bool:
        subject = "Verify your MindGuard AI Account"
        body = f"""
        <html>
            <body>
                <h2>Welcome to MindGuard AI!</h2>
                <p>Please verify your email address by clicking the link below:</p>
                <p><a href="https://mindguard.vercel.app/verify-email?token={token}">Verify Email Address</a></p>
                <p>This verification link will expire in 24 hours.</p>
                <p>If you did not create this account, please ignore this email.</p>
            </body>
        </html>
        """
        return self.send_email(to_email, subject, body)

    def send_reset_password_email(self, to_email: str, token: str) -> bool:
        subject = "Reset your MindGuard AI Password"
        body = f"""
        <html>
            <body>
                <h2>Password Reset Request</h2>
                <p>You requested a password reset for your MindGuard AI account.</p>
                <p>Please click the link below to set a new password:</p>
                <p><a href="https://mindguard.vercel.app/reset-password?token={token}">Reset Password</a></p>
                <p>This link will expire in 2 hours.</p>
                <p>If you did not request a password reset, please ignore this email.</p>
            </body>
        </html>
        """
        return self.send_email(to_email, subject, body)

class JWTService:
    def generate_access_token(self, user_id: int, role: str) -> str:
        return security.create_access_token(subject=user_id, additional_claims={"role": role})

    def generate_refresh_token(self, user_id: int) -> str:
        return security.create_refresh_token(subject=user_id)

    def verify_access_token(self, token: str) -> int:
        try:
            payload = security.decode_token(token, settings.JWT_SECRET_KEY)
            if payload.get("type") != TOKEN_TYPE_ACCESS:
                raise ValueError("Invalid token type")
            user_id = int(payload.get("sub"))
            return user_id
        except Exception as e:
            raise AuthenticationError("Invalid or expired access token") from e

    def verify_refresh_token(self, token: str) -> int:
        try:
            payload = security.decode_token(token, settings.JWT_REFRESH_SECRET_KEY)
            if payload.get("type") != TOKEN_TYPE_REFRESH:
                raise ValueError("Invalid token type")
            user_id = int(payload.get("sub"))
            return user_id
        except Exception as e:
            raise AuthenticationError("Invalid or expired refresh token") from e

class UserService:
    def __init__(self, db: AsyncSession):
        self.user_repo = UserRepository(db)

    async def get_user_profile(self, user_id: int) -> User:
        user = await self.user_repo.get_by_id(user_id)
        if not user:
            raise NotFoundError("User profile not found")
        return user

    async def update_profile(self, user_id: int, username: Optional[str] = None, phone: Optional[str] = None) -> User:
        user = await self.user_repo.get_by_id(user_id)
        if not user:
            raise NotFoundError("User not found")

        if username and username != user.username:
            existing = await self.user_repo.get_by_username(username)
            if existing:
                raise ConflictError("Username is already taken")
            user.username = username

        if phone is not None:
            user.phone = phone

        await self.user_repo.save(user)
        return user

    async def delete_account(self, user_id: int) -> None:
        user = await self.user_repo.get_by_id(user_id)
        if not user:
            raise NotFoundError("User not found")
        user.is_deleted = True
        await self.user_repo.save(user)

class AuthenticationService:
    def __init__(self, db: AsyncSession):
        self.db = db
        self.user_repo = UserRepository(db)
        self.role_repo = RoleRepository(db)
        self.token_repo = TokenRepository(db)
        self.pwd_service = PasswordService()
        self.email_service = EmailService()
        self.jwt_service = JWTService()

    async def register(self, email: str, username: str, password: str, phone: Optional[str] = None) -> User:
        # Check duplicate email
        existing_email = await self.user_repo.get_by_email(email)
        if existing_email:
            raise ConflictError("Email is already registered")

        # Check duplicate username
        existing_username = await self.user_repo.get_by_username(username)
        if existing_username:
            raise ConflictError("Username is already registered")

        # Get or create Role User
        role = await self.role_repo.get_by_name(ROLE_USER)
        if not role:
            role = Role(name=ROLE_USER, description="Standard User Role")
            await self.role_repo.create(role)

        # Hash password and create User
        hashed_password = self.pwd_service.hash_password(password)
        new_user = User(
            email=email,
            username=username,
            hashed_password=hashed_password,
            phone=phone,
            role_id=role.id,
            is_active=True,
            is_verified=False
        )
        await self.user_repo.create(new_user)
        await self.db.flush()

        # Generate Email Verification
        token_str = self.pwd_service.generate_random_token()
        verification = EmailVerification(
            token=token_str,
            user_id=new_user.id,
            expires_at=datetime.utcnow() + timedelta(hours=24)
        )
        await self.token_repo.create_email_verification(verification)
        self.email_service.send_verification_email(email, token_str)

        return new_user

    async def login(self, email_or_username: str, password: str, ip_address: Optional[str] = None, user_agent: Optional[str] = None) -> tuple[User, str, str]:
        # Resolve user
        user = await self.user_repo.get_by_email(email_or_username)
        if not user:
            user = await self.user_repo.get_by_username(email_or_username)

        # Track failure / success in LoginHistory
        if not user or not self.pwd_service.verify_password(password, user.hashed_password):
            if user:
                history = LoginHistory(user_id=user.id, ip_address=ip_address, user_agent=user_agent, status="failure", failure_reason="Incorrect credentials")
                self.db.add(history)
            raise AuthenticationError("Invalid email/username or password")

        if not user.is_active:
            history = LoginHistory(user_id=user.id, ip_address=ip_address, user_agent=user_agent, status="failure", failure_reason="Account suspended")
            self.db.add(history)
            raise AuthenticationError("Your account has been suspended")

        # Record success log
        history = LoginHistory(user_id=user.id, ip_address=ip_address, user_agent=user_agent, status="success")
        self.db.add(history)

        # Generate tokens
        access_token = self.jwt_service.generate_access_token(user.id, user.role.name)
        refresh_token_str = self.jwt_service.generate_refresh_token(user.id)

        # Save refresh token in DB
        refresh_token = RefreshToken(
            token=refresh_token_str,
            user_id=user.id,
            expires_at=datetime.utcnow() + timedelta(days=settings.REFRESH_TOKEN_EXPIRE_DAYS)
        )
        await self.token_repo.create_refresh_token(refresh_token)

        return user, access_token, refresh_token_str

    async def refresh(self, refresh_token_str: str) -> tuple[str, str]:
        # Verify token validity
        user_id = self.jwt_service.verify_refresh_token(refresh_token_str)

        # Fetch token record from database (RTR - Refresh Token Rotation check)
        token_record = await self.token_repo.get_refresh_token(refresh_token_str)
        if not token_record or token_record.expires_at < datetime.utcnow() or token_record.is_revoked:
            if token_record:
                # If someone attempts to reuse a revoked or expired token, invalidate all user sessions
                await self.token_repo.revoke_all_user_tokens(token_record.user_id)
            raise AuthenticationError("Invalid or expired refresh token")

        # Invalidate current token (RTR)
        token_record.is_revoked = True
        await self.db.flush()

        # Load user
        user = await self.user_repo.get_by_id(user_id)
        if not user or not user.is_active:
            raise AuthenticationError("User is suspended or deleted")

        # Issue new pair
        new_access = self.jwt_service.generate_access_token(user.id, user.role.name)
        new_refresh_str = self.jwt_service.generate_refresh_token(user.id)

        # Persist new refresh token record
        new_refresh = RefreshToken(
            token=new_refresh_str,
            user_id=user.id,
            expires_at=datetime.utcnow() + timedelta(days=settings.REFRESH_TOKEN_EXPIRE_DAYS)
        )
        await self.token_repo.create_refresh_token(new_refresh)

        return new_access, new_refresh_str

    async def logout(self, refresh_token_str: str) -> None:
        await self.token_repo.revoke_refresh_token(refresh_token_str)

    async def logout_all(self, user_id: int) -> None:
        await self.token_repo.revoke_all_user_tokens(user_id)

    async def forgot_password(self, email: str) -> None:
        user = await self.user_repo.get_by_email(email)
        if not user:
            # SRE / Security tip: Avoid exposing account existence. Return success silently.
            logger.warning(f"Forgot password requested for unregistered email: {email}")
            return

        token_str = self.pwd_service.generate_random_token()
        reset_token = PasswordResetToken(
            token=token_str,
            user_id=user.id,
            expires_at=datetime.utcnow() + timedelta(hours=2)
        )
        await self.token_repo.create_password_reset_token(reset_token)
        self.email_service.send_reset_password_email(email, token_str)

    async def reset_password(self, token_str: str, new_password: str) -> None:
        token_record = await self.token_repo.get_password_reset_token(token_str)
        if not token_record:
            raise ValidationError("Invalid or expired password reset token")

        # Update password
        user = await self.user_repo.get_by_id(token_record.user_id)
        if not user or not user.is_active:
            raise ValidationError("Account is inactive")

        user.hashed_password = self.pwd_service.hash_password(new_password)
        token_record.is_used = True

        # Revoke all active sessions on password reset (security requirement)
        await self.token_repo.revoke_all_user_tokens(user.id)
        await self.user_repo.save(user)

    async def verify_email(self, token_str: str) -> None:
        ver_record = await self.token_repo.get_email_verification(token_str)
        if not ver_record:
            raise ValidationError("Invalid or expired email verification token")

        user = await self.user_repo.get_by_id(ver_record.user_id)
        if not user:
            raise ValidationError("User not found")

        user.is_verified = True
        ver_record.is_verified = True
        await self.user_repo.save(user)
