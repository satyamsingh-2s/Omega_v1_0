package com.example.omega_v1_0.ui.uistate

import com.example.omega_v1_0.ui.model.ToDoListUiModel

data class ToDoListUiState(

    val items: List<ToDoListUiModel> =
        emptyList(),

    val newItemText: String = "",

)