import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'https://mindguard-api-gz19.onrender.com';

export const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Attach Access Token
api.interceptors.request.use((config) => {
  if (typeof window !== 'undefined') {
    const token = localStorage.getItem('access_token');
    if (token && config.headers) {
      config.headers.Authorization = `Bearer ${token}`;
    }
  }
  return config;
});

// Response Interceptor: Handle 401 & Automatic Refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        const refreshToken = localStorage.getItem('refresh_token');
        if (refreshToken) {
          const res = await axios.post(`${API_BASE_URL}/api/v1/auth/refresh`, {
            refresh_token: refreshToken,
          });
          if (res.data?.access_token) {
            localStorage.setItem('access_token', res.data.access_token);
            originalRequest.headers.Authorization = `Bearer ${res.data.access_token}`;
            return api(originalRequest);
          }
        }
      } catch (refreshErr) {
        if (typeof window !== 'undefined') {
          localStorage.removeItem('access_token');
          localStorage.removeItem('refresh_token');
          window.location.href = '/login';
        }
      }
    }
    return Promise.reject(error);
  }
);
