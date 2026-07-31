package com.example.omega_v1_0.omega_engines.layout_engine.model

import android.graphics.Point

data class LayoutNode(

    val nodeId: Long,
    val parentNodeId: Long?,

    val center: Point,

    val depth: Int

)