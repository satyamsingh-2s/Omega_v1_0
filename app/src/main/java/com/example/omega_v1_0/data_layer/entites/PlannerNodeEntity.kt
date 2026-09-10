package com.example.omega_v1_0.data_layer.entites


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.omega_v1_0.models_enums.PlannerPriority


@Entity(
    tableName = "planner_nodes"
)
data class PlannerNodeEntity(

    /**
     * References UnplannedProjectEntity.nodeId.
     *
     * Rules:
     * - Only leaf nodes are allowed.
     * - One planner entry per node.
     */
    @PrimaryKey
    val nodeId: Long,

    /**
     * Planner bucket.
     */
    val priority: PlannerPriority,

    /**
     * Position inside its own priority bucket.
     *
     * Rules:
     * - Starts from 0.
     * - Unique within the same priority.
     * - Repository maintains contiguous ordering.
     */
    val priorityOrder: Int,

    /**
     * Position inside Today's Queue.
     *
     * Null -> Not scheduled today.
     * 0    -> First task.
     * 1    -> Second task.
     */
    val todayOrder: Int?,

    /**
     * Timestamp when the node was added to the planner.
     */
    val addedToPlannerAt: Long
)