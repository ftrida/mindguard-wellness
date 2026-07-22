from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker
from sqlalchemy.orm import DeclarativeBase
from app.core.config import settings

# Determine database pool arguments based on DB engine
# SQLite does not support pool_size, max_overflow, pool_recycle, etc.
is_sqlite = settings.DATABASE_URL.startswith("sqlite")

connect_args = {}
if is_sqlite:
    connect_args["check_same_thread"] = False

engine_kwargs = {
    "future": True,
    "echo": False,
}

if not is_sqlite:
    engine_kwargs.update({
        "pool_size": 20,
        "max_overflow": 10,
        "pool_recycle": 1800,
        "pool_pre_ping": True,
    })
else:
    engine_kwargs.update({
        "connect_args": connect_args
    })

# Create the asynchronous engine
engine = create_async_engine(
    settings.DATABASE_URL,
    **engine_kwargs
)

# Async session factory creation
AsyncSessionLocal = async_sessionmaker(
    bind=engine,
    autoflush=False,
    autocommit=False,
    expire_on_commit=False
)

class Base(DeclarativeBase):
    pass
