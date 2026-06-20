package com.example.omega_v1_0.ui.utils
import com.example.omega_v1_0.data_layer.imports.OmegaImport
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


object OmegaJsonParser {

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true

    }

    fun decode(jsonString: String
    ):
            Result<OmegaImport>
    {

        return try {

            Result.success(

                OmegaJsonParser.json

                    .decodeFromString<OmegaImport>(

                        jsonString
                    )
            )

        } catch (

            exception: Exception

        ) {

            Result.failure(

                exception
            )
        }
    }

}