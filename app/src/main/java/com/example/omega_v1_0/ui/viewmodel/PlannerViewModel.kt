package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.models_enums.PlannerPriority
import com.example.omega_v1_0.planner.repository.PlannerRepository
import com.example.omega_v1_0.planner.ui.PlannerNodeUiModel
import com.example.omega_v1_0.planner.ui.PlannerUiState
import com.example.omega_v1_0.ui.screens.PlannerBottomSheet.component.PlannerTaskContextMenuState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlannerViewModel(

    private val plannerRepository: PlannerRepository

) : ViewModel() {

    init {
        observePlanner()
    }

    private val _uiState = MutableStateFlow(PlannerUiState())
    val uiState: StateFlow<PlannerUiState> = _uiState.asStateFlow()

    private val _taskContextMenuState =
        MutableStateFlow(PlannerTaskContextMenuState())
    val taskContextMenuState = _taskContextMenuState.asStateFlow()

    private val _expandedBucket = MutableStateFlow(PlannerPriority.CRITICAL)
    val expandedBucket = _expandedBucket.asStateFlow()

    // --- for navigation to session screen --
    private val _openSessionNode =
        MutableStateFlow<PlannerNodeUiModel?>(null)
    val openSessionNode: StateFlow<PlannerNodeUiModel?> =
        _openSessionNode.asStateFlow()

    fun openSession(
        node: PlannerNodeUiModel
    ) {

        _openSessionNode.value = node
    }

    private fun observePlanner() {

        viewModelScope.launch {
            plannerRepository
                .observePlanner()
                .collect { plannerUiState ->

                    _uiState.value = plannerUiState
                }
        }
    }

    fun changePriority(
        nodeId: Long,
        newPriority: PlannerPriority
    ) {

        viewModelScope.launch {

            plannerRepository.changePriority(
                nodeId = nodeId,
                newPriority = newPriority
            )
        }
    }

    fun moveTaskToPriority(
        priority: PlannerPriority
    ) {

        val selectedNode =
            taskContextMenuState.value.selectedNode
                ?: return

        viewModelScope.launch {

            plannerRepository.changePriority(
                nodeId = selectedNode.nodeId,
                newPriority = priority
            )

            hideTaskMenu()
        }
    }

    fun addSelectedTaskToToday() {

        val selectedNode =
            taskContextMenuState.value.selectedNode
                ?: return

        viewModelScope.launch {

            plannerRepository.addToTodayQueue(
                nodeId = selectedNode.nodeId
            )

            hideTaskMenu()
        }
    }

    fun removeSelectedTaskFromPlanner() {

        val selectedNode =
            taskContextMenuState.value.selectedNode
                ?: return

        viewModelScope.launch {

            plannerRepository.removeNodeFromPlanner(
                nodeId = selectedNode.nodeId
            )

            hideTaskMenu()
        }
    }

    fun addToTodayQueue(
        nodeId: Long
    ) {

        viewModelScope.launch {

            plannerRepository.addToTodayQueue(
                nodeId = nodeId
            )
        }
    }

    fun removeFromTodayQueue(
        nodeId: Long
    ) {

        viewModelScope.launch {

            plannerRepository.removeFromTodayQueue(
                nodeId = nodeId
            )
        }
    }

    fun expandBucket(
        priority: PlannerPriority
    ) {
        _expandedBucket.value = priority

    }

    fun showTaskMenu(
        node: PlannerNodeUiModel
    ) {

        _taskContextMenuState.value =
            PlannerTaskContextMenuState(
                isVisible = true,
                selectedNode = node
            )
    }

    fun hideTaskMenu() {

        _taskContextMenuState.value =
            PlannerTaskContextMenuState()
    }

    // -- make the value false
    fun onSessionNavigationComplete() {
        _openSessionNode.value = null
    }


}