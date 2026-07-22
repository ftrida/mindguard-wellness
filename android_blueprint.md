# MindGuard AI Android Architecture Blueprint
## Jetpack Compose & Clean Architecture Android Design

This document details the production-ready Android application architecture for MindGuard AI. Designed using **Clean Architecture** and the **MVVM** pattern, the client features declarative Jetpack Compose interfaces, offline-first Room caching, background synchronization via WorkManager, and dependency injection using Hilt.

---

## 1. Complete Android Architecture

The Android application applies Google's guide to app architecture, separating the codebase into three layers: Presentation, Domain, and Data.

```
       ┌────────────────────────────────────────────────────────┐
       │                   PRESENTATION LAYER                   │
       │  Jetpack Compose Screen ◄──► ViewModel (StateFlow UI)  │
       └───────────────────────────┬────────────────────────────┘
                                   │ Invokes Use Cases
                                   ▼
       ┌────────────────────────────────────────────────────────┐
       │                      DOMAIN LAYER                      │
       │           Use Cases  ◄──► Repository Interfaces         │
       └───────────────────────────┬────────────────────────────┘
                                   │ Implements Interface
                                   ▼
       ┌────────────────────────────────────────────────────────┐
       │                       DATA LAYER                       │
       │       Repository Impl  ◄──► Room Cache  /  Retrofit     │
       └────────────────────────────────────────────────────────┘
```

### Core Architecture Guidelines:
1.  **Strict Unidirectional Data Flow (UDF):** The UI emits events to the ViewModel; the ViewModel processes these events and updates the state (`StateFlow`), which triggers recomposition in the UI.
2.  **Clean Domain Boundaries:** The Domain layer contains pure Kotlin business logic. It does not reference Android SDK classes (such as Context or ViewModels) or external libraries.
3.  **Offline-First Strategy:** The Presentation layer reads data from Use Cases that query the Repository interface. The Repository acts as a mediator, returning local database records (Room) and updating them in the background via Retrofit API calls.

---

## 2. Folder Structure

```
com/mindguard/
├── core/
│   ├── common/                  # Global extensions, constants, and custom utility functions
│   ├── navigation/              # Type-safe Navigation Compose routes and graphs
│   ├── theme/                   # Material Design 3 color palettes, typography, shapes
│   ├── security/                # Encrypted DataStore, key generators, biometric setups
│   └── di/                      # Global Hilt Dependency Injection Modules
├── data/                        # Data Access Layer
│   ├── local/
│   │   ├── db/                  # Room Database configuration
│   │   ├── dao/                 # Room DAOs (LifestyleDao, JournalDao)
│   │   ├── entity/              # Local DB Entity Schemas (Room schema mapping)
│   │   └── datastore/           # Encrypted Preferences DataStore (User tokens, preferences)
│   ├── remote/
│   │   ├── api/                 # Retrofit Endpoints interfaces (AuthApi, TwinApi)
│   │   └── dto/                 # Backend DTOs (Request/Response schemas)
│   └── repository/              # Repository Implementations (AuthRepositoryImpl)
├── domain/                      # Core Business Domain Rules
│   ├── model/                   # Pure Kotlin models (User, DailyLog, StressEstimate)
│   ├── repository/              # Repository Interfaces (Abstract declarations)
│   └── usecase/                 # Granular Use Cases (GetStressReportUseCase, SyncLogsUseCase)
├── features/                    # Presentation Layer (Feature-by-Feature)
│   ├── auth/                    # Login, Register, Verification Screens & ViewModels
│   ├── dashboard/               # Core Dashboard Screen & ViewModel
│   ├── journal/                 # Journaling, Rich Text Editing & ViewModel
│   ├── twin/                    # Digital Twin metrics details & ViewModel
│   ├── coach/                   # AI Coach chat interface & ViewModel
│   └── profile/                 # Profile, Preferences, Device details & ViewModel
└── worker/                      # WorkManager background synchronization tasks
```

---

## 3. Module Responsibilities

