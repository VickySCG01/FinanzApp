package com.equipo.finanzapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.equipo.finanzapp.data.repository.MainRepository
import com.equipo.finanzapp.ui.screens.home.AsesorAcciones.AsesorAccionesViewModel
import com.equipo.finanzapp.ui.screens.home.AsesorPerfil.AsesorPerfilViewModel
import com.equipo.finanzapp.ui.screens.home.ClientePerfil.ClientePerfilViewModel
import com.equipo.finanzapp.ui.screens.home.HomeViewModel
import com.equipo.finanzapp.ui.screens.categorias.CategoriaViewModel
import com.equipo.finanzapp.ui.screens.cuentas.TransaccionViewModel

class AppViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) ->
            HomeViewModel(repository) as T
        modelClass.isAssignableFrom(ClientePerfilViewModel::class.java) ->
            ClientePerfilViewModel(repository) as T
        modelClass.isAssignableFrom(AsesorPerfilViewModel::class.java) ->
            AsesorPerfilViewModel(repository) as T
        modelClass.isAssignableFrom(AsesorAccionesViewModel::class.java) ->
            AsesorAccionesViewModel(repository) as T
        modelClass.isAssignableFrom(CategoriaViewModel::class.java) ->
            CategoriaViewModel(repository) as T
        modelClass.isAssignableFrom(TransaccionViewModel::class.java) ->
            TransaccionViewModel(repository) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}
