package com.example.omega_v1_0.omega_engines.layout_engine.analyzer

import com.example.omega_v1_0.omega_engines.layout_engine.model.LayoutTreeNode

object TreeAnalyzer {

    fun analyze(
        root: LayoutTreeNode
    ): AnalyzedTree {

        return AnalyzedTree(
            root = analyzeNode(
                node = root,
                depth = 0,
                parentNodeId = null
            )
        )
    }

    private fun analyzeNode(
        node: LayoutTreeNode,
        depth: Int,
        parentNodeId: Long?
    ): AnalyzedNode {

        // Recursively analyze children
        val children =
            node.children.map {
                analyzeNode(
                    node = it,
                    depth = depth + 1,
                    parentNodeId = node.nodeId
                )
            }

        // Total descendants of this node
        val descendantCount =
            children.sumOf {
                it.descendantCount + 1
            }
        // leaf count
        val leafCount =
            if (children.isEmpty()) {
                1
            } else {
                children.sumOf {
                    it.leafCount
                }
            }

        // Return analyzed node
        return AnalyzedNode(
            nodeId = node.nodeId,
            parentNodeId = parentNodeId,
            depth = depth,
            descendantCount = descendantCount,
            children = children,
            leafCount = leafCount,

        )
    }
}