*   `core/`: Houses cross-cutting utilities (e.g., custom UI components, MD3 styling, base exceptions).
*   `data/`: Manages database transactions, network responses, and shared preferences. It is responsible for mapping backend DTOs to internal entities.
*   `domain/`: Defines the business interfaces of the application.
*   `features/`: Groups Compose screens, ViewModels, and state definitions. This structure allows the project to be modularized into independent Gradle modules in the future.
*   `worker/`: Defines background execution blocks, such as syncing data when network connectivity returns.

---

## 4 & 5. Navigation Graph & Screen Flow

We use type-safe Navigation Compose, defining destinations as serializable objects or sealed classes.

```
       [Splash Screen] 
              │
              ▼
      [Onboarding Flow] 
              │
              ▼
   [Auth Screen: Login/Register] 
              │
              ▼
    [Main Bottom Navigation]
    ├── Dashboard Screen
    ├── Lifestyle Detail Screen
    ├── AI Coach Chat Screen
    ├── Analytics Graph Screen
    └── Profile Settings Screen
```

### Route Definitions:
```kotlin
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object VerifyEmail : Screen("verify_email")
    object Dashboard : Screen("dashboard")
    object JournalDetail : Screen("journal_detail/{date}") {
        fun createRoute(date: String) = "journal_detail/$date"
    }
    object CoachChat : Screen("coach_chat")
    object StressReport : Screen("stress_report")
}
```

---

## 6. MVVM Design

All screens interact with dedicated ViewModels, which manage screen state using `StateFlow` and handle one-time UI events (e.g., navigation or showing a Toast) using `SharedFlow`.

```
         ┌────────────────────────────────────────────────────────┐
         │                       VIEWMODEL                        │
         │  Exposes UI State: StateFlow<UiState<DashboardData>>   │
         │  Exposes UI Event: SharedFlow<UiEvent>                 │
         └───────────────────────────▲────────────────────────────┘
               │ Event (e.g. LogMood)│ Recomposition on state change
               ▼                     │
         ┌───────────────────────────┴────────────────────────────┐
         │                    COMPOSE SCREEN                      │
         │   Observes State: val state by vm.uiState.collectAsState()  │
         └────────────────────────────────────────────────────────┘
```

### ViewModel Implementation Pattern:
```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardUseCase: GetDashboardUseCase,
    private val logMoodUseCase: LogMoodUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<DashboardData>>(UiState.Loading)
    val uiState: StateFlow<UiState<DashboardData>> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    init {
        fetchDashboardData()
    }

    fun handleIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.Refresh -> fetchDashboardData()
            is DashboardIntent.SelectMood -> submitMood(intent.score)
        }
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                getDashboardUseCase().collect { data ->
                    _uiState.value = UiState.Success(data)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Failed to fetch dashboard data")
            }
        }
    }
}
```

---

## 7. Repository Design

Repositories act as a single point of access for all data operations, coordinating Room database access and Retrofit API requests.

```kotlin
class LifestyleRepositoryImpl @Inject constructor(
    private val lifestyleDao: LifestyleDao,
    private val lifestyleApi: LifestyleApi
) : LifestyleRepository {

    override fun getDailyLog(userId: Long, date: LocalDate): Flow<DailyLog> {
        return flow {
            // 1. Emit cached data immediately to keep UI responsive
            val cached = lifestyleDao.getLogByDate(userId, date.toString())?.toDomain()
            if (cached != null) emit(cached)

            // 2. Fetch latest data from backend, update local cache, and emit update
            try {
                val response = lifestyleApi.fetchDailyLog(date.toString())
                if (response.isSuccessful && response.body() != null) {
                    val remoteLog = response.body()!!.toEntity(userId)
                    lifestyleDao.insertLog(remoteLog)
                    emit(remoteLog.toDomain())
                }
            } catch (e: Exception) {
                // Fail silently if offline, preserving the emitted local cache
            }
        }
    }
}
```

---

## 8. Network Layer Design

We configure OkHttp and Retrofit with custom interceptors to handle token rotation and authorization headers automatically.

