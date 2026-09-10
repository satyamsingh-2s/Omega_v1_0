# Omega v1.0 Codebase Overview

This document provides a comprehensive technical overview of the Omega v1.0 Android application, structured from the perspective of an SDE1 Android Engineer.

---

## 1. High-Level Architecture
The project follows a standard **MVVM (Model-View-ViewModel)** architecture tailored for **Jetpack Compose**.

*   **View (Presentation)**: Built entirely with Jetpack Compose. Screens are stateless where possible, reacting to state emitted by ViewModels.
*   **ViewModel**: Manages UI state using `StateFlow` and handles user interactions by calling the Repository or Engines.
*   **Model/Data Layer**: Uses a Repository pattern to abstract data sources (Room DB and AI APIs).

---

## 2. Layered Structure & Responsibility

### A. Presentation Layer (`ui/`)
*   **`screens/`**: Contains the Composable UI for each screen (e.g., `UnplannedProjectScreen`).
*   **`viewmodel/`**: Holds logic for fetching data and updating `uistate`.
*   **`uistate/`**: Simple data classes representing the "source of truth" for the UI.
*   **`navigation/`**: Defines routes and orchestrates the transition between screens via `OmegaNavGraph`.
*   **`components/`**: Reusable UI building blocks (Custom buttons, status bars).

### B. Domain/Business Logic Layer (`omega_engines/`)
*   Contains core algorithmic logic that isn't strictly UI or Data.
    *   **`pomodoro_engine/`**: Manages timing cycles.
    *   **`layout_engine/`**: Handles tree-based visualization logic.
    *   **`estimation_engine/`**: Logic for calculating project durations.

### C. AI Layer (`ai/`)
*   Split into `branch_a` (Retrofit-based) and `branch_b` (Google SDK-based).
*   **`repository/`**: Abstraction for generating content (workspace structures) using Google Gemini.
*   **`prompt/`**: Contains `AiPromptBuilder` which formats raw user input into structured instructions for the AI.

### D. Data Layer (`data_layer/`)
*   **`database/`**: Room database setup (`OmegaDatabase`).
*   **`dao/`**: Room Data Access Objects for CRUD operations.
*   **`entites/`**: Physical database table mappings.
*   **`omega_repository/`**: The primary `Omega_Repository` which coordinates multiple DAOs and provides a clean API to the ViewModels.

---

## 3. Dependency Injection (DI)
The project **does not use Hilt or Dagger**. Instead, it uses **Manual Dependency Injection**.

*   **Implementation**: In `OmegaNavGraph.kt`, all repositories and viewmodels are instantiated using `remember`.
*   **Pros**: Simple for a v1, no annotation processing overhead.
*   **Cons**: `OmegaNavGraph` has become a "God Object" responsible for creating every single object in the app.

---

## 4. Navigation & Coordination
*   **Mechanism**: Uses `androidx.navigation.compose`.
*   **Screen Coordination**: Screens communicate via the `navController`. Data layer coordination happens in the ViewModel; when a user takes an action, the ViewModel updates the Repository, which emits a new Flow, which updates the UI.
*   **Shared State**: Global states like the `SessionStatusBar` are managed by observing a common Repository flow across different parts of the UI.

---

## 5. File-by-File Breakdown (Key Files)

| File | Responsibility |
| :--- | :--- |
| `MainActivity.kt` | Entry point. Installs Splash Screen and sets up the `OmegaNavGraph`. |
| `OmegaNavGraph.kt` | **God File**. Handles navigation, DI, and lifecycle of all ViewModels/Repositories. |
| `Omega_Repository.kt` | Orchestrator for data. Combines logic from `PlannedProjectDao`, `PhaseDao`, `SessionDao`, etc. |
| `UnplannedProjectScreen.kt` | Complex UI handling hierarchical trees, breadcrumbs, and real-time progress. |
| `AiRepository.kt` | Wraps `GenerativeModel` to communicate with Gemini. |
| `PomodoroEngine.kt` | State machine for work/break cycles. |

---

## 6. SDE1 Perspective & TODOs

### Critical Issues
> [!CAUTION]
> **GOD FILE**: `OmegaNavGraph.kt` is doing way too much. It handles navigation *and* instantiates every single dependency. If this file breaks, the entire app dies.

### Improvements & TODOs
- [ ] **Migrate to Hilt**: Manual DI is becoming unmanageable. Moving to Hilt will decouple `OmegaNavGraph` from object creation.
- [ ] **Clean Up AI Packages**: Currently there are `branch_a` and `branch_b`. This should be unified into a single stable AI module.
- [ ] **Repository Splitting**: `Omega_Repository` is over 1000 lines. It should be decomposed into `ProjectRepository`, `SessionRepository`, and `DailyRecordRepository`.
- [ ] **UI Logic in ViewModels**: Some ViewModels contain recursive search logic (`findNodeById`). This should be moved to a helper or the Repository level to keep ViewModels lean.
- [ ] **Hardcoded Strings**: Many strings in the UI are hardcoded. Move these to `strings.xml` for localization support.
- [ ] **Error Handling**: AI network calls use a simple `try-catch`. Implement a more robust `Resource<T>` or `Result<T>` wrapper for the UI to handle error states gracefully.

---

## 7. Rules Followed
*   **Unplanned Leaf Rule**: Sessions can only be started on leaf nodes (nodes without children).
*   **Single Branch Expansion**: Only one path in the unplanned project tree is expanded at a time to reduce visual clutter.
*   **Reactive State**: Use of Kotlin `Flow` and `collectAsStateWithLifecycle` (where applicable) to ensure the UI is always in sync with the DB.
