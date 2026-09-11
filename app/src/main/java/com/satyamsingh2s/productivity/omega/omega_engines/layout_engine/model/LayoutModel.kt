package com.satyamsingh2s.productivity.omega.omega_engines.layout_engine.model

import android.util.Size

data class LayoutModel(

    val nodes: List<LayoutNode>,

    val connections: List<LayoutConnection>,

    val contentSize: Size

)