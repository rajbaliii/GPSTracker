# Contributing to GPS Tracker Pro

First off, thank you for considering contributing to GPS Tracker Pro! It's people like you that make this project better for everyone.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Commit Guidelines](#commit-guidelines)

## 🤝 Code of Conduct

This project and everyone participating in it is governed by our Code of Conduct. By participating, you are expected to uphold this code. Please report unacceptable behavior to your.email@example.com.

### Our Standards

- Using welcoming and inclusive language
- Being respectful of differing viewpoints and experiences
- Gracefully accepting constructive criticism
- Focusing on what is best for the community
- Showing empathy towards other community members

## 🎯 How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check the existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps to reproduce the problem**
- **Provide specific examples to demonstrate the steps**
- **Describe the behavior you observed after following the steps**
- **Explain which behavior you expected to see instead and why**
- **Include screenshots and animated GIFs** if possible
- **Include your environment details** (OS, browser, version)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

- **Use a clear and descriptive title**
- **Provide a step-by-step description of the suggested enhancement**
- **Provide specific examples to demonstrate the steps**
- **Describe the current behavior** and **explain the expected behavior**
- **Explain why this enhancement would be useful**
- **List any similar features in other applications** if applicable

### Your First Code Contribution

Unsure where to begin? You can start by looking through these `beginner` and `help-wanted` issues:

- **Beginner issues** - issues which should only require a few lines of code
- **Help wanted issues** - issues which should be a bit more involved

## 🛠️ Development Setup

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn
- Git

### Setup Steps

1. **Fork the repository**
   ```bash
   # Click the 'Fork' button on GitHub
   ```

2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/GPSTracker.git
   cd GPSTracker
   ```

3. **Add upstream remote**
   ```bash
   git remote add upstream https://github.com/therajbali/GPSTracker.git
   ```

4. **Install dependencies**
   ```bash
   npm install
   ```

5. **Create a branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

6. **Start development server**
   ```bash
   npm run dev
   ```

## 🔄 Pull Request Process

1. **Update your fork**
   ```bash
   git fetch upstream
   git checkout main
   git merge upstream/main
   ```

2. **Create a feature branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make your changes**
   - Write clean, readable code
   - Follow the coding standards
   - Add tests if applicable
   - Update documentation

4. **Test your changes**
   ```bash
   npm run lint
   npm run test
   npm run build
   ```

5. **Commit your changes**
   ```bash
   git add .
   git commit -m "feat: add amazing feature"
   ```

6. **Push to your fork**
   ```bash
   git push origin feature/amazing-feature
   ```

7. **Create Pull Request**
   - Go to the original repository
   - Click "New Pull Request"
   - Select your fork and branch
   - Fill in the PR template
   - Submit for review

### Pull Request Checklist

- [ ] Code follows the project's coding standards
- [ ] Self-review of code completed
- [ ] Comments added for complex code
- [ ] Documentation updated (if applicable)
- [ ] No new warnings generated
- [ ] Tests added/updated (if applicable)
- [ ] All tests pass
- [ ] PR title follows commit message convention

## 💻 Coding Standards

### JavaScript/React

- Use functional components with hooks
- Follow React best practices
- Use meaningful variable and function names
- Keep components small and focused
- Use prop-types or TypeScript for type checking

### Code Style

```javascript
// ✅ Good
const calculateDistance = (lat1, lon1, lat2, lon2) => {
  const R = 6371; // Earth radius in km
  const dLat = toRad(lat2 - lat1);
  const dLon = toRad(lon2 - lon1);
  
  return calculateHaversine(dLat, dLon, lat1, lat2);
};

// ❌ Bad
const calc = (a, b, c, d) => {
  let x = 6371;
  return x * 2;
};
```

### File Organization

```
src/
├── components/       # Reusable UI components
├── hooks/           # Custom React hooks
├── utils/           # Utility functions
├── constants/       # Constants and configurations
└── styles/          # Global styles
```

### Naming Conventions

- **Components**: PascalCase (`MapView.jsx`)
- **Hooks**: camelCase with 'use' prefix (`useGeoLocation.js`)
- **Utils**: camelCase (`calculateDistance.js`)
- **Constants**: SCREAMING_SNAKE_CASE (`MAX_SPEED`)

## 📝 Commit Guidelines

We follow the [Conventional Commits](https://www.conventionalcommits.org/) specification.

### Commit Message Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Code style changes (formatting, missing semicolons, etc)
- **refactor**: Code change that neither fixes a bug nor adds a feature
- **perf**: Performance improvement
- **test**: Adding missing tests or correcting existing tests
- **chore**: Changes to build process or auxiliary tools

### Examples

```bash
feat(tracking): add real-time speed calculation

- Implement speed tracking using GPS data
- Add average and maximum speed metrics
- Update UI to display speed information

Closes #123
```

```bash
fix(geolocation): handle permission denied error

- Add error handling for denied location permissions
- Display user-friendly error message
- Gracefully degrade functionality

Fixes #456
```

## 🧪 Testing

### Running Tests

```bash
# Run all tests
npm test

# Run tests in watch mode
npm run test:watch

# Run tests with coverage
npm run test:coverage
```

### Writing Tests

- Write tests for all new features
- Maintain at least 80% code coverage
- Use descriptive test names
- Follow AAA pattern (Arrange, Act, Assert)

```javascript
describe('calculateDistance', () => {
  it('should calculate distance between two coordinates', () => {
    // Arrange
    const lat1 = 40.7128;
    const lon1 = -74.0060;
    const lat2 = 34.0522;
    const lon2 = -118.2437;
    
    // Act
    const distance = calculateDistance(lat1, lon1, lat2, lon2);
    
    // Assert
    expect(distance).toBeCloseTo(3944, 0);
  });
});
```

## 📚 Documentation

- Update README.md for new features
- Add JSDoc comments for functions
- Update API documentation
- Include code examples

### JSDoc Example

```javascript
/**
 * Calculates the distance between two geographical coordinates
 * using the Haversine formula.
 * 
 * @param {number} lat1 - Latitude of first point
 * @param {number} lon1 - Longitude of first point
 * @param {number} lat2 - Latitude of second point
 * @param {number} lon2 - Longitude of second point
 * @returns {number} Distance in kilometers
 */
const calculateDistance = (lat1, lon1, lat2, lon2) => {
  // Implementation
};
```

## 🎨 Style Guide

### CSS/Tailwind

- Use Tailwind utility classes
- Follow mobile-first approach
- Keep custom CSS minimal
- Use consistent spacing scale

```jsx
// ✅ Good
<div className="flex items-center justify-between p-4 bg-slate-800 rounded-lg">
  <span className="text-sm font-medium text-white">Distance</span>
  <span className="text-lg font-bold text-blue-400">10.5 km</span>
</div>

// ❌ Bad
<div style={{display: 'flex', padding: '15px'}}>
  <span style={{color: 'white'}}>Distance</span>
</div>
```

## 🏆 Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes
- Project documentation

## 💬 Questions?

Feel free to:
- Open an issue for questions
- Join our discussions
- Reach out via email

## 📄 License

By contributing, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to GPS Tracker Pro! 🎉
