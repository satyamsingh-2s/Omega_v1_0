package com.example.omega_v1_0.omega_engines.layout_engine.model

data class LayoutTreeNode(

    val nodeId: Long,

    val children: List<LayoutTreeNode>

)