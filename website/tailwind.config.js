/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/pages/**/*.{js,ts,jsx,tsx,mdx}',
    './src/components/**/*.{js,ts,jsx,tsx,mdx}',
    './src/app/**/*.{js,ts,jsx,tsx,mdx}',
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          dark: '#0F172A',
          teal: '#00A896',
          cyan: '#02C39A',
          gold: '#F4D35E',
          coral: '#EE964B',
          deepTeal: '#0D3B66',
        },
      },
    },
  },
  plugins: [],
};
