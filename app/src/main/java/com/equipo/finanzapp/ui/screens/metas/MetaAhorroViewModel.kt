package com.equipo.finanzapp.ui.screens.metas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.MetaAhorroEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MetaAhorroViewModel(private val repository: MainRepository) : ViewModel() {

    val metas: StateFlow<List<MetaAhorroEntity>> = repository.allMetas.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun agregarMeta(nombre: String, objetivo: Double, fechaLimite: String) {
        if (nombre.isBlank() || objetivo <= 0) return
        viewModelScope.launch {
            repository.insertMeta(
                MetaAhorroEntity(
                    nombre = nombre,
                    montoObjetivo = objetivo,
                    montoActual = 0.0,
                    fechaLimite = fechaLimite
                )
            )
        }
    }

    fun abonarAMeta(meta: MetaAhorroEntity, monto: Double) {
        if (monto <= 0) return
        viewModelScope.launch {
            repository.updateMeta(meta.copy(montoActual = meta.montoActual + monto))
        }
    }

    fun eliminarMeta(meta: MetaAhorroEntity) {
        viewModelScope.launch {
            repository.deleteMeta(meta)
        }
    }
}
