package com.satyamsingh2s.productivity.omega.navigation

/**
 * Events emitted by the Omega Navigation Bar.
 *
 * The navigation bar only emits these events.
 * It does not know how navigation is performed.
 */
sealed interface NavigationAction {

    /**
     * Open the Todo feature.
     */
    data object OpenTodo : NavigationAction

    /**
     * Open the Session Notes & History feature.
     */
    data object OpenHistory : NavigationAction

    /**
     * Open the Workspace picker.
     *
     * Short press:
     *   Navigate to the current workspace.
     *
     * Long press:
     *   Show the workspace selection sheet.
     */
    data object OpenWorkspace : NavigationAction
}