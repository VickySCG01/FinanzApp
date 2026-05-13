package com.equipo.finanzapp.ui.screens.categorias

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.finanzapp.data.local.CategoriaEntity
import com.equipo.finanzapp.data.local.TransaccionEntity
import com.equipo.finanzapp.data.repository.MainRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CategoriaUiState(
    val categoria: CategoriaEntity,
    val gastoActual: Double = 0.0
)

class CategoriaViewModel(private val repository: MainRepository) : ViewModel() {

    val categoriasUiState: StateFlow<List<CategoriaUiState>> = repository.allCategorias
        .flatMapLatest { categorias ->
            if (categorias.isEmpty()) return@flatMapLatest flowOf(emptyList<CategoriaUiState>())
            
            val flows = categorias.map { cat ->
                repository.getGastoTotalPorCategoria(cat.id).map { gasto ->
                    CategoriaUiState(cat, gasto ?: 0.0)
                }
            }
            combine(flows) { it.toList() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarCategoria(nombre: String, presupuesto: Double) {
        if (nombre.isBlank()) return
        viewModelScope.launch {
            repository.insertCategoria(
                CategoriaEntity(
                    nombre = nombre.trim(),
                    icono = "default",
                    colorHex = "#004481",
                    presupuesto = presupuesto
                )
            )
        }
    }

    fun agregarGasto(categoriaId: Int, monto: Double, descripcion: String) {
        if (monto <= 0) return
        viewModelScope.launch {
            repository.insertTransaccion(
                TransaccionEntity(
                    categoriaId = categoriaId,
                    monto = monto,
                    descripcion = descripcion,
                    fecha = System.currentTimeMillis(),
                    tipo = "EGRESO"
                )
            )
        }
    }

    fun eliminarCategoria(categoria: CategoriaEntity) {
        viewModelScope.launch {
            repository.deleteCategoria(categoria)
        }
    }
}
