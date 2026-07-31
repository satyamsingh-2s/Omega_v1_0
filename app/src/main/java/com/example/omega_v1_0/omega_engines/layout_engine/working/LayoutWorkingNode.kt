package com.example.omega_v1_0.omega_engines.layout_engine.working

// --- layoutWorkingNode is responsible for the node only
data class LayoutWorkingNode(

    // ============================================================
    // Structural Information (Copied from TreeAnalyzer)
    // These values never change during layout calculation.
    // ============================================================

    // Unique identifier of the original project node.
    val nodeId: Long,

    // Direct parent reference.
    // Used frequently by the Buchheim algorithm while walking upward.
    // Null only for the root node.
    var parent: LayoutWorkingNode? = null,

    // Depth from the root.
    // Root = 0, children = 1, grandchildren = 2...
    val depth: Int,

    // Total number of descendants in this subtree.
    // Pure structural information.
    val descendantCount: Int,

    // Total number of leaf nodes contained in this subtree.
    // Used later while calculating subtree width.
    val leafCount: Int,

    // Child hierarchy.
    // The structure never changes, only the nodes themselves mutate.
    val children: MutableList<LayoutWorkingNode>,


    // ============================================================
    // Buchheim Algorithm State (Temporary)
    // These fields exist only while the layout algorithm executes.
    // ============================================================

    // Preliminary X coordinate calculated during First Walk.
    var prelim: Float = 0f,

    // Offset applied to the entire subtree.
    // Accumulated during conflict resolution.
    var modifier: Float = 0f,

    // Temporary shift used while moving subtrees apart.
    var shift: Float = 0f,

    // Temporary accumulated change value used by Buchheim.
    var change: Float = 0f,

    // Temporary thread connecting contours of different subtrees.
    // Exists only while the algorithm executes.
    var thread: LayoutWorkingNode? = null,

    // Current ancestor used during subtree conflict resolution.
    var ancestor: LayoutWorkingNode? = null,

    // Position of this node among its siblings.
    // First child = 1, Second = 2 ...
    var number: Int = 0,


    // ============================================================
    // Final Layout Geometry (Output)
    // These values are the result of the layout algorithm.
    // ============================================================

    // Horizontal space occupied by this subtree.
    var subtreeWidth: Float = 0f,

    // Final X coordinate of the node center.
    var centerX: Float = 0f,

    // Final Y coordinate of the node center.
    var centerY: Float = 0f

) {

    // ============================================================
// Navigation Helpers ---> simply means functions
// These are convenience properties used heavily by the
// Buchheim algorithm.
// ============================================================

    /**
     * True if this node is the root.
     */
    val isRoot: Boolean
        get() = parent == null

    /**
     * True if this node has no children.
     */
    val isLeaf: Boolean
        get() = children.isEmpty()

    /**
     * Number of direct children.
     */
    val childCount: Int
        get() = children.size

    /**
     * Immediate left sibling.
     *
     * Example:
     *
     * A
     * ├── B
     * ├── C
     * └── D
     *
     * leftSibling(C) = B
     */
    val leftSibling: LayoutWorkingNode?
        get() {
            val p = parent ?: return null

            if (number <= 1) {
                return null
            }

            return p.children[number - 2]
        }

    /**
     * Immediate right sibling.
     *
     * O(1)
     */
    val rightSibling: LayoutWorkingNode?
        get() {

            val p = parent ?: return null

            if (number >= p.childCount) {
                return null
            }

            return p.children[number]
        }

    /**
     * First sibling under the same parent.
     *
     * Example:
     *
     * A
     * ├── B
     * ├── C
     * └── D
     *
     * leftMostSibling(D) = B
     */
    val leftMostSibling: LayoutWorkingNode?
        get() {
            val p = parent ?: return null

            return p.children.firstOrNull()
        }

    /**
     * First child of this node.
     */
    val leftMostChild: LayoutWorkingNode?
        get() = children.firstOrNull()

    /**
     * Last child of this node.
     */
    val rightMostChild: LayoutWorkingNode?
        get() = children.lastOrNull()

    /**
     * True if this node is the first child of its parent.
     */
    val isLeftMostSibling: Boolean
        get() = number == 1

    /**
     * True if this node is the last child of its parent.
     */
    val isRightMostSibling: Boolean
        get() = parent?.childCount == number
}