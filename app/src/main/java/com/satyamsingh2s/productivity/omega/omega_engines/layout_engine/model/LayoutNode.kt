package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.model

import android.graphics.Point

data class LayoutNode(

    val nodeId: Long,
    val parentNodeId: Long?,

    val center: Point,

    val depth: Int

)