package com.equipo.finanzapp.ui.screens.cuentas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.TransaccionEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransaccionViewModel(private val repository: MainRepository) : ViewModel() {

    val transacciones: StateFlow<List<TransaccionEntity>> = repository.allTransacciones.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val saldoTotal: StateFlow<Double> = repository.allTransacciones.map { lista ->
        lista.sumOf { if (it.tipo == "INGRESO") it.monto else -it.monto }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    fun agregarIngreso(monto: Double, descripcion: String) {
        if (monto <= 0) return
        viewModelScope.launch {
            repository.insertTransaccion(
                TransaccionEntity(
                    categoriaId = null,
                    monto = monto,
                    descripcion = descripcion,
                    fecha = System.currentTimeMillis(),
                    tipo = "INGRESO"
                )
            )
        }
    }

    fun eliminarTransaccion(transaccion: TransaccionEntity) {
        viewModelScope.launch {
            repository.deleteTransaccion(transaccion)
        }
    }
}
