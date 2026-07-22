import uuid
from starlette.middleware.base import BaseHTTPMiddleware, RequestResponseEndpoint
from fastapi import Request, Response, HTTPException, status

class SecurityMiddleware(BaseHTTPMiddleware):
    def __init__(self, app, max_content_length: int = 10 * 1024 * 1024):  # Default 10MB
        super().__init__(app)
        self.max_content_length = max_content_length

    async def dispatch(self, request: Request, call_next: RequestResponseEndpoint) -> Response:
        # Enforce Request Size Limit
        content_length = request.headers.get("content-length")
        if content_length:
            try:
                if int(content_length) > self.max_content_length:
                    return Response(
                        status_code=status.HTTP_413_REQUEST_ENTITY_TOO_LARGE,
                        content="Request payload exceeds maximum size limit (10MB)."
                    )
            except ValueError:
                pass

        # Generate unique request and correlation IDs
        request_id = request.headers.get("X-Request-ID") or str(uuid.uuid4())
        correlation_id = request.headers.get("X-Correlation-ID") or str(uuid.uuid4())

        # Set IDs in state so they are accessible in routes or logs
        request.state.request_id = request_id
        request.state.correlation_id = correlation_id

        # Proceed with request execution
        response = await call_next(request)

        # Append Request & Correlation IDs to response headers
        response.headers["X-Request-ID"] = request_id
        response.headers["X-Correlation-ID"] = correlation_id

        # Security Headers Setup
        response.headers["X-Content-Type-Options"] = "nosniff"
        response.headers["X-Frame-Options"] = "DENY"
        response.headers["X-XSS-Protection"] = "1; mode=block"
        response.headers["Strict-Transport-Security"] = "max-age=31536000; includeSubDomains"
        response.headers["Content-Security-Policy"] = "default-src 'self'; frame-ancestors 'none';"

        return response
