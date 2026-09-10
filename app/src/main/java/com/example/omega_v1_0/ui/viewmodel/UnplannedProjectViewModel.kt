package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models_enums.PlannerPriority
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel
import com.example.omega_v1_0.ui.uistate.UnplannedProjectUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UnplannedProjectViewModel(
    private val repository: Omega_Repository,
   // private val plannerRepository: PlannerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UnplannedProjectUiState())  // used to change
    val uiState = _uiState.asStateFlow() // only access to read, so it is used by all to read the data

    // using shared flow for one time events....
    private val _navigateToSession = MutableSharedFlow<Unit>()

    val navigateToSession: SharedFlow<Unit> =
        _navigateToSession.asSharedFlow()

    // this is for focused camera feature to keep track of the tree for the ui
    private val navigationStack = mutableListOf<Long>()

    //private val plannerRepository = PlannerRepository()

    init {
        Log.d(
            "UNPLANNED",
            "viemodel recreated ")
        observeTree()
        //  observeSession()
    }

    private fun observeTree() {

        viewModelScope.launch {
            repository
                .getUnplannedTree()
                .collect { tree ->

                    _uiState.update {
                        it.copy(tree = tree)
                    }

                    // -----------------------
                    // NEW FEATURE : SINGLE BRANCH EXPANSION
                    // Refresh expandedPath against the latest emitted tree.
                    // -----------------------
                    val expandedLeafNodeId =
                        uiState.value.expandedPath.lastOrNull()
                    if (expandedLeafNodeId != null) {
                        val refreshedPath =
                            findPathToNode(
                                expandedLeafNodeId,
                                tree
                            )
                        _uiState.update {
                            it.copy(
                                expandedPath = refreshedPath
                            )
                        }
                    }

                    // refresh current focus
                    val currentFocusedId =
                        uiState.value.focusedNode?.nodeId

                    if (currentFocusedId == null) {

                        // No focused node yet
                        // (we'll handle initial selection in a moment)

                    } else {

                        val refreshedNode =
                            findNodeById(
                                currentFocusedId,
                                tree
                            )

                        setFocusedNode(refreshedNode)
                    }
                }
        }
    }

    fun createRootNode(
        title: String
    ) {

        viewModelScope.launch {
            repository.createRootNode(title)
        }
    }

    fun createChildNode(
        parentId: Long,
        title: String

    ) {

        viewModelScope.launch {

            repository.createChildNode(
                parentId, title)
        }
    }

    fun renameNode(
        nodeId: Long,
        title: String

    ) {
        viewModelScope.launch {
            repository.renameNode(
                nodeId,
                title
            )
        }
    }

    fun confirmRename() {
        val nodeId =
            uiState.value.selectedRenameNodeId
                ?: return
        val title =
            uiState.value.renameInput
        if(title.isBlank()) return
        renameNode(
            nodeId,
            title
        )
        hideRenameDialog()
    }

    fun updateExpectedDuration(
        nodeId: Long,
        expectedDurationSeconds: Int?
    ) {

        viewModelScope.launch {
            repository.updateExpectedDuration(
                nodeId,
                expectedDurationSeconds
            )
        }
    }

    fun markCompleted(nodeId: Long
    ) {
        viewModelScope.launch {
            repository.markNodeCompleted(nodeId)
        }
    }

    fun markIncomplete(nodeId: Long
    ) {
        viewModelScope.launch {
            repository.markNodeIncomplete(nodeId)
        }
    }

    fun toggleCompleted(nodeId: Long, isCompleted: Boolean
    ) {
        if (isCompleted) {
            markCompleted(nodeId)
        } else {
            markIncomplete(nodeId)
        }
    }

    fun onNavigateToSession(
        nodeId: Long
    ) {

        viewModelScope.launch {
            repository.setSelectedNode(nodeId)
            if (repository.hasActiveSession()) {
                _uiState.update {
                    it.copy(
                        showSessionAlreadyRunningDialog = true
                    )
                }

            } else {

                _navigateToSession.emit(Unit)
            }
        }
    }


fun confirmAddToPlanner() {

    val nodeId =
        _uiState.value.selectedPlannerNodeId
            ?: return

    val priority =
        _uiState.value.selectedPlannerPriority

    viewModelScope.launch {

        repository.addNodeToPlanner(
            nodeId = nodeId,
            priority = priority
        )

        _uiState.update {
            it.copy(
                showAddToPlannerDialog = false,
                selectedPlannerNodeId = null,
                selectedPlannerPriority = PlannerPriority.MEDIUM
            )
        }
    }
}

    // ---------------- dialogue part -----------------------------------------------------
    // ------------- root dilagoue
    fun showAddRootDialog() {

        _uiState.update {
            it.copy(
                showAddRootDialog = true,
                dialogInput = ""
            )
        }
    }

    fun hideAddRootDialog() {
        _uiState.update {
            it.copy(
                showAddRootDialog = false
            )
        }
    }

    // ------------- child dialogue ----------------
    fun showAddChildDialog(
        nodeId: Long
    ) {
        _uiState.update {

            it.copy(
                selectedNodeId = nodeId,
                showAddChildDialog = true,
                dialogInput = ""
            )
        }
    }

    fun hideAddChildDialog() {
        _uiState.update {
            it.copy(
                showAddChildDialog = false
            )
        }
    }

    // ------------Input handeler --------------
    fun onDialogInputChanged(
        value: String
    ) {
        _uiState.update {
            it.copy(
                dialogInput = value
            )
        }
    }

    // ----------- expected duration dialogue ----------------
    fun showExpectedDurationDialog(
        nodeId: Long
    ) {
        _uiState.update {
            it.copy(
                selectedExpectedDurationNodeId = nodeId,
                showExpectedDurationDialog = true,
                expectedDurationInput = ""
            )
        }
    }

    fun hideExpectedDurationDialog() {
        _uiState.update {
            it.copy(
                showExpectedDurationDialog = false)
        }
    }

    fun onExpectedDurationChanged(
        value: String
    ) {
        _uiState.update {
            it.copy(
                expectedDurationInput = value)
        }
    }

    fun confirmExpectedDuration() {
        val nodeId =
            uiState.value
                .selectedExpectedDurationNodeId
                ?: return
        val seconds =
            uiState.value
                .expectedDurationInput
                .toIntOrNull()
                ?.times(60)
                ?: return

        updateExpectedDuration(nodeId, seconds)
        hideExpectedDurationDialog()
    }

    // ---------- rename dialgoue --------
    fun showRenameDialog(
        nodeId: Long,
        currentTitle: String
    ) {
        _uiState.update {
            it.copy(
                selectedRenameNodeId = nodeId,
                showRenameDialog = true,
                renameInput = currentTitle
            )
        }
    }

    fun hideRenameDialog() {
        _uiState.update {
            it.copy(
                showRenameDialog = false)
        }
    }

    fun onRenameChanged(
        value: String
    ) {
        _uiState.update {
            it.copy(
                renameInput = value)
        }
    }

    //---------------- delet dialogue part
    fun showDeleteDialog(
        nodeId: Long
    ) {
        _uiState.update {
            it.copy(
                selectedDeleteNodeId = nodeId,
                showDeleteDialog = true
            )
        }
    }

    fun hideDeleteDialog() {
        _uiState.update {
            it.copy(
                showDeleteDialog = false
            )
        }
    }

    // ---------- stats experimental pahse ------
    fun showStatsDialog(
        node: UnplannedProjectUiModel
    ) {
        _uiState.update {
            it.copy(
                showStatsDialog = true,
                selectedStatsNode = node
            )
        }
    }

    fun hideStatsDialog() {

        _uiState.update {
            it.copy(
                showStatsDialog = false,
                selectedStatsNode = null
            )
        }
    }

    // ---------- session already running dialogue -----------------
    fun hideSessionAlreadyRunningDialog() {
        _uiState.update {
            it.copy(
                showSessionAlreadyRunningDialog = false
            )
        }
    }

    fun confirmEndRunningSession() {
        viewModelScope.launch {
            repository.stopUnplannedSession()
            _uiState.update {
                it.copy(
                    showSessionAlreadyRunningDialog = false
                )
            }
            _navigateToSession.emit(Unit)
        }
    }

    fun openCurrentSession() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showSessionAlreadyRunningDialog = false
                )
            }
            _navigateToSession.emit(Unit)
        }
    }

    // -------------- add to Planner dialog part ---------------------
    fun showAddToPlannerDialog(
        nodeId: Long
    ) {
        _uiState.update {
            it.copy(
                showAddToPlannerDialog = true,
                selectedPlannerNodeId = nodeId,
                selectedPlannerPriority = PlannerPriority.MEDIUM
            )
        }
    }

    fun hideAddToPlannerDialog() {
        _uiState.update {
            it.copy(
                showAddToPlannerDialog = false,
                selectedPlannerNodeId = null,
                selectedPlannerPriority = PlannerPriority.MEDIUM
            )
        }
    }

    fun selectPlannerPriority(
        priority: PlannerPriority
    ) {
        _uiState.update {
            it.copy(
                selectedPlannerPriority = priority
            )
        }
    }

    // ------------- confirm function -----------------
    fun confirmAddRoot() {
        val title = uiState.value.dialogInput
        if (title.isBlank()
        ) return

        createRootNode(title)

        hideAddRootDialog()
    }

    fun confirmAddChild() {
        val parentId = uiState.value.selectedNodeId ?: return
        val title = uiState.value.dialogInput
        if (
            title.isBlank()

        ) return
        createChildNode(parentId, title)
        hideAddChildDialog()
    }

    fun confirmDelete() {
        val nodeId =
            uiState.value
                .selectedDeleteNodeId
                ?: return
        deleteNode(nodeId)
        hideDeleteDialog()
    }


    fun onNodeClick(
        nodeId: Long
    ) {

        //⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️
        // add this experimental feature TODO -- underdevelopment
        //--- sovled the problem but add button is not appearing --
        val clickedNode =
            findNodeById(
                nodeId,
                uiState.value.tree
            )

        // ---- Frozen Leaf Rule ----
        // A node with zero children is a LEAF by definition. Do NOT update
        // expandedPath. Return immediately so the navigation state stays put.
        if (clickedNode != null && clickedNode.children.isEmpty()) {
            return
        }
        //⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️⚠️
        // -----------------------
        // NEW FEATURE : SINGLE BRANCH EXPANSION
        // User clicks node -> findPathToNode() -> expandedPath = returnedPath -> UI recomposes.
        // -----------------------
        val path =
            findPathToNode(
                nodeId
            )
        if (path.isEmpty()) return
        _uiState.update {
            it.copy(
                expandedPath = path
            )
        }
    }

    fun deleteNode(
        nodeId: Long
    ) {
        viewModelScope.launch {
            repository.deleteNode(
                nodeId
            )
        }
    }

