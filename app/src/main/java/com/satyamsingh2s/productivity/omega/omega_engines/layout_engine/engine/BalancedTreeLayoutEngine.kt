package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.engine

import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.analyzer.AnalyzedNode
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.analyzer.AnalyzedTree
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.api.LayoutConfig
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.api.LayoutEngine
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.model.LayoutModel
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.working.LayoutWorkingNode
import com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.working.LayoutWorkingTree

/**
 * Layout engine implementation based on the
 * Buchheim Tidy Tree Algorithm.
 *
 * Pipeline
 *
 * AnalyzedTree
 *      ↓
 * LayoutWorkingTree
 *      ↓
 * Buchheim Algorithm
 *      ↓
 * LayoutModel
 */
class BalancedTreeLayoutEngine :
    LayoutEngine<AnalyzedTree> {

    /**
     * Current layout configuration.
     * Stored once so every Buchheim helper can access it.
     */
    private lateinit var config: LayoutConfig

    override fun calculate(
        input: AnalyzedTree,
        config: LayoutConfig
    ): LayoutModel {

        // Store layout configuration.
        this.config = config

        // Build mutable working tree.
        val workingTree =
            createWorkingTree(input)

        // ----------------------------
        // Buchheim Algorithm
        // ----------------------------

        firstWalk(
            workingTree.root
        )

        secondWalk(
            workingTree.root
        )

        // Convert into immutable layout model.
        return buildLayoutModel(
            workingTree.root
        )
    }

    // ============================================================
    // Working Tree Construction
    // ============================================================

    /**
     * Converts the immutable analyzed tree into a mutable
     * working tree.
     */
    private fun createWorkingTree(
        analyzedTree: AnalyzedTree
    ): LayoutWorkingTree {

        return LayoutWorkingTree(

            root = createWorkingNode(

                node = analyzedTree.root,

                parent = null,

                siblingNumber = 1

            )

        )
    }

    /**
     * Recursively creates the mutable working tree.
     *
     * During construction this method:
     *
     * • connects parent references
     * • assigns sibling numbers
     */
    private fun createWorkingNode(
        node: AnalyzedNode,
        parent: LayoutWorkingNode?,
        siblingNumber: Int
    ): LayoutWorkingNode {

        val workingNode = LayoutWorkingNode(

            // -------------------------
            // Structural Information
            // -------------------------

            nodeId = node.nodeId,
            parent = parent,
            depth = node.depth,
            descendantCount = node.descendantCount,
            leafCount = node.leafCount,
            children = mutableListOf(),

            // -------------------------
            // Buchheim State
            // -------------------------
            number = siblingNumber

        )

        // -- we are implemetnig the fix, after completing the ancestor function, and resaon that every in buchheim evey node conside  itself to be its own ancestors
        workingNode.ancestor = workingNode

        node.children.forEachIndexed { index, child ->

            val workingChild = createWorkingNode(

                node = child,
                parent = workingNode,
                siblingNumber = index + 1

            )

            workingNode.children.add(
                workingChild
            )
        }

        return workingNode
    }

    // ============================================================
    // Buchheim Tidy Tree Algorithm
    // ============================================================

    /**
     * First tree traversal.
     */
    private fun firstWalk(
        node: LayoutWorkingNode
    ){

        if (node.isLeaf) {
            return
        }

        // Case 2 : Internal Node ---------------------------------------------------

// Initially the left-most child is the default ancestor.
        var defaultAncestor = node.leftMostChild!!

// Recursively process all children.

        node.children.forEach { child ->

            firstWalk(child)

            defaultAncestor =
                apportion(
                    child,
                    defaultAncestor
                )
        }
        // Apply all accumulated subtree shifts.
        executeShifts(node)


//   ------------- Center position of all child subtrees.
        val midpoint = (node.leftMostChild!!.prelim + node.rightMostChild!!.prelim) / 2f
// If there is a left sibling,
// place this subtree to the right of it.
        if (node.leftSibling != null) {

            node.prelim =
                node.leftSibling!!.prelim +
                        config.horizontalSpacing

            node.modifier =
                node.prelim - midpoint

        }

// Otherwise simply center this node
// above its children.
        else {
            node.prelim = midpoint
        }




    }


    /**
     * Resolves conflicts between neighbouring subtrees.
     */
    private fun apportion(
        node: LayoutWorkingNode,
        defaultAncestor: LayoutWorkingNode
    ): LayoutWorkingNode {
        val leftSibling = node.leftSibling
            ?: return defaultAncestor

        var insideRight = leftSibling // right contour of the previous subtree(left)
        var insideLeft = node  // left contour of the current subtree(right)
        var outsideLeft = leftSibling // left contour of the previous subtree(left)
        var outsideRight = node // right contour of the current subtree(right)

        // Running modifier accumulated along each contour.
        var modifierInsideRight = insideRight.modifier
        var modifierInsideLeft = insideLeft.modifier
        var modifierOutsideLeft = outsideLeft.modifier
        var modifierOutsideRight = outsideRight.modifier

        // --- contour traversal
        while (
            nextRight(insideRight) != null &&
            nextLeft(insideLeft) != null
        ) {

            insideRight = nextRight(insideRight)!!
            insideLeft = nextLeft(insideLeft)!!

            outsideLeft = nextLeft(outsideLeft)!!
            outsideRight = nextRight(outsideRight)!!

            // Refresh ancestor reference for the
// current outer-right contour walker.
            outsideRight.ancestor = node


           //  Current horizontal position of the
// right contour of the left subtree.
            val leftContourX =
                insideRight.prelim + modifierInsideRight
// Current horizontal position of the
// left contour of the current subtree.
            val rightContourX =
                insideLeft.prelim + modifierInsideLeft


            // Horizontal movement required to maintain
            // the minimum spacing between the two contours.
            val requiredShift = (leftContourX + config.horizontalSpacing)- rightContourX

            val subtreeAncestor =
                ancestor(
                    insideRight = insideRight,
                    node = node,
                    defaultAncestor = defaultAncestor
                )

            // Only move the current subtree if the
// required spacing is not satisfied.
            if (requiredShift > 0f) {

                moveSubtree(
                    leftSubtree = subtreeAncestor,
                    rightSubtree = node,
                    shift = requiredShift
                )

                // ------------------------------------------------
                // Phase 8.3.6
                // Update shift accumulators
                // ------------------------------------------------

                modifierInsideRight += requiredShift
                modifierOutsideRight += requiredShift
            }
            // ----------------------------------------------------
// Phase 8.3.7
// Update modifier accumulators
// ----------------------------------------------------

            modifierInsideLeft += insideLeft.modifier
            modifierInsideRight += insideRight.modifier

            modifierOutsideLeft += outsideLeft.modifier
            modifierOutsideRight += outsideRight.modifier



        }




        return defaultAncestor

    }

    /** 2nd implementd
     * Moves an entire subtree horizontally.
     */
    /**
     * Records a horizontal movement of an entire subtree.
     *
     * The actual movement will later be propagated by
     * executeShifts().
     */
    private fun moveSubtree(
        leftSubtree: LayoutWorkingNode,
        rightSubtree: LayoutWorkingNode,
        shift: Float

    ) {

        val siblingDistance =
            rightSubtree.number -
                    leftSubtree.number

        if (siblingDistance <= 0) {
            return
        }

        val ratio = shift / siblingDistance
        rightSubtree.change -= ratio
        rightSubtree.shift += shift
        leftSubtree.change += ratio
        rightSubtree.prelim += shift
        rightSubtree.modifier += shift
    }

    /** 1st implemnted
     * Applies accumulated shifts to children.
     */
    /**
     * Applies all accumulated shifts to this node's children.
     *
     * Children are processed from right to left.
     */
    private fun executeShifts(
        node: LayoutWorkingNode
    ) {
        var shift = 0f
        var change = 0f

        for (i in node.children.lastIndex downTo 0) {

            val child = node.children[i]
            child.prelim += shift
            child.modifier += shift
            change += child.change
            shift += child.shift + change
        }
    }

    // --- contour traversal functions
    private fun nextLeft(
        node: LayoutWorkingNode?
    ): LayoutWorkingNode? {

        return node?.leftMostChild ?: node?.thread
    }
    private fun nextRight(
        node: LayoutWorkingNode?
    ): LayoutWorkingNode? {

        return node?.rightMostChild ?: node?.thread
    }


    /**
     * Resolves the correct ancestor to use when moving subtrees.
     *
     * If the stored ancestor still belongs to the current parent,
     * it is returned. Otherwise, the default ancestor is used.
     */
    private fun ancestor(
        insideRight: LayoutWorkingNode,
        node: LayoutWorkingNode,
        defaultAncestor: LayoutWorkingNode
    ): LayoutWorkingNode {

        val ancestor = insideRight.ancestor

        return if (
            ancestor != null &&
            ancestor.parent == node.parent
        ) {
            ancestor
        } else {
            defaultAncestor
        }
    }

    /**
     * Second tree traversal.
     *
     * Converts preliminary coordinates into final coordinates.
     */
    private fun secondWalk(
        node: LayoutWorkingNode,
        modifier: Float = 0f
    ) {

    }

    // ============================================================
    // Layout Model Construction
    // ============================================================

    /**
     * Converts the working tree into an immutable LayoutModel.
     */
    private fun buildLayoutModel(
        root: LayoutWorkingNode
    ): LayoutModel {

        TODO("Implemented after Buchheim algorithm")

    }

}