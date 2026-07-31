package com.example.omega_v1_0.omega_engines.layout_engine.analyzer

data class AnalyzedNode(

    val nodeId: Long,

    val depth: Int,

    val parentNodeId: Long?,

    val descendantCount: Int,

    val leafCount: Int,

    val children: List<AnalyzedNode>

)