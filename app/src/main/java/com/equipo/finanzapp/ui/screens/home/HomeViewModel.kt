package com.equipo.finanzapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.local.SessionManager
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
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

    val balance: StateFlow<Double> = repository.allTransacciones.map { lista ->
        lista.sumOf { if (it.tipo == "INGRESO") it.monto else -it.monto }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val ingresosTotales: StateFlow<Double> = repository.allTransacciones.map { lista ->
        lista.filter { it.tipo == "INGRESO" }.sumOf { it.monto }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    val gastosTotales: StateFlow<Double> = repository.allTransacciones.map { lista ->
        lista.filter { it.tipo == "EGRESO" }.sumOf { it.monto }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )
}
