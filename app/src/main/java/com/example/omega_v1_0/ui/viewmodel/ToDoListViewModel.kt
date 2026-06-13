package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.ui.model.ToDoListUiModel
import com.example.omega_v1_0.ui.uistate.ToDoListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ToDoListUiState()
        )

    val uiState =
        _uiState.asStateFlow()

    init {
        observeToDoItems()
    }

    private fun observeToDoItems() {

        viewModelScope.launch {

            repository
                .getAllToDoItems()
                .collect { items ->

                    _uiState.update {

                        it.copy(
                            items = items
                        )
                    }
                }
        }
    }

    fun onNewItemTextChanged(
        value: String
    ) {

        _uiState.update {

            it.copy(
                newItemText = value
            )
        }
    }

    fun addItem() {

        val text =
            _uiState.value.newItemText.trim()

        if (text.isBlank()) {
            return
        }

        viewModelScope.launch {

            repository.addToDoItem(
                text = text
            )

            _uiState.update {

                it.copy(
                    newItemText = ""
                )
            }
        }
    }

    fun toggleCompleted(
        item: ToDoListUiModel
    ) {

        viewModelScope.launch {

            repository.updateToDoCompleted(
                itemId = item.id,
                isCompleted =
                    !item.isCompleted
            )
        }
    }

    fun deleteItem(
        itemId: Long
    ) {

        viewModelScope.launch {

            repository.deleteToDoItem(
                itemId
            )
        }
    }
}