// eslint-disable-next-line no-undef
module.exports = {
  "extends": "next/core-web-vitals",
  env: {
    node: true,
    browser: true,
    es2021: true
  },
  extends: [
    'next/core-web-vitals',
    'eslint:recommended',
    'plugin:import/recommended'
  ],
  ignorePatterns: ['node_modules/*', 'dist/*'],
  parser: '@typescript-eslint/parser',
  parserOptions: {
    ecmaFeatures: {
      jsx: true
    },
    ecmaVersion: 'latest',
    sourceType: 'module'
  },
  plugins: [
    'import',
    '@typescript-eslint'
  ],
  rules: {
    'no-trailing-spaces': 'error',
    'no-multiple-empty-lines': 'error',
    indent: ['error', 2, { SwitchCase: 1 }],
    'linebreak-style': ['error', 'unix'],
    quotes: ['error', 'single'],
    complexity: ['error', 8],
    'comma-dangle': ['error'],
    semi: ['error', 'never'],
    'object-curly-spacing': ['error', 'always'],
    'no-unused-vars': [
      'error',
      {
        argsIgnorePattern: '^_',
        ignoreRestSiblings: true,
        args: 'none'
      }
    ],
    'import/order': 'error',
    'import/no-unresolved': 'off'
  },
  settings: {
    'import/resolver': {
      node: {
        extensions: ['.js', '.jsx', '.ts', '.tsx'],
        moduleDirectory: ['node_modules', 'src/']
      }
    },
    'import/ignore': 'node_modules'
  }
}