```
  [HTTP Outbound Request] ──► [JWT Interceptor] ──► [Response Received]
                                                         │
                                               Is Status 401 Unauthorized?
                                                         │
                                                         ▼
                                            [Refresh Token Interceptor]
                                                         │
                                             Query /auth/refresh Endpoint
                                                         │
                                        ┌────────────────┴────────────────┐
                                        ▼ Success                         ▼ Fail
                                  Save new tokens;                  Clear Session;
                                  Retry original request            Redirect to Login
```

*   **JWT Authorization Interceptor:** Injects the active access token into the `Authorization: Bearer <token>` header of all outgoing requests.
*   **Token Refresh Authenticator:** If an API call returns `HTTP 401 Unauthorized`, this authenticator pauses the request chain, calls `/api/v1/auth/refresh` to obtain new tokens, updates the token store, and retries the original request.
*   **Logging Interceptor:** Formats API request and response bodies for log diagnostics, disabled in release builds.

---

## 9. Offline Sync Strategy

Offline support is handled by a local Room database and background synchronization tasks managed by Android WorkManager.

```
  ┌────────────────────────────────────────────────────────┐
  │ 1. Write Log Offline:                                  │
  │    - Insert record into Room with is_synced = FALSE    │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ 2. Schedule Sync Work:                                 │
  │    - WorkManager.enqueueUniqueWork("SYNC_WORK")        │
  │    - Constraints: NETWORK_CONNECTED                    │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ 3. Execute Worker:                                     │
  │    - Query Room for all is_synced = FALSE logs         │
  │    - Send logs to POST /api/v1/sync/bulk               │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ 4. Sync Confirmation:                                  │
  │    - Update Room records: sets is_synced = TRUE        │
  └────────────────────────────────────────────────────────┘
```

---

## 10. State Management Strategy

UI state is represented as a sealed class, ensuring all possible screen states (Loading, Success, Error) are handled explicitly by the Compose UI layer.

```kotlin
sealed interface UiState<out T> {
    object Idle : UiState<Nothing>
    object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
```

Compose screens use a `switch` block (`when`) to render the appropriate UI based on the current state:

```kotlin
@Composable
fun DashboardScreen(state: UiState<DashboardData>) {
    when (state) {
        is UiState.Loading -> CircularProgressIndicator()
        is UiState.Success -> DashboardContent(state.data)
        is UiState.Error -> ErrorState(state.message)
    }
}
```

---

## 11. UI Component Library (Custom MindGuard UI)

MindGuard AI implements a custom Material Design 3 theme with the following reusable components:
1.  **Glassmorphic Card:** A customized card component featuring a translucent background, subtle border, and background blur effects.
2.  **Interactive Habit Chart:** Reusable canvas-based charts displaying screen time, step counts, and sleep trends over weekly and monthly periods.
3.  **Pomodoro Progress Ring:** A circular canvas component with custom animations that visualizes active focus intervals and rest phases.
4.  **Emoji Mood Grid:** An interactive selector containing custom-styled emojis for logging daily mood scores (1-5 scale).

---

## 12. Theme Design

We define custom Material Design 3 Color Schemes to support Light and Dark modes.

```kotlin
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF62D2C3),       // Sleek Mint Accent
    secondary = Color(0xFF90CAF9),     // Ocean Blue Accent
    background = Color(0xFF0F172A),    // Dark Navy
    surface = Color(0xFF1E293B),       // Glass Slate
    onPrimary = Color(0xFF003730),
    onBackground = Color(0xFFF8FAFC)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A60),
    secondary = Color(0xFF1976D2),
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color(0xFFFFFFFF),
    onBackground = Color(0xFF0F172A)
)
```

Typography is configured to use the **Inter** font family, defining clean hierarchies for headings, body copy, and labels.

---

## 13. Security Design

1.  **Encrypted Token Storage:** User sessions and JWT tokens are saved using **Encrypted DataStore**, which automatically encrypts keys and values using Android KeyStore-backed AES-256 keys.
2.  **Certificate Pinning:** The network layer configuration includes certificate pinning definitions. OkHttp limits API connection trust to the specific SSL certificate fingerprint of our FastAPI server on Render, mitigating middle-in-the-middle (MITM) risks.
3.  **R8 & ProGuard Optimization:** Release builds apply R8 rules to obfuscate package names, class names, and parameters, preventing reverse engineering of application source code.

