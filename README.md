# 🧭 Omega v1.0  
**Project Time Estimation & Discipline Tool (Android)**

Omega is a local-only Android application designed to help users **estimate project time**, **track real effort manually**, and **confront the difference honestly**.

Omega does not motivate you.  
It does not optimize your workflow.  
It does not fix your mistakes.

It shows you reality.

---

## 📌 What Omega Is

Omega helps you:

- Estimate projects using **fixed, structured phases**
- Track work with a **manual, phase-level timer**
- Compare **estimated vs actual** time clearly
- Build discipline through **explicit responsibility**

Everything is **local**, **deterministic**, and **intentional**.

---

## ❌ What Omega Is NOT

Omega intentionally does **not** include:

- ❌ Background timers  
- ❌ Notifications  
- ❌ Auto-resume or auto-stop  
- ❌ AI-based estimation  
- ❌ Cloud sync or accounts  
- ❌ Gamification, streaks, or rewards  
- ❌ Editing estimates after confirmation  

If you forget to stop a timer, Omega will not correct it for you.

That behavior is **by design**.

---

## 🧠 Core Philosophy

> **Discipline over convenience**  
> **Truth over comfort**  
> **Responsibility over automation**

Omega reflects what happened.  
It does not soften it.

---

## 🧱 Fixed Project Phases

All projects are estimated using the same **non-editable phases**:

1. IDEA  
2. RESEARCH  
3. DEVELOPMENT  
4. DEBUG  
5. POLISH  

This keeps estimation consistent and comparable across projects.

---

## ⏱️ Timer & Session Model

- Time tracking is **manual**
- Sessions start only via **START**
- Sessions end only via **STOP**
- Only **one active session** exists globally
- Actual time is derived from **stored sessions**
- Elapsed time is reconstructed from the database

No background services.  
No silent tracking.  
No lifecycle magic.

---

## 🧭 App Flow

1. **Create Project**
   - Enter project name
   - Select experience level
   - View recent projects (read-only)

2. **Estimate Project**
   - Choose complexity & scope per phase
   - Estimation is **one-way**
   - Estimates cannot be edited afterward

3. **Dashboard**
   - View estimated vs actual time per phase
   - See which phase (if any) is currently running

4. **Phase Timer**
   - Start / Stop manual timer
   - Live elapsed time display
   - Back navigation intentionally restricted
   - Active sessions must be resolved explicitly

---

## 🗃️ Data Model (Simplified)

- **Project**
  - name, experience, createdAt

- **Phase**
  - phaseType, estimatedMinutes

- **Session**
  - startTime, endTime, duration (seconds)

The database is the **single source of truth**.

---

## 🏗️ Architecture Overview

Omega follows a strict, layered architecture designed to prevent
logic leakage, background behavior, and accidental automation.

---

### 🔹 High-Level Architecture

```mermaid
flowchart TD
    UI[Compose Screens\n(UI Only)]
    NAV[NavGraph\n(Wiring & Lifecycle)]
    VM[ViewModels\n(State & Intent)]
    REPO[Repository\n(Rules & Coordination)]
    DAO[DAO Layer\n(SQL Only)]
    DB[(Room Database\nSource of Truth)]

    UI --> NAV
    NAV --> VM
    VM --> REPO
    REPO --> DAO
    DAO --> DB

    DB --> DAO
    DAO --> REPO
    REPO --> VM
    VM --> UI

flowchart LR
    Screen[Screen\n(UI)]
    VM[ViewModel]
    Repo[Repository]
    DAO[DAO]

    Screen -- user intent --> VM
    VM -- commands --> Repo
    Repo -- queries --> DAO
    DAO -- results --> Repo
    Repo -- state --> VM
    VM -- UI state --> Screen

sequenceDiagram
    participant U as User
    participant UI as PhaseTimerScreen
    participant VM as PhaseTimerViewModel
    participant R as Repository
    participant D as DAO
    participant DB as Room DB

    U->>UI: Tap START
    UI->>VM: start(phaseId)
    VM->>R: request start
    R->>D: hasActiveSession()
    D->>DB: query
    DB-->>D: none active
    D-->>R: allowed
    R->>D: insert session(startTime)

    U->>UI: Tap STOP
    UI->>VM: stop()
    VM->>R: stopSession()
    R->>D: update endTime + duration


