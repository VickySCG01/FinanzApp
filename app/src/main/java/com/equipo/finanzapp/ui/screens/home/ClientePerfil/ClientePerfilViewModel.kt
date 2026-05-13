package com.equipo.finanzapp.ui.screens.home.ClientePerfil

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class ClientePerfilViewModel(private val repository: MainRepository) : ViewModel() {

    var guardadoExitoso by mutableStateOf(false)
        private set

    fun guardarCliente(
        nombre: String,
        apellido: String,
        email: String,
        telefono: String,
        rfc: String,
        saldo: String
    ) {
        if (nombre.isBlank() || apellido.isBlank()) return
        viewModelScope.launch {
            repository.insertCliente(
                ClienteEntity(
                    nombre = nombre.trim(),
                    apellido = apellido.trim(),
                    email = email.trim(),
                    telefono = telefono.trim(),
                    rfc = rfc.trim().uppercase(),
                    saldo = saldo.toDoubleOrNull() ?: 0.0
                )
            )
            guardadoExitoso = true
        }
    }

    fun resetGuardado() {
        guardadoExitoso = false
    }
}