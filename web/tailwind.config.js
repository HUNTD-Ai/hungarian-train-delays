/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primaryColor: '#6665DD',
        primaryColorHover: '#4D4BE2',
        textColor: '#F9FAFB',
        backgroundDark: '#293132',
        backgroundLight: '#EFF6FF',
        cardBackgroundDark: '#3E4849',
        cardBackgroundLight: '#FFFFFF',
        textBoxBackgroundDark: '#525F61',
        textBoxBackgroundLight: '#D9D9D9',
        textBoxTextColorDark: '#F9FAFB',
        textBoxTextColorLight: '#111827',
      },
    },
  },
  plugins: [],
};
