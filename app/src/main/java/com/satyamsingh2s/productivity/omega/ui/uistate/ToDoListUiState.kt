package com.satyamsingh2s.productivity.omega.ui.uistate

import com.satyamsingh2s.productivity.omega.ui.model.ToDoListUiModel

data class ToDoListUiState(

    val items: List<ToDoListUiModel> =
        emptyList(),

    val newItemText: String = "",

)