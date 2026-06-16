package com.example.omega_v1_0.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.omega_v1_0.data_layer.database.DatabaseProvider
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.ui.screens.CreateProjectScreen
import com.example.omega_v1_0.ui.screens.DailyRecordDetailsScreen
import com.example.omega_v1_0.ui.screens.DailyRecordHistoryScreen
import com.example.omega_v1_0.ui.screens.DailyRecordScreen
import com.example.omega_v1_0.ui.screens.EstimateScreen
import com.example.omega_v1_0.ui.screens.LauncherScreen
import com.example.omega_v1_0.ui.screens.MainScreen
import com.example.omega_v1_0.ui.screens.PhaseTimerScreen
import com.example.omega_v1_0.ui.screens.ProjectDashboardScreen
import com.example.omega_v1_0.ui.theme.OmegaDarkTheme
import com.example.omega_v1_0.ui.viewmodel.CreateProjectViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordDetailsViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordHistoryViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordViewModel
import com.example.omega_v1_0.ui.viewmodel.DashboardViewModel
import com.example.omega_v1_0.ui.viewmodel.EstimateScreenViewModel
import com.example.omega_v1_0.ui.viewmodel.PhaseTimerViewModel
import com.example.omega_v1_0.ui.viewmodel.ToDoListViewModel

/**
 * NavHost = container
 *
 * composable = one screen
 *
 * navArgument = enforced context
 */

/*
i am not injecting hilt dependecy injection, as we are manually creating viewmodel , repositroy every time and wiring them,
in v2 I will use them
 */

@Composable
fun OmegaNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Launcher.route
    ) {

        // here the entry point of the app to mainScreen
        composable(
            route = Screen.Launcher.route
        ) {
            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }
//            val db = remember {
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase:: class.java,
//                    "omega_db"
//                ).build()
//            }


            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }
            OmegaDarkTheme {
                LauncherScreen(repository, navController)
            }
        }

        composable(Screen.MainScreen.route) {

            OmegaDarkTheme {
                MainScreen(
                    onPlannedWorkClick = {
                        navController.navigate(Screen.CreateProject.route)
                    },
                    onUnplannedWorkClick = {
                        // Placeholder for now
                    },
                    onDailyRecordClick = {
                        navController.navigate(Screen.DailyRecord.route)
                    }
                )
            }
        }

        composable(Screen.CreateProject.route)    // jab yeah wala composable ka call karnege, tab ek hi value dena hoaga -> route
        {
        // ------------------- performing temporary wiring, as not using DI-------------------
            /**
             * here we created database name omega_db
             * created the repository or connected it to
             * connected OmeganNavGraph to the CreateProjectViewModel and
             * viewModel to repositroy
             * */
            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }
