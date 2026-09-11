package com.satyamsingh2s.productivity.omega.planner.repository


import com.satyamsingh2s.productivity.omega.data_layer.dao.PlannerDao
import com.satyamsingh2s.productivity.omega.data_layer.entites.PlannerNodeEntity
import com.satyamsingh2s.productivity.omega.data_layer.entites.UnplannedProjectEntity
import com.satyamsingh2s.productivity.omega.data_layer.omega_repository.UnplannedProjectRepository
import com.satyamsingh2s.productivity.omega.models_enums.PlannerPriority
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerNodeUiModel
import com.satyamsingh2s.productivity.omega.planner.ui.PlannerUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class PlannerRepository(
    private val plannerDao: PlannerDao,
    private val unplannedProjectRepository: UnplannedProjectRepository
) {

    /* -------------------------------------------------------------------------
     * Read
     * ------------------------------------------------------------------------- */

    fun getAllPlannerNodes(): Flow<List<PlannerNodeEntity>> {
        return plannerDao.getAllPlannerNodes()
    }

    fun getPriorityNodes(
        priority: PlannerPriority
    ): Flow<List<PlannerNodeEntity>> {
        return plannerDao.getPlannerNodesByPriority(priority)
    }

    fun getTodayQueue(): Flow<List<PlannerNodeEntity>> {
        return plannerDao.getTodayQueue()
    }

    /* -------------------------------------------------------------------------
     * Write
     * ------------------------------------------------------------------------- */

    suspend fun addNodeToPlanner(
        nodeId: Long,
        priority: PlannerPriority
    ) {

        // Planner only supports leaf nodes.
        check(
            unplannedProjectRepository.canStartSession(nodeId)
        ) {
            "Only leaf nodes can be added to planner."
        }

        // Ignore duplicate requests.
        if (isInPlanner(nodeId)) {
            return
        }

        // Overflow engine will be implemented next.
        if (isBucketFull(priority)) {
            cascadeDown(priority)
        }

        insertIntoBucket(
            nodeId = nodeId,
            priority = priority
        )
    }

    suspend fun removeNodeFromPlanner(
        nodeId: Long
    ) {

        // Nothing to remove.
        val plannerNode = plannerDao.getPlannerNode(nodeId)
            ?: return

        plannerDao.deletePlannerNode(nodeId)

        normalizeBucket(plannerNode.priority)
    }

    suspend fun changePriority(
        nodeId: Long,
        newPriority: PlannerPriority
    ) {

        val plannerNode = requirePlannerNode(nodeId)

        // Nothing to do.
        if (plannerNode.priority == newPriority) {
            return
        }

        val oldPriority = plannerNode.priority

        // Ensure the destination bucket has space.
        if (isBucketFull(newPriority)) {
            cascadeDown(newPriority)
        }

        // Move the existing planner node.
        moveToBucket(
            plannerNode = plannerNode,
            priority = newPriority
        )

        // Fill any gaps left in the old bucket.
        normalizeBucket(oldPriority)
    }

    // the priority order within the same bucket
    suspend fun reorderPriority(
        priority: PlannerPriority,
        fromIndex: Int,
        toIndex: Int
    ) {

        if (fromIndex == toIndex) {
            return
        }

        val nodes = plannerDao
            .getPriorityNodesOnce(priority)
            .toMutableList()

        require(fromIndex in nodes.indices)
        require(toIndex in nodes.indices)

        val movedNode = nodes.removeAt(fromIndex)
        nodes.add(toIndex, movedNode)

        nodes.forEachIndexed { index, plannerNode ->

            if (plannerNode.priorityOrder != index) {

                plannerDao.updatePlannerNode(
                    plannerNode.copy(
                        priorityOrder = index
                    )
                )
            }
        }
    }

    suspend fun addToTodayQueue(
        nodeId: Long
    ) {

        val plannerNode = requirePlannerNode(nodeId)

        // Ignore duplicate requests.
        if (isInTodayQueue(nodeId)) {
            return
        }

        check(plannerDao.getTodayQueueCount() < TODAY_QUEUE_CAPACITY) {
            "Today's queue is full."
        }

        appendToTodayQueue(plannerNode)
    }

    suspend fun removeFromTodayQueue(
        nodeId: Long
    ) {

        val plannerNode = plannerDao.getTodayQueueNode(nodeId)
            ?: return

        plannerDao.updatePlannerNode(
            plannerNode.copy(
                todayOrder = null
            )
        )

        normalizeTodayQueue()
    }

    suspend fun reorderTodayQueue(
        fromIndex: Int,
        toIndex: Int
    ) {

        if (fromIndex == toIndex) {
            return
        }

        val nodes = plannerDao
            .getTodayQueueOnce()
            .toMutableList()

        require(fromIndex in nodes.indices)
        require(toIndex in nodes.indices)

        val movedNode = nodes.removeAt(fromIndex)
        nodes.add(toIndex, movedNode)

        nodes.forEachIndexed { index, plannerNode ->

            if (plannerNode.todayOrder != index) {

                plannerDao.updatePlannerNode(
                    plannerNode.copy(
                        todayOrder = index
                    )
                )
            }
        }
    }

    /* -------------------------------------------------------------------------
 * Helpers
 * ------------------------------------------------------------------------- */

    /**
     * Returns the maximum capacity for a priority bucket.
     */
    private fun getBucketCapacity(
        priority: PlannerPriority
    ): Int {
        return when (priority) {
            PlannerPriority.CRITICAL -> 3
            PlannerPriority.HIGH -> 5
            PlannerPriority.MEDIUM -> 6
            PlannerPriority.LOW -> 10
            PlannerPriority.BACKLOG -> 20
        }
    }

    /**
     * Returns true if the given priority bucket has reached its capacity.
     */
    private suspend fun isBucketFull(
        priority: PlannerPriority
    ): Boolean {
        return plannerDao.getPriorityCount(priority) >= getBucketCapacity(priority)
    }

    /**
     * Returns the planner node or throws if it does not exist.
     */
    private suspend fun requirePlannerNode(
        nodeId: Long
    ): PlannerNodeEntity {
        return plannerDao.getPlannerNode(nodeId)
            ?: throw IllegalStateException(
                "Planner node not found for nodeId = $nodeId"
            )
    }

    /**
     * Inserts a node at the end of the given priority bucket.
     *
     * Assumes:
     * - Node is valid.
     * - Node is not already in planner.
     * - Bucket has available capacity.
     */
    private suspend fun insertIntoBucket(
        nodeId: Long,
        priority: PlannerPriority
    ) {

        val priorityOrder = plannerDao.getPriorityCount(priority)

        plannerDao.insertPlannerNode(
            PlannerNodeEntity(
                nodeId = nodeId,
                priority = priority,
                priorityOrder = priorityOrder,
                todayOrder = null,
                addedToPlannerAt = System.currentTimeMillis()
            )
        )
    }

    /**
     * Returns true if the node already exists in planner.
     */
    private suspend fun isInPlanner(
        nodeId: Long
    ): Boolean {
        return plannerDao.exists(nodeId)
    }

    private fun getNextPriority(
        priority: PlannerPriority
    ): PlannerPriority? {
        return when (priority) {
            PlannerPriority.CRITICAL -> PlannerPriority.HIGH
            PlannerPriority.HIGH -> PlannerPriority.MEDIUM
            PlannerPriority.MEDIUM -> PlannerPriority.LOW
            PlannerPriority.LOW -> PlannerPriority.BACKLOG
            PlannerPriority.BACKLOG -> null
        }
    }

    private suspend fun getLastNodeInBucket(
        priority: PlannerPriority
    ): PlannerNodeEntity {

        return plannerDao.getLastNodeInPriority(priority)
            ?: throw IllegalStateException(
                "Priority bucket '$priority' is empty."
            )
    }

    private suspend fun moveToBucket(
        plannerNode: PlannerNodeEntity,
        priority: PlannerPriority
    ) {

        val newOrder = plannerDao.getPriorityCount(priority)

        plannerDao.updatePlannerNode(
            plannerNode.copy(
                priority = priority,
                priorityOrder = newOrder
            )
        )
    }

    private suspend fun normalizeBucket(
        priority: PlannerPriority
    ) {

        val nodes =
            plannerDao.getPriorityNodesOnce(priority)

        nodes.forEachIndexed { index, plannerNode ->

            if (plannerNode.priorityOrder != index) {

                plannerDao.updatePlannerNode(
                    plannerNode.copy(
                        priorityOrder = index
                    )
                )
            }
        }
    }

    private suspend fun cascadeDown(
        priority: PlannerPriority
    ) {

        val nextPriority = getNextPriority(priority)
            ?: throw IllegalStateException(
                "Planner is full. Remove some backlog tasks."
            )

        if (isBucketFull(nextPriority)) {
            cascadeDown(nextPriority)
        }

        val lastNode = getLastNodeInBucket(priority)

        moveToBucket(
            plannerNode = lastNode,
            priority = nextPriority
        )

        normalizeBucket(priority)
    }

    //----------- helpers of todayqueue
    private suspend fun isInTodayQueue(
        nodeId: Long
    ): Boolean {
        return plannerDao.getTodayQueueNode(nodeId) != null
    }

    private companion object {
        const val TODAY_QUEUE_CAPACITY = 10
    }

    private suspend fun appendToTodayQueue(
        plannerNode: PlannerNodeEntity
    ) {

        val todayOrder = plannerDao.getTodayQueueCount()

        plannerDao.updatePlannerNode(
            plannerNode.copy(
                todayOrder = todayOrder
            )
        )
    }

    private suspend fun normalizeTodayQueue() {

        val nodes = plannerDao.getTodayQueueOnce()

        nodes.forEachIndexed { index, plannerNode ->

            if (plannerNode.todayOrder != index) {

                plannerDao.updatePlannerNode(
                    plannerNode.copy(
                        todayOrder = index
                    )
                )
            }
        }
    }

    private fun buildPlannerNodeUiModel(
        plannerNode: PlannerNodeEntity,
        project: UnplannedProjectEntity,
        isLeaf: Boolean,
        depth: Int
    ): PlannerNodeUiModel {

        return PlannerNodeUiModel(

            // Identity
            nodeId = project.nodeId,

            // Display
            title = project.title,

            // Tree
            parentNodeId = project.parentNodeId,
            depth = depth,
            isLeaf = isLeaf,

            // Planner
            priority = plannerNode.priority,
            priorityOrder = plannerNode.priorityOrder,
            todayOrder = plannerNode.todayOrder,

            // Progress
            isCompleted = project.isCompleted ?: false,
            expectedDurationSeconds = project.expectedDurationSeconds
        )
    }

    private fun isLeaf(
        project: UnplannedProjectEntity,
        projectMap: Map<Long, UnplannedProjectEntity>
    ): Boolean {

        return projectMap.values.none { node ->
            node.parentNodeId == project.nodeId
        }
    }

    private fun calculateDepth(
        project: UnplannedProjectEntity,
        projectMap: Map<Long, UnplannedProjectEntity>
    ): Int {

        var depth = 0
        var currentParentId = project.parentNodeId

        while (currentParentId != null) {

            depth++

            currentParentId = projectMap[currentParentId]?.parentNodeId
        }

        return depth
    }

    // ---- for the planner feature to reduce searching complexity to O(1)
    private fun buildProjectMap(
        projects: List<UnplannedProjectEntity>
    ): Map<Long, UnplannedProjectEntity> {

        return projects.associateBy { it.nodeId }
    }

    // --- main function for screen date
    private fun buildPlannerUiState(
        plannerNodes: List<PlannerNodeEntity>,
        projects: List<UnplannedProjectEntity>
    ): PlannerUiState {

        val projectMap = buildProjectMap(projects)
        val criticalTasks = mutableListOf<PlannerNodeUiModel>()
        val highTasks = mutableListOf<PlannerNodeUiModel>()
        val mediumTasks = mutableListOf<PlannerNodeUiModel>()
        val lowTasks = mutableListOf<PlannerNodeUiModel>()
        val backlogTasks = mutableListOf<PlannerNodeUiModel>()
        val todayQueue = mutableListOf<PlannerNodeUiModel>()

        plannerNodes.forEach { plannerNode ->

            val project = projectMap[plannerNode.nodeId]
                ?: return@forEach

            val isLeaf = isLeaf(
                project = project,
                projectMap = projectMap
            )

            val depth = calculateDepth(
                project = project,
                projectMap = projectMap
            )

            val plannerNodeUiModel = buildPlannerNodeUiModel(
                plannerNode = plannerNode,
                project = project,
                isLeaf = isLeaf,
                depth = depth
            )

            when (plannerNode.priority) {

                PlannerPriority.CRITICAL -> {
                    criticalTasks.add(plannerNodeUiModel)
                }

                PlannerPriority.HIGH -> {
                    highTasks.add(plannerNodeUiModel)
                }

                PlannerPriority.MEDIUM -> {
                    mediumTasks.add(plannerNodeUiModel)
                }

                PlannerPriority.LOW -> {
                    lowTasks.add(plannerNodeUiModel)
                }

                PlannerPriority.BACKLOG -> {
                    backlogTasks.add(plannerNodeUiModel)
                }
            }

            if (plannerNode.todayOrder != null) {
                todayQueue.add(plannerNodeUiModel)
            }
        }

        return PlannerUiState(
            isLoading = false,
            criticalTasks = criticalTasks,
            highTasks = highTasks,
            mediumTasks = mediumTasks,
            lowTasks = lowTasks,
            backlogTasks = backlogTasks,
            todayQueue = todayQueue
        )

    }

    fun observePlanner(): Flow<PlannerUiState> {

        return combine(
            plannerDao.getAllPlannerNodes(),
            unplannedProjectRepository.getAllNodes()
        ) { plannerNodes, projects ->

            buildPlannerUiState(
                plannerNodes = plannerNodes,
                projects = projects
            )
        }
    }





}

