package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.model

data class LayoutTreeNode(

    val nodeId: Long,

    val children: List<LayoutTreeNode>

)