//    private fun observeSession() {
//        viewModelScope.launch {
//            repository
//                .observeActiveSession()
//                .collect { activeSession ->
//
//                    if (activeSession == null) {
//                        _uiState.update {
//                            it.copy(
//                                sessionStatus = null,
//                                runningNodeId = null
//                            )
//                        }
//
//                    } else {
//                        _uiState.update {
//                            it.copy(
//                                sessionStatus = activeSession.status,
//                                runningNodeId = repository.getRunningNodeId()
//                            )
//                        }
//                    }
//                }
//        }
//    }

    fun toggleExpandNode(
        nodeId: Long
    ) {
        // -----------------------
        // NEW FEATURE : SINGLE BRANCH EXPANSION
        // Toggle behavior is now based on expandedPath (single-branch).
        // If the node is already expanded:
        // - If it is the leaf of expandedPath, collapse the node itself (to its parent).
        // - If it is an ancestor in expandedPath, collapse only its descendants.
        // Otherwise, expand the single path to it.
        // -----------------------
        val currentPath =
            uiState.value.expandedPath

        val index =
            currentPath.indexOf(nodeId)

        if (index == -1) {
            onNodeClick(nodeId)
            return
        }

        if (index == currentPath.lastIndex) {
            val newPath =
                if (index == 0) {
                    emptyList()
                } else {
                    currentPath.take(index)
                }
            _uiState.update {
                it.copy(
                    expandedPath = newPath
                )
            }
            return
        }

        collapseToNode(nodeId)
    }

    fun collapseToNode(
        nodeId: Long
    ) {
        // -----------------------
        // NEW FEATURE : SINGLE BRANCH EXPANSION
        // Collapses the currently expanded branch to the given node (inclusive).
        // Example:
        // Current: [Project2, Engine, Database, Layout]
        // Input: Engine
        // Output: [Project2, Engine]
        // -----------------------
        val currentPath =
            uiState.value.expandedPath

        val index =
            currentPath.indexOf(nodeId)

        if (index == -1) return

        val newPath =
            currentPath.take(index + 1)

        _uiState.update {
            it.copy(
                expandedPath = newPath
            )
        }
    }

    fun findPathToNode(
        nodeId: Long
    ): List<Long> {
        // -----------------------
        // NEW FEATURE : SINGLE BRANCH EXPANSION
        // Finds the hierarchical path from a root node to the given nodeId.
        // Returns a list of nodeIds like: [Root, Child, SubChild, Leaf]
        // -----------------------
        return findPathToNode(
            nodeId,
            uiState.value.tree
        )
    }

    private fun findPathToNode(
        nodeId: Long,
        nodes: List<UnplannedProjectUiModel>
    ): List<Long> {
        // -----------------------
        // NEW FEATURE : SINGLE BRANCH EXPANSION
        // Recursive helper for findPathToNode(nodeId).
        // -----------------------
        for (node in nodes) {
            if (node.nodeId == nodeId) {
                return listOf(node.nodeId)
            }
            val childPath =
                findPathToNode(
                    nodeId,
                    node.children
                )
            if (childPath.isNotEmpty()) {
                return listOf(node.nodeId) + childPath
            }
        }
        return emptyList()
    }

    // --- new feature improvement in ui
    private fun focusNode(
        nodeId: Long
    ) {
        if (uiState.value.focusedNode?.nodeId == nodeId) {
            return
        }

        val node = findNodeById(
            nodeId,
            uiState.value.tree
        ) ?: return

        uiState.value.focusedNode?.let { currentNode ->
            navigationStack.add(currentNode.nodeId)
        }

        setFocusedNode(node)
    }

    // -- it has time complexity O(n) TODO -- improve time complexity
    private fun findNodeById(
        nodeId: Long,
        nodes: List<UnplannedProjectUiModel>
    ): UnplannedProjectUiModel? {

        for (node in nodes) {
            if (node.nodeId == nodeId) {
                return node
            }
            val found = findNodeById(
                nodeId,
                node.children
            )
            if (found != null) {
                return found
            }
        }

        return null
    }

    private fun setFocusedNode(
        node: UnplannedProjectUiModel?
    ) {
        _uiState.update {
            it.copy(
                focusedNode = node,
                visibleChildren = node?.children ?: emptyList(),
                canNavigateBack = navigationStack.isNotEmpty()
            )
        }
    }

    fun navigateBack() {

        val previousNodeId =
            navigationStack.removeLastOrNull()
                ?: return

        val previousNode =
            findNodeById(
                previousNodeId,
                uiState.value.tree
            )

        setFocusedNode(previousNode)
    }


}
