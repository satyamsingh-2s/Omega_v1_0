package com.example.omega_v1_0.data_layer.imports

import kotlinx.serialization.Serializable

@Serializable
data class ImportNode(

    val title: String,

    val estimatedHours: Int = 0,

    val children: List<ImportNode> = emptyList()

)