from fastapi import FastAPI, Request, status
from fastapi.responses import JSONResponse
from fastapi.exceptions import RequestValidationError

class APIException(Exception):
    def __init__(self, message: str, status_code: int = status.HTTP_400_BAD_REQUEST):
        super().__init__(message)
        self.message = message
        self.status_code = status_code

class AuthenticationError(APIException):
    def __init__(self, message: str = "Authentication failed"):
        super().__init__(message, status_code=status.HTTP_401_UNAUTHORIZED)

class PermissionDeniedError(APIException):
    def __init__(self, message: str = "Permission denied"):
        super().__init__(message, status_code=status.HTTP_403_FORBIDDEN)

class NotFoundError(APIException):
    def __init__(self, message: str = "Resource not found"):
        super().__init__(message, status_code=status.HTTP_404_NOT_FOUND)

class ConflictError(APIException):
    def __init__(self, message: str = "Resource already exists"):
        super().__init__(message, status_code=status.HTTP_409_CONFLICT)

class ValidationError(APIException):
    def __init__(self, message: str = "Validation failed"):
        super().__init__(message, status_code=status.HTTP_422_UNPROCESSABLE_ENTITY)

def register_exception_handlers(app: FastAPI) -> None:
    @app.exception_handler(APIException)
    async def api_exception_handler(request: Request, exc: APIException) -> JSONResponse:
        return JSONResponse(
            status_code=exc.status_code,
            content={"detail": exc.message}
        )

    @app.exception_handler(RequestValidationError)
    async def validation_exception_handler(request: Request, exc: RequestValidationError) -> JSONResponse:
        errors = exc.errors()
        error_messages = []
        for error in errors:
            loc = " -> ".join(str(x) for x in error.get("loc", []))
            msg = error.get("msg", "Invalid value")
            error_messages.append(f"{loc}: {msg}")
        
        detail_msg = "; ".join(error_messages) if error_messages else "Validation error"
        return JSONResponse(
            status_code=status.HTTP_422_UNPROCESSABLE_ENTITY,
            content={"detail": detail_msg}
        )

    @app.exception_handler(Exception)
    async def global_exception_handler(request: Request, exc: Exception) -> JSONResponse:
        # Avoid exposing stack traces in production environment
        # Real SRE setups will log this internally
        import logging
        logger = logging.getLogger("mindguard")
        logger.error(f"Unhandled Exception: {str(exc)}", exc_info=True)
        return JSONResponse(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            content={"detail": "Internal server error"}
        )
