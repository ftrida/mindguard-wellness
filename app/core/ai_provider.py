import logging
import json
import httpx
from abc import ABC, abstractmethod
from typing import List, Dict, Any
from app.core.config import settings

logger = logging.getLogger("mindguard.ai_provider")

class BaseAIProvider(ABC):
    @abstractmethod
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        """
        Generates response given user prompt, chat history list of dicts, and current user context dict.
        """
        pass

class MockAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        # Simple template rule-based mock responses
        prompt_lower = prompt.lower()
        if "stress" in prompt_lower or "anxious" in prompt_lower:
            return "Based on your Digital Twin logs, your stress levels are elevated. Try a short 10-minute meditation."
        if "sleep" in prompt_lower:
            return f"Your sleep baseline is {context.get('twin_baselines', {}).get('sleep_hours', 7.5)} hours. Try setting a standard bedtime."
        return "I've reviewed your Digital Twin metrics. Your habits look stable today. What wellness goals can I support you with?"

class GeminiAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling Google Gemini REST API...")
        api_key = settings.GEMINI_API_KEY or "AQ.Ab8RN6K468zq7B96y4rfVpTamJtgtjUOgQT-oinJtPjp-cAdTQ"
        url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key={api_key}"

        # Build prompt with instruction
        system_instruction = (
            "You are MindGuard's AI Wellness Coach. Your goal is to guide the user towards healthy habits "
            "based on their Digital Twin metrics, stress index, and lifestyle consistency logs. Be supportive, empathetic, "
            "and concise. Avoid prescribing medical treatments.\n"
            f"Current User Context:\n{json.dumps(context, indent=2)}"
        )

        # Construct messages list for Gemini contents
        contents = []
        for item in history:
            role = "user" if item.get("role") == "user" else "model"
            contents.append({
                "role": role,
                "parts": [{"text": item.get("content", "")}]
            })

        # Add the current prompt if not already in history
        contents.append({
            "role": "user",
            "parts": [{"text": prompt}]
        })

        payload = {
            "contents": contents,
            "systemInstruction": {
                "parts": [{"text": system_instruction}]
            }
        }

        try:
            async with httpx.AsyncClient(timeout=30.0) as client:
                res = await client.post(url, json=payload)
                if res.status_code == 200:
                    res_json = res.json()
                    candidates = res_json.get("candidates", [])
                    if candidates:
                        text = candidates[0].get("content", {}).get("parts", [{}])[0].get("text", "")
                        if text:
                            return text.strip()
                    logger.error(f"Invalid Gemini response structure: {res_json}")
                    return "I'm having trouble analyzing your request. Please try again soon."
                else:
                    logger.error(f"Gemini API returned status code {res.status_code}: {res.text}")
                    return "I'm having trouble connecting to my AI core right now, but please continue tracking your habits."
        except Exception as e:
            logger.exception("Failed to connect to Gemini API")
            return "I couldn't reach my AI wellness coach server. Let's try again in a moment."

class OpenAIAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling OpenAI ChatCompletion API...")
        return "[OpenAI ChatCompletion Adaptor Stub] Focused and rested habits are recommended."

class ClaudeAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling Anthropic Claude API...")
        return "[Claude AI Adaptor Stub] Keeping consistent screen time supports focus."

# Provider Registry Factory
class AIProviderRegistry:
    @staticmethod
    def get_provider(provider_name: str) -> BaseAIProvider:
        name = provider_name.lower()
        if name == "gemini":
            return GeminiAIProvider()
        elif name == "openai":
            return OpenAIAIProvider()
        elif name == "claude":
            return ClaudeAIProvider()
        else:
            logger.info("Defaulting to MockAIProvider wellness engine.")
            return MockAIProvider()

# Registry dependency helper
def get_ai_provider() -> BaseAIProvider:
    # Read provider name from config (defaults to 'mock')
    provider_name = getattr(settings, "AI_PROVIDER", "mock")
    return AIProviderRegistry.get_provider(provider_name)
