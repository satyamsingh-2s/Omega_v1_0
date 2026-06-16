package com.example.omega_v1_0.data_layer.omega_repository

import com.example.omega_v1_0.data_layer.dao.SessionDao
import com.example.omega_v1_0.data_layer.dao.UnplannedProjectDao
import com.example.omega_v1_0.data_layer.entites.SessionEntity
import com.example.omega_v1_0.data_layer.entites.UnplannedProjectEntity
import com.example.omega_v1_0.models.SessionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


// ----------- concept --------------
//DAOs must be interfaces (or abstract classes) because Room generates their implementation.
//Repositories are usually normal classes, but sometimes we make them interfaces to hide the implementation and allow multiple implementations later.

class UnplannedProjectRepository(
    private val projectDao: UnplannedProjectDao,
    private val sessionDao: SessionDao
) {

//    fun getUnplannedTree(): Flow<List<UnplannedNode>> {
//
//        return combine(
//
//            projectDao.getAllNodes(),
//
//            sessionDao.getSessionsByType(
//                SessionType.UNPLANNED
//            )
//
//        ) { nodes, sessions ->
//
//            buildForest(
//                nodes = nodes,
//                sessions = sessions
//            )
//        }
//    }
//
//    private fun buildForest(
//        nodes: List<UnplannedProjectEntity>,
//        sessions: List<SessionEntity>
//    ): List<UnplannedNode> {
//
//        // Parent -> Children lookup
//        val childrenByParent =
//            nodes.groupBy { it.parentNodeId }
//
//        // Node -> Sessions lookup
//        val sessionsByNode =
//            sessions.groupBy { it.parentId }
//
//        // Find root nodes
//        val roots =
//            childrenByParent[null]
//                ?.sortedBy { it.sortOrder }
//                ?: emptyList()
//
//        // Build the forest
//        return roots.map { root ->
//
//            buildNode(
//                entity = root,
//
//                childrenByParent = childrenByParent,
//
//                sessionsByNode = sessionsByNode
//            )
//        }
//    }
//
//    private fun buildNode(
//
//        entity: UnplannedProjectEntity,
//        childrenByParent: Map<Long?, List<UnplannedProjectEntity>>,
//        sessionsByNode: Map<Long, List<SessionEntity>>
//    ): UnplannedNode {
//
//        // Get direct children entities
//        val childEntities =
//            childrenByParent[entity.nodeId]
//                ?.sortedBy { it.sortOrder }
//                ?: emptyList()
//
//        // Recursively build children
//        val children =
//            childEntities.map {
//
//                buildNode(
//
//                    entity = it,
//
//                    childrenByParent = childrenByParent,
//
//                    sessionsByNode = sessionsByNode
//                )
//            }
//
//        // LEAF NODE
//        if (children.isEmpty()) {
//
//            val expectedDurationSeconds =
//
//                entity.expectedDurationSeconds ?: 0
//
//            val currentDurationSeconds =
//
//                sessionsByNode[entity.nodeId]
//                    ?.sumOf { it.durationSeconds }
//                    ?: 0
//
//            val isCompleted =
//                entity.isCompleted ?: false
//            return UnplannedNode(
//                nodeId = entity.nodeId,
//                title = entity.title,
//                children = emptyList(),
//                expectedDurationSeconds = expectedDurationSeconds,
//                currentDurationSeconds = currentDurationSeconds,
//                isCompleted = isCompleted
//            )
//        }
//
//        // PARENT NODE
//
//        val expectedDurationSeconds =
//
//            children.sumOf {
//                it.expectedDurationSeconds
//            }
//
//        val currentDurationSeconds =
//
//            children.sumOf {
//                it.currentDurationSeconds
//            }
//
//        val isCompleted =
//
//            children.all {
//                it.isCompleted
//            }
//
//        return UnplannedNode(
//            nodeId = entity.nodeId,
//            title = entity.title,
//            children = children,
//            expectedDurationSeconds = expectedDurationSeconds,
//            currentDurationSeconds = currentDurationSeconds,
//            isCompleted = isCompleted
//        )
//    }
//
//    suspend fun createRootNode(
//        title: String
//    ) {
//        TODO()
//    }
//
//    suspend fun createChildNode(
//        parentNodeId: Long,
//        title: String
//    ) {
//        TODO()
//    }
//
//    suspend fun getNodeById(
//        nodeId: Long
//    ): UnplannedProjectEntity? {
//        TODO()
//    }
//
//    suspend fun renameNode(
//        nodeId: Long,
//        newTitle: String
//    ) {
//        TODO()
//    }
//
//    suspend fun updateExpectedDuration(
//        nodeId: Long,
//        expectedDurationSeconds: Int
//    ) {
//        TODO()
//    }
//
//    suspend fun markNodeCompleted(
//        nodeId: Long
//    ) {
//        TODO()
//    }
//
//    suspend fun markNodeIncomplete(
//        nodeId: Long
//    ) {
//        TODO()
//    }
}