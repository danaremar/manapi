/** @type {import('tailwindcss').Config} */
const colors = require('tailwindcss/colors')

module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
    colors: {
      dark: {
        1: '#DBDBDB',
        2: '#9E9E9E',
        3: '#2C2C2C',
        4: '#252525',
        5: '#1C1C1C',
      },
      slate: colors.slate,
      gray: colors.gray,
      neutral: colors.neutral,
      green: colors.green,
      blue: colors.blue,
      cyan: colors.cyan,
      indigo: colors.indigo,
      red: colors.red,
      yellow: colors.yellow,
      white: colors.white,
      black: colors.black
    }
  },
  plugins: [],
}
