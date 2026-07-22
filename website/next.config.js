/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  env: {
    NEXT_PUBLIC_API_URL: 'https://mindguard-api-gz19.onrender.com',
  },
};

module.exports = nextConfig;