//            val db = remember {
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase:: class.java,
//                    "omega_db"
//                ).build()
//            }


            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao= db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao= db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {
                CreateProjectViewModel(repository)
            }

            // 🔹 NEW: load recent projects ONCE
            LaunchedEffect(Unit) {
                viewModel.loadRecentProjects()
                viewModel.checkActiveSession()
            }

            /**
             * more to learn form line 73 to 85
             */
            // below there are 3 data streams
            val recentProjects by viewModel.recentProjects.collectAsState()
            val projectId by viewModel.createProjectId.collectAsState() // here we collect value of projectId , and it get automatically if changes
            // now we have to collect value of experience, but in viewmodel we have to define the flow taht will give the experinced, right now experince is getting nothing
            val activeSession by viewModel.activeSession.collectAsState()
            // collecting the new state for all projects
            val allProjects by viewModel.allProjects.collectAsState()
            // collection the project to delete  2. also projectToDelete contains all information of particular project, a data type to store that particular type of data
            val projectToDelete by viewModel.projectToDelete.collectAsState()


            CreateProjectScreen (       // here we create the onCreateClicked function, and call CreateProjectScreen with the parameter of OnCreateClicked.
                recentProjects = recentProjects,
                activeSession = activeSession,
                allProjects = allProjects,
                onCreateClicked = {name, experience ->
                viewModel.createProject(name,experience)
                },
                onRecentProjectClicked = { projectId ->
                    navController.navigate(
                        Screen.Dashboard.createRoute(projectId)
                    )
                },
                onStopActiveSession = {
                    viewModel.stopActiveSession()
                },
                onStopSessionAndGoToDashboard = { projectId ->
                    viewModel.stopActiveSession()
                    navController.navigate(
                        Screen.Dashboard.createRoute(projectId)
                    )
                },
                onAllProjectLongPressed = { project ->
                    viewModel.onProjectLongPressed(project)
                    // here project is passed which is long pressed...
                },
                projectToDelete = projectToDelete, // here project is passed which is to be deleted
                onConfirmDelete = {
                    viewModel.confirmDeleteProject()
                },
                onCancelDelete = {
                    viewModel.cancelDelete()
                }
            )
            /**
             * projectId?. check if the projectId is null or not , if not nul
             * then excutes the block & launchedeffect is used to run the block when the key changes/
             * if not use, then block will run in every composition , so launched effect saves.
             */
            projectId?.let {id ->
                // purpose of line is to get the value of experince from creatprojectscreenviewmodel.
                val experience = viewModel.getLatestExperience() ?: return@let

                LaunchedEffect(id) {
                    // navigate to estimate screen with projectId -----
                    navController.navigate(
                        Screen.Estimate.createRoute(id,experience)
                   )
                //                    {
//
//                        // ❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌❌ checking it is created or not
//                        Log.d("OMEGA_DB", "⭕⭕⭕⭕⭕⭕⭕⭕Project created with id=$projectId")
//
//                        // remove CreateProject from backstack
//                        // the bleow line remove every visited screen till CreateProject screen
//                        popUpTo(Screen.CreateProject.route) {
//                            inclusive=true // this inculde creeteproject screen also
//                        }
//                    }
                }
            }



        }