---

## 14. Performance Strategy

*   **Lazy Loading lists:** Screens with high scroll volumes use `LazyColumn` or `LazyRow` layout builders. Items inside are configured with unique keys to prevent redundant recompositions.
*   **Optimized Image Delivery:** The Coil image loading library handles profile avatar caching, automatically requesting cropped images and managing network download pools.
*   **Baseline Profiles:** Release builds include compiled baseline profiles. This pre-compiles core startup paths on the device, minimizing application start times and eliminating first-frame UI stuttering.

---

## 15. API Integration Strategy

We configure API URLs dynamically using Gradle build configurations:

```groovy
android {
    buildTypes {
        release {
            buildConfigField "String", "BASE_URL", "\"https://api.mindguard.ai/api/v1/\""
        }
        debug {
            buildConfigField "String", "BASE_URL", "\"https://api-staging.mindguard.ai/api/v1/\""
        }
    }
}
```

The Retrofit builder references `BuildConfig.BASE_URL` to instantiate endpoints, preventing hardcoded IP addresses or hostname changes from breaking compiled builds.

---

## 16. Background Worker Strategy

The application leverages **Android WorkManager** for scheduling system-critical, deferred tasks:
1.  **`SyncLogsWorker`:** Enqueued as a unique, constrained task (`Constraints(NetworkType.CONNECTED)`). Triggered when the device regains internet connection to submit the local sync queue to the `/api/v1/sync/bulk` endpoint.
2.  **`FetchTwinWorker`:** Enqueued as a periodic worker that runs every 24 hours at night, updating the local database cache with the user's latest twin baseline parameters.

---

## 17. Future Scalability Plan: Multi-Module Architecture

To support scaling development teams and codebases, the single-module structure can be divided into a **modular multi-module configuration**:

```
                              [ :app ]
                                 │
         ┌───────────────────────┼───────────────────────┐
         ▼                       ▼                       ▼
   [ :feature:dashboard ]  [ :feature:auth ]    [ :feature:coach ]
         │                       │                       │
         └───────────┬───────────┴───────────┬───────────┘
                     ▼                       ▼
              [ :core:model ]         [ :core:network ]
```

*   **`:app` Module:** Contains the main application class and coordinates navigation routing between screens.
*   **`:feature` Modules:** Houses localized Jetpack Compose screens, ViewModels, and state variables for specific features. These modules do not depend on one another.
*   **`:core` Modules:** Common utility packages containing network configurations, database instances, and shared models.

---

## 18. Android Development Roadmap

```
  ┌────────────────────────────────────────────────────────┐
  │ Phase 1: Hilt, Room DB & Mock Repositories             │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 2: Retrofit Network & OkHttp Integrations        │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 3: Auth flow Compose Screens & Session Cache     │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 4: Core Logging Screens & Offline Sync Work      │
  └───────────────────────────┬────────────────────────────┘
                              │
                              ▼
  ┌────────────────────────────────────────────────────────┐
  │ Phase 5: Digital Twin, AI Coach & Dashboard UI         │
  └────────────────────────────────────────────────────────┘
```

1.  **Phase 1 (Core Foundations):** Configure Hilt dependencies, build Room DB schemas, define standard repository interfaces, and initialize mock repositories for testing UI screens.
2.  **Phase 2 (Networking):** Implement OkHttp interceptors for token refresh, register base Retrofit API interfaces, and write domain mappings for API responses.
3.  **Phase 3 (Auth UI):** Build Splash, Onboarding, Login, and Registration screens using Compose. Implement token caching within Encrypted DataStore.
4.  **Phase 4 (Habit Tracker UI & Sync):** Design logging forms, calendar integrations, and charts. Implement WorkManager background synchronization tasks.
5.  **Phase 5 (Dashboard & Coach UI):** Build the home dashboard with dynamic widgets, integrate the AI Coach chat view with typing animations, and perform UI performance optimization runs.
