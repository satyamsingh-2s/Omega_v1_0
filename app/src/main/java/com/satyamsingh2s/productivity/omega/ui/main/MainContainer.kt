package com.satyamsingh2s.productivity.omega.ui.main

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
import com.satyamsingh2s.productivity.omega.data_layer.database.DatabaseProvider
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.UnplannedProjectRepository
import com.satyamsingh2s.productivity.omega.planner.repository.PlannerRepository
import com.satyamsingh2s.productivity.omega.navigation.NavigationAction
import com.satyamsingh2s.productivity.omega.navigation.NavigationCoordinator
import com.satyamsingh2s.productivity.omega.navigation.NavigationItem
import com.satyamsingh2s.productivity.omega.navigation.Screen
import com.satyamsingh2s.productivity.omega.navigation.workspace.WorkspaceManager
import com.satyamsingh2s.productivity.omega.ui.navigation.navigation_bar.BottomNavigationBar
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.PlannerBottomSheet
import com.satyamsingh2s.productivity.omega.ui.screens.PlannerBottomSheet.TodayQueueBottomSheet
import com.satyamsingh2s.productivity.omega.ui.screens.WorkspaceBottomSheet.WorkspaceBottomSheet
import com.satyamsingh2s.productivity.omega.ui.viewmodel.PlannerViewModel

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

    // ---- Quick fix for navigation bar not displaying on splash screen

    val currentRoute =
        navController.currentBackStackEntryFlow
            .collectAsState(initial = null)
            .value
            ?.destination
            ?.route

    val showBottomBar =
        currentRoute != Screen.OmegaSplashScreen.route

    var showWorkspaceSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val currentWorkspace by
    workspaceManager.currentWorkspace.collectAsState()

    val context = LocalContext.current

    val db = remember {
        DatabaseProvider.getDatabase(context)
    }

    val unplannedProjectRepository = remember {
        UnplannedProjectRepository(
            db.UnplannedProjectDao(),
            db.SessionDao()
        )
    }

    val plannerRepository = remember {
        PlannerRepository(
            plannerDao = db.plannerDao(),
            unplannedProjectRepository = unplannedProjectRepository
        )
    }

    val plannerViewModel = remember {
        PlannerViewModel(plannerRepository)
    }

    val uiState by
    plannerViewModel.uiState.collectAsState()

    val expandedBucket by
    plannerViewModel.expandedBucket.collectAsState()

    val taskContextMenuState by
    plannerViewModel.taskContextMenuState.collectAsState()

    val openSessionNode by
    plannerViewModel.openSessionNode.collectAsState()

    var openPlannerAfterDismiss by rememberSaveable {
        mutableStateOf(false)
    }

    // --- Listen to the event ---
    // Pausing the navigation as it is not navigating to that node.

    LaunchedEffect(openSessionNode) {

        val node = openSessionNode
            ?: return@LaunchedEffect

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

                    // ---------------- Long-click section

                    onTodoLongClick = {
                        showPlanner = true
                    },

                    onWorkspaceLongClick = {
                        showWorkspaceSheet = true
                    }
                )
            }
        }

    ) { innerPadding ->

        content(innerPadding)

        // ---------------- Workspace Bottom Sheet

        if (showWorkspaceSheet) {

            WorkspaceBottomSheet(

                currentWorkspace = currentWorkspace,

                onDismiss = {
                    showWorkspaceSheet = false
                },

                onWorkspaceSelected = { workspace ->

                    workspaceManager.setCurrentWorkspace(workspace)

                    showWorkspaceSheet = false

                    NavigationCoordinator.handle(

                        action = NavigationAction.OpenWorkspace,

                        navController = navController,

                        workspaceManager = workspaceManager
                    )
                }
            )
        }

        // ---------------- Today Queue Bottom Sheet

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

        // ---------------- Planner Bottom Sheet

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

                onRemoveFromPlanner =
                    plannerViewModel::removeSelectedTaskFromPlanner
            )
        }
    }
}