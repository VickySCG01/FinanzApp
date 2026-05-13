package com.equipo.finanzapp.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthUiState {
    object Idle : AuthUiState()
    object Loading : AuthUiState()
    data class Success(val email: String) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

class LoginViewModel(private val repository: MainRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        // Crear cuenta base si no existe
        viewModelScope.launch {
            val existing = repository.getClienteByEmail("admin@gmail.com")
            if (existing == null) {
                repository.insertCliente(
                    ClienteEntity(
                        nombre = "Admin",
                        apellido = "FinanzApp",
                        email = "admin@gmail.com",
                        telefono = "0000000000",
                        rfc = "FinanzApp University",
                        password = "12345"
                    )
                )
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState.Error("Por favor llena todos los campos")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState.Error("Correo electrónico no válido")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val user = repository.login(email, password)
            if (user != null) {
                _uiState.value = AuthUiState.Success(email)
            } else {
                _uiState.value = AuthUiState.Error("Correo o contraseña incorrectos")
            }
        }
    }

    fun registrar(nombre: String, email: String, password: String, confirmPassword: String) {
        if (nombre.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _uiState.value = AuthUiState.Error("Todos los campos son obligatorios")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = AuthUiState.Error("Correo electrónico no válido")
            return
        }

        if (password.length < 5) {
            _uiState.value = AuthUiState.Error("La contraseña debe tener al menos 5 caracteres")
            return
        }

        if (password != confirmPassword) {
            _uiState.value = AuthUiState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val existing = repository.getClienteByEmail(email)
            if (existing != null) {
                _uiState.value = AuthUiState.Error("El correo ya está registrado")
            } else {
                repository.insertCliente(
                    ClienteEntity(
                        nombre = nombre,
                        apellido = "",
                        email = email,
                        telefono = "",
                        rfc = "Estudiante",
                        password = password
                    )
                )
                _uiState.value = AuthUiState.Success(email)
            }
        }
    }
    
    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}
