import uuid
from datetime import datetime, timedelta, timezone
from typing import Any, Dict, Optional, Union
from jose import JWTError, jwt
import bcrypt
from app.core.config import settings

def verify_password(plain_password: str, hashed_password: str) -> bool:
    try:
        return bcrypt.checkpw(
            plain_password.encode("utf-8"),
            hashed_password.encode("utf-8")
        )
    except Exception:
        return False

def get_password_hash(password: str) -> str:
    salt = bcrypt.gensalt()
    return bcrypt.hashpw(password.encode("utf-8"), salt).decode("utf-8")

def create_jwt_token(
    subject: Union[str, Any],
    expires_delta: timedelta,
    token_type: str,
    secret_key: str,
    additional_claims: Optional[Dict[str, Any]] = None
) -> str:
    now = datetime.now(timezone.utc)
    expire = now + expires_delta
    to_encode = {
        "exp": expire,
        "iat": now,
        "sub": str(subject),
        "type": token_type,
        "jti": str(uuid.uuid4())
    }
    if additional_claims:
        to_encode.update(additional_claims)
    return jwt.encode(to_encode, secret_key, algorithm=settings.ALGORITHM)

def create_access_token(subject: Union[str, Any], additional_claims: Optional[Dict[str, Any]] = None) -> str:
    expires_delta = timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)
    return create_jwt_token(
        subject=subject,
        expires_delta=expires_delta,
        token_type="access",
        secret_key=settings.JWT_SECRET_KEY,
        additional_claims=additional_claims
    )

def create_refresh_token(subject: Union[str, Any]) -> str:
    expires_delta = timedelta(days=settings.REFRESH_TOKEN_EXPIRE_DAYS)
    return create_jwt_token(
        subject=subject,
        expires_delta=expires_delta,
        token_type="refresh",
        secret_key=settings.JWT_REFRESH_SECRET_KEY
    )

def decode_token(token: str, secret_key: str) -> Dict[str, Any]:
    try:
        decoded_token: Dict[str, Any] = jwt.decode(token, secret_key, algorithms=[settings.ALGORITHM])
        return decoded_token
    except JWTError as e:
        raise ValueError("Invalid token signature or expired token") from e
