package com.example.omega_v1_0.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.room.Room
import com.example.omega_v1_0.data_layer.database.DatabaseProvider
import com.example.omega_v1_0.data_layer.database.OmegaDatabase
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.Experience
import com.example.omega_v1_0.ui.screens.CreateProjectScreen
import com.example.omega_v1_0.ui.screens.EstimateScreen
import com.example.omega_v1_0.ui.screens.PhaseTimerScreen
import com.example.omega_v1_0.ui.screens.ProjectDashboardScreen
import com.example.omega_v1_0.ui.viewmodel.CreateProjectViewModel
import com.example.omega_v1_0.ui.viewmodel.DashboardViewModel
import com.example.omega_v1_0.ui.viewmodel.EstimateScreenViewModel
import com.example.omega_v1_0.ui.viewmodel.PhaseTimerViewModel

/**
 * NavHost = container
 *
 * composable = one screen
 *
 * navArgument = enforced context
 */

@Composable
fun OmegaNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.CreateProject.route
    ) {

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
                    db.SessionDao()
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
                    db.SessionDao()
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
                    db.SessionDao()
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
                    db.SessionDao()
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
            }

            // --- UI ---                          // after launched effect the phaseTimerscreen is called with values, and function
            PhaseTimerScreen(
                uiState = uiState,
                isRunning = isRunning,
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
    }
}