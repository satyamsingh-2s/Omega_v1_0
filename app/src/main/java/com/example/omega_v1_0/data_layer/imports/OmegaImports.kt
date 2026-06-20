package com.example.omega_v1_0.data_layer.imports

import kotlinx.serialization.Serializable

@Serializable
data class OmegaImport(

    val project: String,
    val structure: List<ImportNode>

)