// -------------------- EstimateScreen part ---------------------------------------------------------------------------------------------------------------------
        composable(     // jab yeah wala composalbe ko call karenge, tab mereko do value dena hoga, pahlea -> route, argumets(projectId, experience) total 3 values
            route = Screen.Estimate.route,
            arguments = listOf(   // “This screen requires a value called projectId, and it must be a Long.”
                navArgument("projectId") { type = NavType.LongType },
                navArgument("experience") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val projectId = backStackEntry.arguments?.getLong("projectId") ?: return@composable

            // * imp - navigation passes string, int, long and so on not enum types
            // so we convert into enums, as acroos all app we are using enums, not strings
            val experienceString =
                backStackEntry.arguments?.getString("experience")
                    ?: return@composable

            val experience =
                try {
                    Experience.valueOf(experienceString)
                } catch (e: IllegalArgumentException) {
                    return@composable
                }
            /**
             * above 2 lines, backStackEntry - holds the trace of reaching this screen, by keeping the stack of previous screeesns with arugumesnt(data)
             * basically it gives the argumets to the val projectId of the last screen*/

            val context = LocalContext.current // see in onenote for detail also it creates database
            val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

            val repository = remember {   // again line for creates repository. but by remeber it won't create again and agian safe from recompostion or change of composables
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {  // again line for viewModel creation,
                EstimateScreenViewModel(repository)
            }

            val navigate by viewModel.navigateToDashboard.collectAsState()

            LaunchedEffect(projectId) {
                viewModel.loadProject(projectId)
            }

            val projectName by viewModel.projectName.collectAsState()

            EstimateScreen(
                projectName = projectName,
                experience = experience,
                onEstimateClicked = { phaseInputs, experience ->             // here the function is originally created, from estimateScree.kt it is called, here it performs funciton
                    viewModel.estimateAndSave(
                        projectId = projectId,
                        experience = experience,
                        phaseInputs = phaseInputs
                    )
                }
            )

            if(navigate) {
                LaunchedEffect(Unit) {
                    navController.navigate(
                        Screen.Dashboard.createRoute(projectId)
                    ){
                            popUpTo(Screen.Estimate.route) {
                                inclusive = true
                            }
                        }
                }
            }
        }

// -----------------------------DASHBOARD SCREEN PART---------------------------------------------------------------------------
    // here we did the navigation setup with 3 screens, not define the routes strings.
        composable(
            route = Screen.Dashboard.route,
            // “This screen requires a value called projectId, and it must be a Long.”
            arguments = listOf(
                navArgument("projectId") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            val projectId =
                backStackEntry.arguments?.getLong("projectId") ?: return@composable

            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {
                DashboardViewModel(repository)
            }

            val phases by viewModel.phases.collectAsState()
            val runningPhaseId by viewModel.runningPhaseId.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadDashboard(projectId)
                viewModel.syncRunningState()

            }
    // ---------------- fro project name, automatically called if projdect id is change to give automatic recomposition
            LaunchedEffect(projectId) {
                viewModel.loadProject(projectId)
            }

            val projectName by viewModel.projectName.collectAsState()


            // here the projectDashboard screen is called......
            ProjectDashboardScreen(
                projectName = projectName,
                phases = phases,
                runningPhaseId = runningPhaseId,
                onPhaseClicked = { phaseId ->              // here this function is created, here it performs its function,
                    navController.navigate(               // also when we click any phase onPhaseClicked function is called..... direlty [perfoms certain opooeration and performs navigation, ]
                        Screen.PhaseTimer.createRoute(phaseId)
                    )
                },
                onBack = {
                    navController.popBackStack()
                }
            )

        }
// --------------------------------------- TIMER SCREEN --------------------------------------------------------------------------------
        composable(
            route = Screen.PhaseTimer.route,
            arguments = listOf(
                navArgument("phaseId") { type = NavType.LongType }
            )
        ) {backStackEntry ->

        val phaseId =
            backStackEntry.arguments?.getLong("phaseId") ?: return@composable

            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {                      // here the view model is created and it is told to use the repository,
                PhaseTimerViewModel(repository)
            }

            // --- State collection ---
            val uiState by viewModel.uiState.collectAsState()
            val isRunning by viewModel.isRunning.collectAsState()

            val elapsedSeconds by viewModel.elapsedSeconds.collectAsState()


            // --- Load phase info once ---
            LaunchedEffect(phaseId) {
                viewModel.loadPhase(phaseId)                       // here the view model load data, by calling loadPhase function, now the ui updates automatatically as the data of uistate changes and also becaouse of line 282
                viewModel.syncRunningState(phaseId)
                viewModel.syncElapsedTimeIfRunning()
            }

            // --- UI ---                          // after launched effect the phaseTimerscreen is called with values, and function
            PhaseTimerScreen(
                uiState = uiState,
                isRunning = isRunning,
                runningPhaseName = viewModel.runningPhaseName.collectAsState().value,
                onStart = {
                    viewModel.start(phaseId)
                },
                elapsedSeconds = elapsedSeconds,
                onStop = {
                    viewModel.stop(phaseId)
                    // After stop, refresh phase info
                    viewModel.loadPhase(phaseId)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // -------------------- DailyRecordScreen part ------------------
        composable(Screen.DailyRecord.route) {

            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }

            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {
                DailyRecordViewModel(repository)
            }
            val uiState by viewModel.uiState.collectAsState()

            val toDoListViewModel = remember {
                ToDoListViewModel(repository)
            }
            val toDoUiState by
            toDoListViewModel.uiState.collectAsState()

            val selectedTodoCategory by
            toDoListViewModel.todoCategory.collectAsState()

            val toastMessage by
            toDoListViewModel.showMaxLimitToast.collectAsState()

            toastMessage?.let {

                Toast.makeText(

                    context,

                    it,

                    Toast.LENGTH_SHORT

                ).show()

                toDoListViewModel.onToastShown()
            }



            LaunchedEffect(Unit) {
                viewModel.syncActiveSession()
                viewModel.loadTodaysTotal()
                viewModel.loadRecentSessions()

            }
            // ===== temp theme ==========
//            MaterialTheme(
//                colorScheme = darkColorScheme(
//
//                    // Accent
//                    primary = Color(0xFF8B7FBF),
//
//                    // Backgrounds
//                    background = Color(0xFF141414),
//                    surface = Color(0xFF1B1B1B),
//                    surfaceVariant = Color(0xFF232323),
//
//                    // Text colors
//                    onPrimary = Color(0xFFE4E4E4),
//                    onBackground = Color(0xFFE4E4E4),
//                    onSurface = Color(0xFFE4E4E4),
//                    onSurfaceVariant = Color(0xFF9D9D9D),
//
//                    // Optional extras
//                    outline = Color(0xFF2A2A2A)
//                )
//            )
            OmegaDarkTheme()
            { // =================== temp them ======================
                DailyRecordScreen(
                    todaystotalSeconds = uiState.todaysTotalSeconds,
                    sessionName = uiState.sessionNameInput,
                    activeSessionName = uiState.activeSessionName,
                    onEstimateSelected =
                        viewModel::onEstimateSelected,
                    selectedEstimateMinutes =
                        uiState.selectedEstimateMinutes,
                    onSessionNameChange =
                        viewModel::onSessionNameChanged,
//               // onExpectedDurationChange =
//                    viewModel::onExpectedDurationChanged,
                    onStartSession =
                        viewModel::startSession,
                    sessionStatus = uiState.sessionStatus,
                    onPauseSession = viewModel::pauseSession,
                    onResumeSession = viewModel::resumeSession,
                    onStopSession = viewModel::stopSession,
                    stopwatchSeconds =
                        uiState.stopwatchSeconds,
                    recentSessions = uiState.recentSessions,
                    onHistoryClick = { navController.navigate(Screen.DailyRecordHistory.route) },

                    // ---- To do List section  ----------------
                    todoItems = toDoUiState.items,
                    newTodoText = toDoUiState.newItemText,
                    onTodoTextChanged = toDoListViewModel::onNewItemTextChanged,
                    onAddTodo = toDoListViewModel::addItem,
                    onToggleTodo = toDoListViewModel::toggleCompleted,
                    onDeleteTodo = toDoListViewModel::deleteItem,
                    selectedTodoCategory = selectedTodoCategory,
                    onTodoCategoryChanged = toDoListViewModel::changeCategory,


                    //------------- break section
                    isBreakRunning = uiState.isBreakRunning,
                    currentBreakSeconds = uiState.currentBreakSeconds,
                    todaysBreakSeconds = uiState.todaysBreakSeconds,
                    todaysBreakCount = uiState.todaysBreakCount,
                    onEndBreak = viewModel::endBreak,
                    onStartBreak = viewModel::startBreak
                )
            }
        }
        // ---------------- DailyRecordHistoryScreen --------------------
        composable(
            route = Screen.DailyRecordHistory.route
        ) {

            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }

            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val viewModel = remember {
                DailyRecordHistoryViewModel(repository)
            }
            val historyRecords by
            viewModel.historyRecords.collectAsState()

            OmegaDarkTheme {
                DailyRecordHistoryScreen(
                    historyRecords = historyRecords,
                    onRecordClick = { record ->
                        navController.navigate(
                            "daily_record_details/${record.recordId}/${record.recordDate}"
                        )
                    }
                )
            }
        }

        composable(
            route = "daily_record_details/{recordId}/{recordDate}",
            arguments = listOf(
                navArgument("recordId") {
                    type = NavType.LongType
                },
                navArgument("recordDate") {
                    type = NavType.StringType
                }
            )
        ) {
            val context = LocalContext.current
            val db = remember { DatabaseProvider.getDatabase(context) }

            val repository = remember {
                Omega_Repository(
                    db.ProjectDao(),
                    db.PhaseDao(),
                    db.SessionDao(),
                    dailyRecordDao = db.DailyRecordDao(),
                    activeSessionDao = db.ActiveSessionDao(),
                    todolistDao = db.ToDoListDao(),
                    activeBreakDao = db.ActiveBreakDao(),
                    unplannedProjectDao = db.UnplannedProjectDao()
                )
            }

            val recordId =
                it.arguments?.getLong("recordId")
                    ?: return@composable

            val recordDate =
                it.arguments?.getString("recordDate")
                    ?: ""

            Log.d(
                "OMEGA",
                "DETAIL SCREEN REACHED: $recordId"
            )

            val viewModel = remember {

                DailyRecordDetailsViewModel(
                    repository,
                    recordId
                )
            }

            val sessions by
            viewModel.sessions.collectAsState()

            OmegaDarkTheme {
                DailyRecordDetailsScreen(
                    sessions = sessions,
                    recordDate = recordDate
                )
            }
        }



    }
}