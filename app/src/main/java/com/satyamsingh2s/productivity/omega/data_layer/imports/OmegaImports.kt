package com.satyamsingh2s.productivity.omega.data_layer.imports

import kotlinx.serialization.Serializable

@Serializable
data class OmegaImport(

    val project: String,
    val structure: List<ImportNode>

)