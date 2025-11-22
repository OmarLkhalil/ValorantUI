# 🤝 Contributing to Valorant UI

First off, thank you for considering contributing to Valorant UI! 🎉

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How Can I Contribute?](#how-can-i-contribute)
- [Development Setup](#development-setup)
- [Pull Request Process](#pull-request-process)
- [Coding Guidelines](#coding-guidelines)
- [Commit Message Guidelines](#commit-message-guidelines)

## 📜 Code of Conduct

This project adheres to a code of conduct. By participating, you are expected to uphold this code. Please be respectful and professional in all interactions.

## 🚀 How Can I Contribute?

### Reporting Bugs 🐛

Before creating bug reports, please check existing issues. When creating a bug report, include:

- **Clear title and description**
- **Steps to reproduce** the behavior
- **Expected behavior**
- **Actual behavior**
- **Screenshots** (if applicable)
- **Device/OS information**
- **App version**

### Suggesting Features 💡

Feature suggestions are welcome! Please include:

- **Clear description** of the feature
- **Use case** - Why is this feature needed?
- **Mockups or examples** (if applicable)
- **Alternative solutions** you've considered

### Code Contributions 👨‍💻

1. **Fork** the repository
2. **Clone** your fork
3. **Create a branch** for your feature
4. **Make your changes**
5. **Test thoroughly**
6. **Submit a pull request**

## 🛠️ Development Setup

### Prerequisites

```bash
# Check your tools
java -version        # Should be JDK 17+
kotlin -version      # Should be 2.2.21+
```

### Setup Steps

```bash
# 1. Fork and clone
git clone https://github.com/YOUR_USERNAME/ValorantUI.git
cd ValorantUI

# 2. Create a branch
git checkout -b feature/amazing-feature

# 3. Open in Android Studio
# File -> Open -> Select project directory

# 4. Sync Gradle
./gradlew sync

# 5. Run tests to verify setup
./gradlew test
```

## 🔄 Pull Request Process

### Before Submitting

- [ ] Code follows project style guidelines
- [ ] Tests are written and passing
- [ ] Documentation is updated
- [ ] Commit messages follow conventions
- [ ] Branch is up to date with main

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
Describe the tests you ran

## Screenshots
Add screenshots if applicable

## Checklist
- [ ] My code follows the style guidelines
- [ ] I have performed a self-review
- [ ] I have commented my code
- [ ] I have updated the documentation
- [ ] My changes generate no new warnings
- [ ] I have added tests
- [ ] All tests pass
```

## 📝 Coding Guidelines

### Kotlin Style

Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)

```kotlin
// ✅ Good
class AgentsViewModel(
    private val useCase: AgentsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(AgentsState())
    val state: StateFlow<AgentsState> = _state.asStateFlow()
}

// ❌ Bad
class AgentsViewModel(private val useCase:AgentsUseCase):ViewModel(){
    private val _state=MutableStateFlow(AgentsState())
    val state:StateFlow<AgentsState>=_state.asStateFlow()
}
```

### Architecture Patterns

- **MVVM** for presentation layer
- **Repository Pattern** for data layer
- **UseCase Pattern** for business logic
- **State Hoisting** in Compose

### File Organization

```
domain/
├── model/          # Data classes only
├── repository/     # Interfaces only
└── usecase/        # Business logic

data/
├── remote/         # API & DTOs
├── local/          # Database
└── repository/     # Implementations

presentation/
├── viewmodel/      # ViewModels
└── uistates/       # States & Intents
```

### Naming Conventions

```kotlin
// Classes: PascalCase
class AgentsViewModel

// Functions: camelCase
fun fetchAgents()

// Constants: UPPER_SNAKE_CASE
const val BASE_URL = "https://valorant-api.com"

// Variables: camelCase
val agentsList = listOf()

// Composables: PascalCase
@Composable
fun AgentCard()
```

### Comments

```kotlin
// ✅ Good - Explain WHY, not WHAT
// Cache agents to improve offline experience
private fun cacheAgents(agents: List<Agent>)

// ❌ Bad - Obvious comment
// This function caches agents
private fun cacheAgents(agents: List<Agent>)
```

## 💬 Commit Message Guidelines

Follow [Conventional Commits](https://www.conventionalcommits.org/)

### Format

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting)
- `refactor`: Code refactoring
- `test`: Adding/updating tests
- `chore`: Build/tool changes

### Examples

```bash
# ✅ Good
feat(agents): add pull-to-refresh functionality
fix(weapons): resolve crash on weapon details screen
docs(readme): update installation instructions
test(viewmodel): add tests for AgentsViewModel

# ❌ Bad
update code
fixed bug
changes
wip
```

## 🧪 Testing Guidelines

### Write Tests For

- **ViewModels** - All business logic
- **Repositories** - Data operations
- **UI Components** - User interactions
- **Integration** - End-to-end flows

### Test Structure

```kotlin
@Test
fun `feature description - expected behavior`() = runTest {
    // Given - Setup
    val viewModel = AgentsViewModel(mockUseCase)
    
    // When - Action
    viewModel.sendIntent(FetchAgents)
    
    // Then - Assertion
    assertEquals(expected, viewModel.state.value)
}
```

### Run Tests

```bash
# All tests
./gradlew test

# Specific module
./gradlew :composeApp:test

# With coverage
./gradlew :composeApp:testDebugUnitTestCoverage
```

## 📚 Additional Resources

- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/)
- [Material Design 3](https://m3.material.io/)
- [Ktor Documentation](https://ktor.io/docs/)

## 🎯 Areas for Contribution

### High Priority
- [ ] iOS implementation
- [ ] Search and filter functionality
- [ ] Unit test coverage increase
- [ ] Performance optimizations

### Medium Priority
- [ ] Maps information screen
- [ ] Ranks system
- [ ] Weapon comparison
- [ ] Animation improvements

### Good First Issues
- [ ] UI translations
- [ ] Theme color customization
- [ ] Documentation improvements
- [ ] Error message improvements

## ❓ Questions?

Feel free to:
- Open an issue
- Start a discussion
- Contact maintainers

## 🙏 Thank You!

Your contributions make this project better! 

---

**Happy Coding! 🚀**

