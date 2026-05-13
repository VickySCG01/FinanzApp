package com.equipo.finanzapp.ui.screens.home.ClientePerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.local.SessionManager
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

class ClientePerfilViewModel(
    private val repository: MainRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val perfil: StateFlow<ClienteEntity?> = flowOf(sessionManager.fetchUserEmail())
        .flatMapLatest { email ->
            if (email != null) {
                repository.getClienteByEmailFlow(email)
            } else {
                flowOf(null)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun logout() {
        sessionManager.clearAuthToken()
    }
}
