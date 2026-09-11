package com.satyamsingh2s.productivity.omega.ui.model


// -- it is the bridge between repo -- ui model -- viewmodel
// - it represents the node,
data class UnplannedProjectUiModel(

    val nodeId: Long,

    val title: String,

    val children: List<UnplannedProjectUiModel>,

    val expectedDurationSeconds: Int,

    val currentDurationSeconds: Int,

    val isCompleted: Boolean,

    val accentIndex: Int
)
{
    /**
     * Calculates the completion progress of this node and its children.
     * - If a node has no children (leaf node), its progress is 100% if `isCompleted` is true, otherwise 0%.
     * - If a node has children, its progress is the average of its children's `completionProgress`.
     */
    val completionProgress: Float
        get() {
            if (children.isEmpty()) {
                // Leaf node: progress is binary based on its own completion status
                return if (isCompleted) 1.0f else 0.0f
            } else {
                // Parent node: progress is the average of its children's progress
                val totalChildren = children.size
                if (totalChildren == 0) {
                    // This case should ideally not be reached if children.isEmpty() is checked first,
                    // but as a fallback, treat as a leaf node.
                    return if (isCompleted) 1.0f else 0.0f
                }

                val completedChildrenProgressSum = children.sumOf { it.completionProgress.toDouble() }
                return (completedChildrenProgressSum / totalChildren).toFloat()
            }
        }
}