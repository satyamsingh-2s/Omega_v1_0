package com.example.omega_v1_0.navigation

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.omega_v1_0.ai.branch_b.repository.AiRepository
import com.example.omega_v1_0.core.storage.LocalFileStorageManager
import com.example.omega_v1_0.data_layer.database.DatabaseProvider
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.data_layer.omega_repository.SessionNoteAttachmentRepository
import com.example.omega_v1_0.data_layer.omega_repository.SessionNoteRepository
import com.example.omega_v1_0.data_layer.omega_repository.SessionStatusBarRepository
import com.example.omega_v1_0.data_layer.pemissions.OmegaPermissionManager
import com.example.omega_v1_0.models_enums.Experience
import com.example.omega_v1_0.models_enums.SessionType
import com.example.omega_v1_0.notification.OmegaNotificationManager
import com.example.omega_v1_0.ui.screens.CreateProjectScreen
import com.example.omega_v1_0.ui.screens.DailyRecordDetailsScreen
import com.example.omega_v1_0.ui.screens.DailyRecordHistoryScreen
import com.example.omega_v1_0.ui.screens.DailyRecordScreen
import com.example.omega_v1_0.ui.screens.DeskOmegaScreen
import com.example.omega_v1_0.ui.screens.EstimateScreen
import com.example.omega_v1_0.ui.screens.MainScreen
import com.example.omega_v1_0.ui.screens.PhaseTimerScreen
import com.example.omega_v1_0.ui.screens.ProjectDashboardScreen
import com.example.omega_v1_0.ui.screens.UnplannedProjectScreen
import com.example.omega_v1_0.ui.screens.UnplannedProjectEntryScreen
import com.example.omega_v1_0.ui.viewmodel.CreateProjectViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordDetailsViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordHistoryViewModel
import com.example.omega_v1_0.ui.viewmodel.DailyRecordViewModel
import com.example.omega_v1_0.ui.viewmodel.DashboardViewModel
import com.example.omega_v1_0.ui.viewmodel.EstimateScreenViewModel
import com.example.omega_v1_0.ui.viewmodel.PhaseTimerViewModel
import com.example.omega_v1_0.ui.viewmodel.ToDoListViewModel
import com.example.omega_v1_0.ui.viewmodel.UnplannedProjectEntryScreenViewModel
import com.example.omega_v1_0.ui.viewmodel.UnplannedProjectViewModel
import com.example.omega_v1_0.ui.deskOmega.DeskOmegaSkin
import com.example.omega_v1_0.ui.screens.OmegaSplashScreen
import com.example.omega_v1_0.ui.screens.UnplannedProjectSessionScreen
import com.example.omega_v1_0.ui.theme.OmegaRedTheme
import com.example.omega_v1_0.ui.viewmodel.UnplannedProjectSessionViewModel
import com.example.omega_v1_0.settings.repository.SettingsRepository
import com.example.omega_v1_0.ui.components.SessionStatusBar
import com.example.omega_v1_0.ui.components.SessionStatusBarViewModel
import com.example.omega_v1_0.ui.main.MainContainer
import com.example.omega_v1_0.ui.model.DeskOmegaUiModel
import com.example.omega_v1_0.navigation.workspace.WorkspaceManager
import com.example.omega_v1_0.ui.viewmodel.DeskOmegaViewModel
import com.example.omega_v1_0.ui.viewmodel.RevisionNoteViewModel

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

