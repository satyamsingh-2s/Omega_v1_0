package com.example.omega_v1_0.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Workspaces
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.omega_v1_0.data_layer.database.DatabaseProvider
import com.example.omega_v1_0.data_layer.omega_repository.UnplannedProjectRepository
import com.example.omega_v1_0.planner.repository.PlannerRepository
import com.example.omega_v1_0.navigation.NavigationAction
import com.example.omega_v1_0.navigation.NavigationCoordinator
import com.example.omega_v1_0.navigation.NavigationItem
import com.example.omega_v1_0.navigation.Screen
import com.example.omega_v1_0.ui.navigation.navigation_bar.BottomNavigationBar
import com.example.omega_v1_0.navigation.workspace.WorkspaceManager
import com.example.omega_v1_0.navigation.workspace.WorkspaceSwitcher
import com.example.omega_v1_0.ui.screens.PlannerBottomSheet.PlannerBottomSheet
import com.example.omega_v1_0.ui.screens.PlannerBottomSheet.TodayQueueBottomSheet
import com.example.omega_v1_0.ui.viewmodel.PlannerViewModel

@Composable
fun MainContainer(

    navController: NavHostController,
    workspaceManager: WorkspaceManager,
    content: @Composable (PaddingValues) -> Unit

) {

    val navBackStackEntry by
    navController.currentBackStackEntryAsState()

    var showTodayQueue by rememberSaveable {
        mutableStateOf(false)
    }
    var showPlanner by rememberSaveable {
        mutableStateOf(false)
    }

    val navigationItems = listOf(

        NavigationItem(
            label = "Todo",
            icon = Icons.Outlined.CheckCircle,
            action = NavigationAction.OpenTodo
        ),

        NavigationItem(
            label = "History",
            icon = Icons.Outlined.History,
            action = NavigationAction.OpenHistory
        ),

        NavigationItem(
            label = "Workspace",
            icon = Icons.Outlined.Workspaces,
            action = NavigationAction.OpenWorkspace
        )

    )
// ---- quick fix for navigation bar not display on spalsh screen
    val currentRoute =
        navController.currentBackStackEntryFlow.collectAsState(initial = null)
            .value
            ?.destination
            ?.route
    val showBottomBar = currentRoute != Screen.OmegaSplashScreen.route


    var showWorkspaceSwitcher by rememberSaveable {
        mutableStateOf(false)
    }

    val currentWorkspace by workspaceManager.currentWorkspace.collectAsState()


    val context = LocalContext.current
    val db = remember { DatabaseProvider.getDatabase(context) }

    val unplannedProjectRepository = remember {
        UnplannedProjectRepository(
            db.UnplannedProjectDao(),
            db.SessionDao())
    }

    val plannerRepository = remember {
        PlannerRepository(
            plannerDao = db.plannerDao(),
            unplannedProjectRepository = unplannedProjectRepository)
    }

    val plannerViewModel = remember {
        PlannerViewModel(plannerRepository)
    }
    val uiState by plannerViewModel.uiState.collectAsState()
    val expandedBucket by
    plannerViewModel.expandedBucket.collectAsState()
    val taskContextMenuState by
    plannerViewModel.taskContextMenuState.collectAsState()
    val openSessionNode by
    plannerViewModel.openSessionNode.collectAsState()

    var openPlannerAfterDismiss by rememberSaveable {
        mutableStateOf(false)
    }

    // --- listen the event-- pausing the navigation as it not navigation to that node
    LaunchedEffect(openSessionNode) {

        val node = openSessionNode ?: return@LaunchedEffect
        navController.navigate(
            Screen.UnplannedProjectSessionScreen.route
        )
        plannerViewModel.onSessionNavigationComplete()
    }

    LaunchedEffect(showTodayQueue) {

        if (!showTodayQueue && openPlannerAfterDismiss) {

            showPlanner = true
            openPlannerAfterDismiss = false

        }

    }

    Scaffold(

        modifier = Modifier.fillMaxSize(),

        bottomBar = {

            if (showBottomBar) {

                BottomNavigationBar(

                    items = navigationItems,

                    selectedAction =
                        NavigationCoordinator.getSelectedAction(
                            currentRoute
                        ),

                    onAction = { action ->
                        when (action) {
                            NavigationAction.OpenTodo -> {
                                showTodayQueue = true
                            }

                            else -> {
                                NavigationCoordinator.handle(
                                    action = action,
                                    navController = navController,
                                    workspaceManager = workspaceManager
                                )
                            }
                        }
                    },

                    // ---------------- longclick section
                    onTodoLongClick = {
                        showPlanner = true
                    },
                    onWorkspaceLongClick = {
                        showWorkspaceSwitcher = true
                    }

                )

            }
        }

    ) { innerPadding ->

        content(innerPadding)

        WorkspaceSwitcher(
            expanded = showWorkspaceSwitcher,
            currentWorkspace = currentWorkspace,
            onDismiss = { showWorkspaceSwitcher = false
            },
            onWorkspaceSelected = { workspace ->
                workspaceManager.setCurrentWorkspace(workspace)
                showWorkspaceSwitcher = false
                NavigationCoordinator.handle(

                    action = NavigationAction.OpenWorkspace,

                    navController = navController,

                    workspaceManager = workspaceManager

                )

            }

        )
        if (showTodayQueue) {

            TodayQueueBottomSheet(
                todayQueue = uiState.todayQueue,
                onDismiss = {
                    showTodayQueue = false
                },
                onTaskClick = {

                    // Will connect later

                },
                onTaskLongClick = {

                // Will connect later
                },
                onOpenPlanner = {
                    openPlannerAfterDismiss = true
                    showTodayQueue = false
                }

            )
        }
        if (showPlanner) {
            PlannerBottomSheet(
                uiState = uiState,
                expandedBucket = expandedBucket,
                taskContextMenuState = taskContextMenuState,

                onDismiss = {
                    showPlanner = false
                },

                onExpandBucket = plannerViewModel::expandBucket,
                onTaskClick = plannerViewModel::openSession,
                onTaskLongClick = plannerViewModel::showTaskMenu,
                onDismissTaskMenu = plannerViewModel::hideTaskMenu,

                onMovePriority = plannerViewModel::moveTaskToPriority,
                onAddToToday = plannerViewModel::addSelectedTaskToToday,
                onRemoveFromPlanner = plannerViewModel::removeSelectedTaskFromPlanner,


                )

        }


    }
}




