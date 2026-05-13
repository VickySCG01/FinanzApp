package com.equipo.finanzapp.ui.screens.home.ClientePerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ClientePerfilViewModel(private val repository: MainRepository) : ViewModel() {

    val perfil: StateFlow<ClienteEntity?> = repository.allClientes.map { 
        it.firstOrNull() 
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    var guardadoExitoso by mutableStateOf(false)
        private set

    fun guardarPerfil(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        universidad: String // Usaremos el campo RFC para guardar la universidad en este contexto de ejemplo
    ) {
        if (nombre.isBlank()) return
        
        viewModelScope.launch {
            val current = perfil.value
            val nuevoPerfil = if (current != null) {
                current.copy(
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    telefono = telefono,
                    rfc = universidad
                )
            } else {
                ClienteEntity(
                    nombre = nombre,
                    apellido = apellido,
                    email = email,
                    telefono = telefono,
                    rfc = universidad,
                    saldo = 0.0
                )
            }
            
            repository.insertCliente(nuevoPerfil)
            guardadoExitoso = true
        }
    }

    fun resetGuardado() {
        guardadoExitoso = false
    }
}