//this is the structue we should follow 🟢🟢🟢🟢🟢🟢
//fun OmegaNavGraph(...) {
//    // 1. Context
//    // 2. Database
//    // 3. Repository
//    // 4. Permission Manager
//    // 5. Notification Manager
//    // 6. Permission Launcher
//    // 7. Request Permission
//    // 8. ViewModels
//    //9. NavHost

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OmegaNavGraph(
    navController: NavHostController
) {
    OmegaRedTheme {


        val context = LocalContext.current

        val localFileStorageManager = remember {
            LocalFileStorageManager(
                context.applicationContext
            )
        }

        val db = remember { DatabaseProvider.getDatabase(context) }

        val settingsRepository = remember {
            SettingsRepository(context.applicationContext)
        }

        val aiRepository = remember {
            AiRepository()
        }

        //--- temorarlily creating the variable for bottom navigation bar
        val workspaceManager = remember {
            WorkspaceManager()
        }

        val sessionStatusBarRepository = remember {
            SessionStatusBarRepository(
                activeSessionDao = db.ActiveSessionDao(),
                sessionDao = db.SessionDao(),
                projectDao = db.ProjectDao(),
                phaseDao = db.PhaseDao(),
                unplannedProjectDao = db.UnplannedProjectDao(),
                dailyRecordDao = db.DailyRecordDao()
            )
        }
        val sessionNoteRepository = remember {
            SessionNoteRepository(
                db.sessionNoteDao()
            )
        }

        val sessionNoteAttachmentRepository = remember {
            SessionNoteAttachmentRepository(
                db.sessionNoteAttachmentDao(),
                localFileStorageManager = localFileStorageManager
            )
        }

        // --- all things are done form omega repository ---
        // but recently i have using sesison note attachment , unplannedproject repository for notes and plannerfeature
        // --- keep in mind
        val repository = remember {
            Omega_Repository(
                db.ProjectDao(),
                db.PhaseDao(),
                db.SessionDao(),
                dailyRecordDao = db.DailyRecordDao(),
                activeSessionDao = db.ActiveSessionDao(),
                todolistDao = db.ToDoListDao(),
                activeBreakDao = db.ActiveBreakDao(),
                unplannedProjectDao = db.UnplannedProjectDao(),
                pomodoroDao = db.pomodoroDao(),
                settingsRepository = settingsRepository,
                sessionStatusBarRepository = sessionStatusBarRepository,
                plannerDao = db.plannerDao()

                )
        }

        // here we given the application context to the file omegaNotificationmangager
        val omegaNotificationManager = remember {
            OmegaNotificationManager(context.applicationContext)
        }
        val OmegaPermissionManager = remember {
            OmegaPermissionManager(context.applicationContext)
        }

        // creating a notificationpermissionLAUNCHER -- ⚠️⚠️⚠️⚠️⚠️
        // doubt where it will called
        val notificationPermissionLauncher =
            rememberLauncherForActivityResult(
                contract =
                    ActivityResultContracts
                        .RequestPermission()

            ) { isGranted ->
                // --- nknkf
            }

        // ----------- permission launcher -----------
        LaunchedEffect(Unit) {
            omegaNotificationManager.createChannel()
            //omegaNotificationManager.playIntroSound()
            if (
                !OmegaPermissionManager.hasNotificationPermission()
            ) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                omegaNotificationManager.showNotification(

                    title = "😈",
                    message = "let's get started"

                )
            }
        }

        // this viewmodel is created for the 2 screen,1. for dailyrecordscreen 2. deskomegascreen
        val dailyrecordviewModel = remember {
            DailyRecordViewModel(repository, omegaNotificationManager)
        }

        val sessionStatusBarViewModel = remember {
            SessionStatusBarViewModel(repository)
        }
        val sessionStatusBar by
        sessionStatusBarViewModel.sessionStatusBar.collectAsState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()

        val currentRoute =
            navBackStackEntry?.destination?.route


        MainContainer(
            navController = navController,
            workspaceManager = workspaceManager
        ) {innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {

                NavHost(
                    navController = navController,
                    startDestination = Screen.OmegaSplashScreen.route
                ) {

// ------------------------ SplashScreen part ----------------
                    composable(Screen.OmegaSplashScreen.route) {

                        val context = LocalContext.current
                        OmegaSplashScreen(repository, navController)
                    }

                    composable(Screen.MainScreen.route) {
                        MainScreen(
                            onPlannedWorkClick = {
                                navController.navigate(Screen.CreateProject.route)
                            },
                            onUnplannedWorkClick = {
                                navController.navigate(Screen.UnplannedProjectEntryScreen.route)
                                // Placeholder for now
                            },
                            onDailyRecordClick = {
                                navController.navigate(Screen.DailyRecord.route)
                            }
                        )
                    }

                    // --- navigation to planner bottom scren
//                    composable(
//                        route = Screen.Planner.route
//                    ) {
//
//
//                        val unplannedProjectRepository = remember {
//                            UnplannedProjectRepository(
//                                db.UnplannedProjectDao(),
//                                db.SessionDao())
//                        }
//
//                        val plannerRepository = remember {
//                            PlannerRepository(
//                                plannerDao = db.plannerDao(),
//                                unplannedProjectRepository = unplannedProjectRepository)
//                        }
//
//                       val plannerViewModel = remember {
//                           PlannerViewModel(plannerRepository)
//                       }
//                        val uiState by plannerViewModel.uiState.collectAsState()
//                        val expandedBucket by
//                        plannerViewModel.expandedBucket.collectAsState()
//                        val taskContextMenuState by
//                        plannerViewModel.taskContextMenuState.collectAsState()
//                        val openSessionNode by
//                        plannerViewModel.openSessionNode.collectAsState()
//
//                        // --- listen the event-- pausing the navigation as it not navigation to that node
//                        LaunchedEffect(openSessionNode) {
//
//                            val node = openSessionNode ?: return@LaunchedEffect
//                            navController.navigate(
//                                Screen.UnplannedProjectSessionScreen.route
//                            )
//                            plannerViewModel.onSessionNavigationComplete()
//                        }
//
//                        PlannerBottomSheet(
//                            uiState = plannerViewModel.uiState.collectAsState().value,
//                            expandedBucket = plannerViewModel.expandedBucket.collectAsState().value,
//                            taskContextMenuState = plannerViewModel.taskContextMenuState.collectAsState().value,
//
//                            onDismiss = {
//                                navController.popBackStack()
//                            },
//
//                            onExpandBucket = plannerViewModel::expandBucket,
//                            onTaskClick = plannerViewModel::openSession,
//                            onTaskLongClick = plannerViewModel::showTaskMenu,
//                            onDismissTaskMenu = plannerViewModel::hideTaskMenu,
//
//                            onMovePriority = plannerViewModel::moveTaskToPriority,
//                            onAddToToday = plannerViewModel::addSelectedTaskToToday,
//                            onRemoveFromPlanner = plannerViewModel::removeSelectedTaskFromPlanner,
//
//
//                        )
//                    }

                    composable(Screen.CreateProject.route)    // jab yeah wala composable ka call karnege, tab ek hi value dena hoaga -> route
                    {
                        // ------------------- performing temporary wiring, as not using DI-------------------
                        /**
                         * here we created database name omega_db
                         * created the repository or connected it to
                         * connected OmeganNavGraph to the CreateProjectViewModel and
                         * viewModel to repositroy
                         * */
//            val context = LocalContext.current
//            val db = remember { DatabaseProvider.getDatabase(context) }
////            val db = remember {
////                Room.databaseBuilder(
////                    context,
////                    OmegaDatabase:: class.java,
////                    "omega_db"
////                ).build()
////            }
//
//
//            val repository = remember {
//                Omega_Repository(
//                    db.ProjectDao(),
//                    db.PhaseDao(),
//                    db.SessionDao(),
//                    dailyRecordDao= db.DailyRecordDao(),
//                    activeSessionDao = db.ActiveSessionDao(),
//                    todolistDao = db.ToDoListDao(),
//                    activeBreakDao = db.ActiveBreakDao(),
//                    unplannedProjectDao= db.UnplannedProjectDao()
//                )
//            }

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


                        CreateProjectScreen(       // here we create the onCreateClicked function, and call CreateProjectScreen with the parameter of OnCreateClicked.
                            recentProjects = recentProjects,
                            activeSession = activeSession,
                            allProjects = allProjects,
                            onCreateClicked = { name, experience ->
                                viewModel.createProject(name, experience)
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
                        projectId?.let { id ->
                            // purpose of line is to get the value of experince from creatprojectscreenviewmodel.
                            val experience = viewModel.getLatestExperience() ?: return@let

                            LaunchedEffect(id) {
                                // navigate to estimate screen with projectId -----
                                navController.navigate(
                                    Screen.Estimate.createRoute(id, experience)
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

                        val projectId =
                            backStackEntry.arguments?.getLong("projectId") ?: return@composable

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
//
//                val context =
//                    LocalContext.current // see in onenote for detail also it creates database
//                val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

//                val repository =
//                    remember {   // again line for creates repository. but by remeber it won't create again and agian safe from recompostion or change of composables
//                        Omega_Repository(
//                            db.ProjectDao(),
//                            db.PhaseDao(),
//                            db.SessionDao(),
//                            dailyRecordDao = db.DailyRecordDao(),
//                            activeSessionDao = db.ActiveSessionDao(),
//                            todolistDao = db.ToDoListDao(),
//                            activeBreakDao = db.ActiveBreakDao(),
//                            unplannedProjectDao = db.UnplannedProjectDao(),
//                            pomodoroDao = db.pomodoroDao()
//                        )
//                    }

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

                        if (navigate) {
                            LaunchedEffect(Unit) {
                                navController.navigate(
                                    Screen.Dashboard.createRoute(projectId)
                                ) {
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

//                val context = LocalContext.current
//                val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

//                val repository = remember {
//                    Omega_Repository(
//                        db.ProjectDao(),
//                        db.PhaseDao(),
//                        db.SessionDao(),
//                        dailyRecordDao = db.DailyRecordDao(),
//                        activeSessionDao = db.ActiveSessionDao(),
//                        todolistDao = db.ToDoListDao(),
//                        activeBreakDao = db.ActiveBreakDao(),
//                        unplannedProjectDao = db.UnplannedProjectDao(),
//                        pomodoroDao = db.pomodoroDao()
//                    )
//                }

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
                    ) { backStackEntry ->

                        val phaseId =
                            backStackEntry.arguments?.getLong("phaseId") ?: return@composable

//                val context = LocalContext.current
//                val db = remember { DatabaseProvider.getDatabase(context) }
//                Room.databaseBuilder(
//                    context,
//                    OmegaDatabase::class.java,
//                    "omega_db"
//                ).build()
//            }

//                val repository = remember {
//                    Omega_Repository(
//                        db.ProjectDao(),
//                        db.PhaseDao(),
//                        db.SessionDao(),
//                        dailyRecordDao = db.DailyRecordDao(),
//                        activeSessionDao = db.ActiveSessionDao(),
//                        todolistDao = db.ToDoListDao(),
//                        activeBreakDao = db.ActiveBreakDao(),
//                        unplannedProjectDao = db.UnplannedProjectDao(),
//                        pomodoroDao = db.pomodoroDao(),
//                        settingsRepository = settingsRepository
//                    )
//                }

                        val viewModel =
                            remember {                      // here the view model is created and it is told to use the repository,
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
                        // --- the creating repository part is done on top, so we don't have to create multiple time

                        val uiState by dailyrecordviewModel.uiState.collectAsState()

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
                            dailyrecordviewModel.syncActiveSession()
                            dailyrecordviewModel.loadTodaysTotal()
                            dailyrecordviewModel.loadRecentSessions()

                        }

                        DailyRecordScreen(
                            todaystotalSeconds = uiState.todaysTotalSeconds,
                            sessionName = uiState.sessionNameInput,
                            activeSessionName = uiState.activeSessionName,
                            onEstimateSelected =
                                dailyrecordviewModel::onEstimateSelected,
                            selectedEstimateMinutes =
                                uiState.selectedEstimateMinutes,
                            onSessionNameChange =
                                dailyrecordviewModel::onSessionNameChanged,
//               // onExpectedDurationChange =
//                    viewModel::onExpectedDurationChanged,
                            onStartSession =
                                dailyrecordviewModel::startSession,
                            sessionStatus = uiState.sessionStatus,
                            onPauseSession = dailyrecordviewModel::pauseSession,
                            onResumeSession = dailyrecordviewModel::resumeSession,
                            onStopSession = dailyrecordviewModel::stopSession,
                            stopwatchSeconds =
                                uiState.stopwatchSeconds,
                            recentSessions = uiState.recentSessions,
                            onHistoryClick = { navController.navigate(Screen.DailyRecordHistory.route) },
                            navigateToDeskOmega = {
                                navController.navigate(Screen.DeskOmega.route)
                            },

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
                            onEndBreak = dailyrecordviewModel::endBreak,
                            onStartBreak = dailyrecordviewModel::startBreak,

                            onBreakDurationSelected = dailyrecordviewModel::onBreakDurationSelected,
                            selectedBreakMinutes = uiState.selectedBreakMinutes
                        )
                    }

                    //--------------------  logic ------------------------------- to perform navigation smoothly
                    composable(
                        Screen.DeskOmegaRouter.route
                    ) {

                        DeskOmegaRouter(
                            sessionStatusBar = sessionStatusBar,
                            navController = navController
                        )
                    }
//------------------ DeskOmegaScreen --------------------------------------
                    composable(Screen.DeskOmega.route) {

                        // Dispatcher ViewModel
                        val deskOmegaViewModel = remember {
                            DeskOmegaViewModel(repository)
                        }

                        val sessionStatusBar by
                        deskOmegaViewModel.sessionStatusBar.collectAsState()

                        // Existing feature ViewModels
//                    val dailyRecordViewModel = remember {
//                        DailyRecordViewModel(
//                            repository,
//                            omegaNotificationManager
//                        )
//                    }

                        val unplannedSessionViewModel = remember {
                            UnplannedProjectSessionViewModel(repository)
                        }

                        when (sessionStatusBar?.sessionType) {

                            SessionType.DAILY_RECORD -> {

                                val dailyRecordUiState by
                                dailyrecordviewModel.uiState.collectAsState()

                                val deskUiModel = DeskOmegaUiModel(
                                    title = "",
                                    subtitle = dailyRecordUiState.activeSessionName ?: "Session",
                                    stopwatchSeconds = dailyRecordUiState.stopwatchSeconds,
                                    expectedDurationSeconds =
                                        dailyRecordUiState.selectedEstimateMinutes?.times(60),
                                    sessionStatus = dailyRecordUiState.sessionStatus
                                )

                                DeskOmegaScreen(
                                    uiModel = deskUiModel,
                                    onPauseSession = dailyrecordviewModel::pauseSession,
                                    onResumeSession = dailyrecordviewModel::resumeSession,
                                    skin = DeskOmegaSkin.AOD,
                                    onBack = {
                                        navController.navigate(Screen.DailyRecord.route) {
                                            popUpTo(Screen.DeskOmega.route) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                )
                            }

                            SessionType.UNPLANNED -> {

                                val unplannedProjectSessionUiState by
                                unplannedSessionViewModel.uiState.collectAsState()

                                val deskUiModel = DeskOmegaUiModel(
                                    title = unplannedProjectSessionUiState.breadcrumb,
                                    subtitle = unplannedProjectSessionUiState.activeSessionName,
                                    stopwatchSeconds = unplannedProjectSessionUiState.stopwatchSeconds,
                                    expectedDurationSeconds =
                                        unplannedProjectSessionUiState.expectedDurationSeconds,
                                    sessionStatus = unplannedProjectSessionUiState.sessionStatus
                                )

                                DeskOmegaScreen(
                                    uiModel = deskUiModel,
                                    onPauseSession = unplannedSessionViewModel::pauseSession,
                                    onResumeSession = unplannedSessionViewModel::resumeSession,
                                    onBack = {
                                        navController.navigate(
                                            Screen.UnplannedProjectSessionScreen.route
                                        )
                                        {
                                            popUpTo(Screen.DeskOmega.route) {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    },
                                    skin = DeskOmegaSkin.AOD
                                )
                            }

                            SessionType.PLANNED -> {
                                // Not supported in v1
                            }

                            null -> {
                                Text("No Active Session")
                            }
                        }
                    }

                    // ---------------- DailyRecordHistoryScreen --------------------
                    composable(
                        route = Screen.DailyRecordHistory.route
                    ) {

                        val viewModel = remember {
                            DailyRecordHistoryViewModel(repository)
                        }
                        val historyRecords by
                        viewModel.historyRecords.collectAsState()

                        DailyRecordHistoryScreen(
                            historyRecords = historyRecords,
                            onRecordClick = { record ->
                                navController.navigate(
                                    "daily_record_details/${record.recordId}/${record.recordDate}"
                                )
                            }
                        )
                    }

// ---------------------------- dailyrecordhistorydetails screeen -----------------------------------
                    composable(
                        Screen.DailyRecordDetails.route,
                        arguments = listOf(
                            navArgument("recordId") {
                                type = NavType.LongType
                            },
                            navArgument("recordDate") {
                                type = NavType.StringType
                            }
                        )
                    ) {

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

                        val sessions by viewModel.sessions.collectAsState()

                        DailyRecordDetailsScreen(
                            sessions = sessions,
                            recordDate = recordDate
                        )
                    }

                    // ---------------- UnplannedProjectScreen part ----------------------------------

                    composable(
                        Screen.UnplannedProjectEntryScreen.route
                    ) {
                        val viewModel = remember {
                            UnplannedProjectEntryScreenViewModel(
                                repository,
                                aiRepository
                            )
                        }


                        UnplannedProjectEntryScreen(
                            viewModel = viewModel,
                            onSkip = {
                                navController.navigate(Screen.UnplannedProject.route)
                            },
                            navigateToWorkspace = {
                                navController.navigate(
                                    Screen.UnplannedProject.route
                                )
                            }
                        )
                    }


                    composable(
                        Screen.UnplannedProject.route
                    ) {
                        val viewModel = remember { UnplannedProjectViewModel(
                            repository) }
                        val uiState by viewModel.uiState.collectAsState()

                        LaunchedEffect(viewModel) {
                            viewModel.navigateToSession.collect {
                                navController.navigate(
                                    Screen.UnplannedProjectSessionScreen.route
                                )
                            }
                        }
                        //   OmegaDarkTheme {
                        UnplannedProjectScreen(
                            uiState = uiState,
                            onAddRoot = {
                                viewModel.showAddRootDialog()
                            },
                            onAddChild = { nodeId ->
                                viewModel.showAddChildDialog(nodeId)
                            },
                            onToggleCompleted = { nodeId,
                                                  isCompleted ->
                                viewModel.toggleCompleted(nodeId, isCompleted)
                            },

                            onDialogInputChanged = viewModel::onDialogInputChanged,
                            onDismissRootDialog = viewModel::hideAddRootDialog,
                            onDismissChildDialog = viewModel::hideAddChildDialog,
                            onConfirmRoot = viewModel::confirmAddRoot,
                            onConfirmChild = viewModel::confirmAddChild,

                            onDismissSessionDialog = viewModel::hideSessionAlreadyRunningDialog,
                            onOpenSession = viewModel::openCurrentSession,
                            onEndSession = viewModel::confirmEndRunningSession,

                            onNodeClick = viewModel::onNodeClick,

                            onAddExpectedDuration = viewModel::showExpectedDurationDialog,
                            onDismissExpectedDuration = viewModel::hideExpectedDurationDialog,
                            onExpectedDurationChanged = viewModel::onExpectedDurationChanged,
                            onConfirmExpectedDuration = viewModel::confirmExpectedDuration,

                            onRename = viewModel::showRenameDialog,
                            onDismissRename = viewModel::hideRenameDialog,
                            onRenameChanged = viewModel::onRenameChanged,
                            onConfirmRename = viewModel::confirmRename,

                            onDelete = viewModel::showDeleteDialog,
                            onDismissDelete = viewModel::hideDeleteDialog,
                            onConfirmDelete = viewModel::confirmDelete,

                            onShowStats = viewModel::showStatsDialog,
                            onDismissStats = viewModel::hideStatsDialog,

                            //  expandedNodeIds = uiState.expandedNodeIds,
                            onToggelExpand = viewModel::toggleExpandNode,

                            onNavigateToSession = { nodeId ->
                                viewModel.onNavigateToSession(nodeId)
                            },
                            onAddToPlanner = { nodeId ->
                                viewModel.showAddToPlannerDialog(nodeId)
                            },

                            onDismissAddToPlanner = {
                                viewModel.hideAddToPlannerDialog()
                            },

                            onSelectPlannerPriority = { priority ->
                                viewModel.selectPlannerPriority(priority)
                            },

                            onConfirmAddToPlanner = {
                                viewModel.confirmAddToPlanner()
                            },



                        )
                    }


                    // ---------------- UnplannedProjectSessionScreen ----------------
                    composable(
                        route = Screen.UnplannedProjectSessionScreen.route
                    ) {

                        val viewModel = remember {
                            UnplannedProjectSessionViewModel(repository)
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

                        val revisionNoteViewModel = remember {
                            RevisionNoteViewModel(
                                sessionNoteRepository,
                                sessionNoteAttachmentRepository,
                                localFileStorageManager = localFileStorageManager
                            )
                        }
                        val revisionNoteUiState by
                        revisionNoteViewModel.uiState.collectAsState()

                        LaunchedEffect(uiState.workingNodeId) {

                            uiState.workingNodeId?.let {
                                revisionNoteViewModel.observeRevisionNotes(it)
                            }
                        }

                        UnplannedProjectSessionScreen(
                            // ---------- Header ----------
                            projectName = uiState.projectName,
                            breadcrumb = uiState.breadcrumb,

                            // ---------- Progress ----------
                            currentDurationSeconds = uiState.currentDurationSeconds,
                            expectedDurationSeconds = uiState.expectedDurationSeconds,
                            totalSessions = uiState.totalSessions,

                            // ---------- Session ----------
                            sessionName = uiState.sessionNameInput,
                            activeSessionName = uiState.activeSessionName,
                            onSessionNameChanged = viewModel::onSessionNameChanged,

                            // ---------- Stopwatch ----------
                            stopwatchSeconds = uiState.stopwatchSeconds,
                            sessionStatus = uiState.sessionStatus,

                            // ---------- Controls ----------
                            onStartSession = viewModel::startSession,
                            onPauseSession = viewModel::pauseSession,
                            onResumeSession = viewModel::resumeSession,
                            onStopSession = viewModel::stopSession,

                            // ---------- Recent Sessions ----------
                            recentSessions = uiState.recentSessions,

                            // ---------- Navigation ----------
                            onBack = {
                                navController.popBackStack()
                            },
                            pomodoroState = uiState.pomodoroState,
                            workCyclesBeforeLongBreak =
                                uiState.workCyclesBeforeLongBreak,
                            onSkipBreak = viewModel::skipBreak,

                            onDurationSelected =
                                viewModel::onEstimateSelected,
                            selectedDurationMinutes =
                                uiState.selectedEstimateMinutes,

                            // ---- To do List section  ----------------
                            todoItems = toDoUiState.items,
                            newTodoText = toDoUiState.newItemText,
                            onTodoTextChanged = toDoListViewModel::onNewItemTextChanged,
                            onAddTodo = toDoListViewModel::addItem,
                            onToggleTodo = toDoListViewModel::toggleCompleted,
                            onDeleteTodo = toDoListViewModel::deleteItem,
                            selectedTodoCategory = selectedTodoCategory,
                            onTodoCategoryChanged = toDoListViewModel::changeCategory,

                            navigateToDeskOmega = {
                                navController.navigate(Screen.DeskOmega.route)
                            },

                            revisionNoteViewModel = revisionNoteViewModel, // for note feature


                            onStatsClick = {
                                // Future Statistics Screen
                            }
                        )
                    }
                }

                if (currentRoute != Screen.OmegaSplashScreen.route && currentRoute != Screen.DailyRecord.route && currentRoute != Screen.UnplannedProjectSessionScreen.route && currentRoute != Screen.DeskOmega.route && currentRoute != Screen.UnplannedProjectEntryScreen.route) {
                    SessionStatusBar(
                        model = sessionStatusBar,
                        onDeskOmegaClick = {

                            when (sessionStatusBar?.sessionType) {

                                SessionType.DAILY_RECORD -> {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("openDeskOmega", true)

                                    navController.navigate(
                                        Screen.DeskOmegaRouter.route
                                    ) {
                                        launchSingleTop = true
                                    }
                                }

                                SessionType.UNPLANNED -> {
                                    navController.currentBackStackEntry
                                        ?.savedStateHandle
                                        ?.set("openDeskOmega", true)

                                    navController.navigate(
                                        Screen.DeskOmegaRouter.route
                                    ) {
                                        launchSingleTop = true
                                    }
                                }

                                SessionType.PLANNED -> {
                                    // v1
                                }

                                null -> Unit
                            }
                        },
                        onClick = {

                            when (sessionStatusBar?.sessionType) {

                                SessionType.PLANNED -> {
                                    // for v1 unabailable
                                }

                                SessionType.UNPLANNED -> {
                                    navController.navigate(
                                        Screen.UnplannedProjectSessionScreen.route
                                    ) {
                                        launchSingleTop = true
                                    }
                                }

                                SessionType.DAILY_RECORD -> {
                                    navController.navigate(
                                        Screen.DailyRecord.route
                                    ) {
                                        launchSingleTop = true
                                    }
                                }

                                null -> Unit
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                        ///.weight(1f)
                    )

                }

                Spacer(modifier = Modifier.height(48.dp))
            }
        }
    }
}