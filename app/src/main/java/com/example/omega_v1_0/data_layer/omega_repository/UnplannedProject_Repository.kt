package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.dao.UnplannedProjectDao
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.data_layer.entites.UnplannedProjectEntity
import com.example.omega_v1_0.data_layer.imports.ImportNode
import com.example.omega_v1_0.data_layer.imports.OmegaImport
import com.example.omega_v1_0.models.SessionType
import com.example.omega_v1_0.ui.model.UnplannedProjectUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


// ----------- concept --------------
//DAOs must be interfaces (or abstract classes) because Room generates their implementation.
//Repositories are usually normal classes, but sometimes we make them interfaces to hide the implementation and allow multiple implementations later.
// here, in repository we are creating trees, we simply collect raw data form (unplannedprojectdao and sessiondao) and convert them here to make tress

class UnplannedProjectRepository(
    private val unplannedProjectDao: UnplannedProjectDao,
    private val sessionDao: SessionDao
) {

    fun getUnplannedTree(): Flow<List<UnplannedProjectUiModel>> {

        return combine(
            unplannedProjectDao.getAllNodes(),
            sessionDao.getSessionsByType(SessionType.UNPLANNED)
        ) {
            nodes, sessions ->
            buildForest(
                nodes = nodes,
                sessions = sessions
            )
        }
    }


    private fun buildForest(nodes: List<UnplannedProjectEntity>, sessions: List<SessionEntity>)
    :List<UnplannedProjectUiModel> {

        // Parent -> Children lookup
        val childrenByParent = nodes.groupBy { it.parentNodeId }

        // Node -> Sessions lookup
        val sessionsByNode = sessions.groupBy { it.parentId }

        // Find root nodes
        val roots = childrenByParent[null]
                ?.sortedBy { it.sortOrder }
                ?: emptyList()

        // Build the forest ------------- understand how the function work later
        return roots.map { root ->
            buildNode(
                entity = root,
                childrenByParent = childrenByParent,
                sessionsByNode = sessionsByNode
            )
        }
    }
//--------------------------- build node ----------------------------
    // here tree is build from the root node, pattern use deep search recursion (DFS)
    // every time build node recives root node and find children and do it recursivewly until it find leaf node
    private fun buildNode(

        entity: UnplannedProjectEntity,
        childrenByParent: Map<Long?, List<UnplannedProjectEntity>>,
        sessionsByNode: Map<Long, List<SessionEntity>>
    ): UnplannedProjectUiModel {

        // Get direct children entities
        val childEntities =
            childrenByParent[entity.nodeId]
                ?.sortedBy { it.sortOrder }
                ?: emptyList()

        // Recursively build children
        val children =
            childEntities.map {
                buildNode(
                    entity = it,
                    childrenByParent = childrenByParent,
                    sessionsByNode = sessionsByNode
                )
            }

        // LEAF NODE
        if (children.isEmpty()) {
            val expectedDurationSeconds = entity.expectedDurationSeconds ?: 0
            val currentDurationSeconds = sessionsByNode[entity.nodeId]
                    ?.sumOf { it.durationSeconds }
                    ?: 0

            val isCompleted = entity.isCompleted ?: false
            return UnplannedProjectUiModel(
                nodeId = entity.nodeId,
                title = entity.title,
                children = emptyList(),
                expectedDurationSeconds = expectedDurationSeconds,
                currentDurationSeconds = currentDurationSeconds,
                isCompleted = isCompleted
            )
        }

        // PARENT NODE

        val expectedDurationSeconds =
            children.sumOf { it.expectedDurationSeconds
            }
        val currentDurationSeconds =
            children.sumOf {
                it.currentDurationSeconds
            }

        val isCompleted = children.all {
                it.isCompleted
            }

        return UnplannedProjectUiModel(
            nodeId = entity.nodeId,
            title = entity.title,
            children = children,
            expectedDurationSeconds = expectedDurationSeconds,
            currentDurationSeconds = currentDurationSeconds,
            isCompleted = isCompleted
        )
    }
    // ------------------------- build node ---------------------------

    suspend fun createRootNode(
        title: String
    ):Long {  // change 1 --- for import feature

        val sortOrder = unplannedProjectDao.getMaxRootSortOrder() + 1
        val node = UnplannedProjectEntity(
            parentNodeId = null,
                title = title,
                sortOrder = sortOrder,
                createdAt = System.currentTimeMillis(),
                isCompleted = false,
                expectedDurationSeconds = null
            )
     return unplannedProjectDao.insertNode(node)
    }

    suspend fun createChildNode(parentNodeId: Long, title: String
    ): Long { // change 2 --- for import feature
        val sortOrder = unplannedProjectDao.getMaxChildSortOrder(parentNodeId) + 1

        val node = UnplannedProjectEntity(
                parentNodeId = parentNodeId,
                title = title,
                sortOrder = sortOrder,
                createdAt = System.currentTimeMillis(),
                isCompleted = false,
                expectedDurationSeconds = null
            )
      return unplannedProjectDao.insertNode(node)
    }

    suspend fun getNodeById(nodeId: Long
    ): UnplannedProjectEntity? {
        return unplannedProjectDao.getNodeById(nodeId)
    }

    suspend fun renameNode(nodeId: Long, newTitle: String
    ) {
        val node = getNodeById(nodeId) ?: return
        unplannedProjectDao.updateNode(node.copy(title = newTitle))
    }


    suspend fun updateExpectedDuration(nodeId: Long,
        expectedDurationSeconds: Int?
    ) {
        //---- restricting the updateExpectedDuration to update on leaf only
        if (!isLeaf(nodeId))
            return

        val node = getNodeById(nodeId) ?: return
        unplannedProjectDao.updateNode(node.copy(expectedDurationSeconds = expectedDurationSeconds)
        )
    }


    suspend fun markNodeCompleted(nodeId: Long
    ) {
        //  ------------ restricting the markNodeCompleted to update on leaf only -----------
        if (!isLeaf(nodeId))
            return

        val node = getNodeById(nodeId) ?: return
        unplannedProjectDao.updateNode(node.copy(isCompleted = true))
    }


    suspend fun markNodeIncomplete(nodeId: Long
    ) {
        // ---------- restricting  --------------
        if (!isLeaf(nodeId))
            return

        val node = getNodeById(nodeId) ?: return
        unplannedProjectDao.updateNode(node.copy(isCompleted = false))
    }

    private suspend fun isLeaf(nodeId: Long
    ): Boolean {
        return unplannedProjectDao
            .getChildCount(nodeId) == 0
    }

    // ------------- a helper -----=============
    suspend fun canStartSession(nodeId: Long
    ): Boolean {
        return isLeaf(nodeId)
    }


    suspend fun importStructure(
        omegaImport: OmegaImport
    ) {

        val rootId =
            createRootNode(omegaImport.project
            )

        omegaImport.structure.forEach {
            createNodeRecursively(
                rootId,
                it
            )
        }
    }

    private suspend fun createNodeRecursively(

        parentId: Long,
        node: ImportNode

    ) {

        val nodeId =

            createChildNode(

                parentId,

                node.title
            )

        if (

            node.children.isEmpty()

        ) {

            updateExpectedDuration(

                nodeId,

                node.estimatedHours * 3600
            )
        }

        node.children.forEach {

            createNodeRecursively(

                nodeId,

                it
            )
        }
    }

}