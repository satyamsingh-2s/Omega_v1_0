package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.analyzer

data class AnalyzedNode(

    val nodeId: Long,

    val depth: Int,

    val parentNodeId: Long?,

    val descendantCount: Int,

    val leafCount: Int,

    val children: List<AnalyzedNode>

)