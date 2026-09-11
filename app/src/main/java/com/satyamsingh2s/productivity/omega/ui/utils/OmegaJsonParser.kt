package com.satyamsingh2s.productivity.omega.ui.utils
import com.satyamsingh2s.productivity.omega.data_layer.imports.OmegaImport
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