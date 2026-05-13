package com.equipo.finanzapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clientes")
data class ClienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val rfc: String, // Usado como Universidad en el contexto estudiantil
    val password: String = "12345", // Password por defecto para simplificar
    val saldo: Double = 0.0,
    val fotoPerfil: String? = null
)

@Entity(tableName = "asesores")
data class AsesorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val email: String,
    val telefono: String,
    val especialidad: String,
    val anioIngreso: Int
)

@Entity(tableName = "reuniones")
data class ReunionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clienteId: Int,
    val asesorId: Int,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val notas: String = ""
)

@Entity(tableName = "avisos")
data class AvisoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val mensaje: String,
    val fecha: String,
    val asesorId: Int
)

@Entity(tableName = "categorias")
data class CategoriaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val icono: String,
    val colorHex: String,
    val presupuesto: Double = 0.0
)

@Entity(tableName = "transacciones")
data class TransaccionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val categoriaId: Int?, // Opcional, puede no tener categoría
    val monto: Double,
    val descripcion: String,
    val fecha: Long, // Timestamp
    val tipo: String // "INGRESO" o "EGRESO"
)

@Entity(tableName = "metas_ahorro")
data class MetaAhorroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val montoObjetivo: Double,
    val montoActual: Double = 0.0,
    val fechaLimite: String,
    val icono: String = "flag"
)
