package com.equipo.finanzapp.ui.screens.home.AsesorAcciones

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ReunionEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AsesorAccionesViewModel(private val repository: MainRepository) : ViewModel() {

    // El estudiante visualiza sus propias reuniones y avisos recibidos
    val reuniones = repository.allReuniones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val avisos = repository.allAvisos.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    var reunionGuardada by mutableStateOf(false)
        private set

    fun guardarReunion(fecha: String, hora: String, motivo: String, notas: String) {
        if (fecha.isBlank() || motivo.isBlank()) return
        viewModelScope.launch {
            repository.insertReunion(
                ReunionEntity(
                    clienteId = 1, // ID del estudiante logueado (simulado)
                    asesorId = 101, // ID del asesor asignado (simulado)
                    fecha = fecha.trim(),
                    hora = hora.trim(),
                    motivo = motivo.trim(),
                    notas = notas.trim()
                )
            )
            reunionGuardada = true
        }
    }

    fun resetReunion() { reunionGuardada = false }
}
