package com.satyamsingh2s.productivity.omega.data_layer.imports

import kotlinx.serialization.Serializable

@Serializable
data class ImportNode(

    val title: String,

    val estimatedHours: Int = 0,

    val children: List<ImportNode> = emptyList()

)