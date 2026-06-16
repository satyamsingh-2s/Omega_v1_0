package com.example.omega_v1_0.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import com.example.omega_v1_0.models.TodoCategory
import com.example.omega_v1_0.ui.model.ToDoListUiModel
import com.example.omega_v1_0.ui.uistate.ToDoListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ToDoListViewModel(
    private val repository: Omega_Repository
) : ViewModel() {

    private val _todoCategory =
        MutableStateFlow(
            TodoCategory.TODAY
        )
    val todoCategory =
        _todoCategory.asStateFlow()

    private val _uiState =
        MutableStateFlow(
            ToDoListUiState()
        )
// -- for limitin notes on today and future
    private val _showMaxLimitToast =
        MutableStateFlow<String?>(null)
    val showMaxLimitToast =
        _showMaxLimitToast.asStateFlow()

    val uiState =
        _uiState.asStateFlow()
    init {
        observeToDoItems()
    }
    private fun observeToDoItems() {
        viewModelScope.launch {
            _todoCategory
                .flatMapLatest {
                    repository.getAllToDoItems(it)
                }

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
        // for limitnng nuber of tasks
        if (
            _uiState.value.items.size >= getMaxLimit()
        ) {
            _showMaxLimitToast.value =
                if (
                    _todoCategory.value == TodoCategory.TODAY
                ) {
                    "Maximum 5 tasks allowed"
                } else {
                    "Maximum 7 tasks allowed" }
            return
        }

        viewModelScope.launch {
            repository.addToDoItem(
                text = text,
                category = _todoCategory.value
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

    fun changeCategory(
        category: TodoCategory
    ) {

        Log.d("TODO❌❌❌❌", "Changed to $category")

        _todoCategory.value = category
    }

    private fun getMaxLimit(): Int {
        return when (_todoCategory.value) {
            TodoCategory.TODAY -> 5
            TodoCategory.FUTURE -> 7
        }
    }

    fun onToastShown() {

        _showMaxLimitToast.value =
            null
    }
}