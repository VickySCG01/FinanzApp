package com.equipo.finanzapp.ui.screens.home.AsesorPerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.AsesorEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class AsesorPerfilViewModel(private val repository: MainRepository) : ViewModel() {

    var guardadoExitoso by mutableStateOf(false)
        private set

    fun guardarAsesor(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        especialidad: String,
        anioIngreso: String
    ) {
        if (nombre.isBlank() || apellido.isBlank()) return
        viewModelScope.launch {
            repository.insertAsesor(
                AsesorEntity(
                    nombre = nombre.trim(),
                    apellido = apellido.trim(),
                    email = email.trim(),
                    telefono = telefono.trim(),
                    especialidad = especialidad.trim(),
                    anioIngreso = anioIngreso.toIntOrNull() ?: 0
                )
            )
            guardadoExitoso = true
        }
    }

    fun resetGuardado() {
        guardadoExitoso = false
    }
}