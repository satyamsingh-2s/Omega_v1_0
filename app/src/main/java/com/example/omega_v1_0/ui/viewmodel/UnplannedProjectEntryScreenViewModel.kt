package com.example.omega_v1_0.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.omega_v1_0.data_layer.imports.OmegaImport
import com.example.omega_v1_0.data_layer.omega_repository.Omega_Repository
import kotlinx.coroutines.launch

class UnplannedProjectEntryScreenViewModel(

    private val repository: Omega_Repository

) : ViewModel() {


     fun importWorkspace(
        omegaImport: OmegaImport
    ) {
        viewModelScope.launch {
            repository.importStructure(omegaImport)
        }
    }

}