package com.equipo.finanzapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {
    @Query("SELECT * FROM clientes")
    fun getAllClientes(): Flow<List<ClienteEntity>>
    
    @Query("SELECT * FROM clientes WHERE id = :id")
    fun getClienteById(id: Int): Flow<ClienteEntity>
    
    @Query("SELECT * FROM clientes WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE email = :email LIMIT 1")
    suspend fun getClienteByEmail(email: String): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE email = :email LIMIT 1")
    fun getClienteByEmailFlow(email: String): Flow<ClienteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cliente: ClienteEntity)
    
    @Update
    suspend fun update(cliente: ClienteEntity)
    
    @Delete
    suspend fun delete(cliente: ClienteEntity)
}

@Dao
interface MetaAhorroDao {
    @Query("SELECT * FROM metas_ahorro")
    fun getAllMetas(): Flow<List<MetaAhorroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meta: MetaAhorroEntity)

    @Update
    suspend fun update(meta: MetaAhorroEntity)

    @Delete
    suspend fun delete(meta: MetaAhorroEntity)
}

@Dao
interface AsesorDao {
    @Query("SELECT * FROM asesores")
    fun getAllAsesores(): Flow<List<AsesorEntity>>
    @Query("SELECT * FROM asesores WHERE id = :id")
    fun getAsesorById(id: Int): Flow<AsesorEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asesor: AsesorEntity)
    @Update
    suspend fun update(asesor: AsesorEntity)
    @Delete
    suspend fun delete(asesor: AsesorEntity)
}

@Dao
interface ReunionDao {
    @Query("SELECT * FROM reuniones")
    fun getAllReuniones(): Flow<List<ReunionEntity>>
    @Query("SELECT * FROM reuniones WHERE asesorId = :asesorId")
    fun getReunionesByAsesor(asesorId: Int): Flow<List<ReunionEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reunion: ReunionEntity)
    @Update
    suspend fun update(reunion: ReunionEntity)
    @Delete
    suspend fun delete(reunion: ReunionEntity)
}

@Dao
interface AvisoDao {
    @Query("SELECT * FROM avisos")
    fun getAllAvisos(): Flow<List<AvisoEntity>>
    @Query("SELECT * FROM avisos WHERE asesorId = :asesorId")
    fun getAvisosByAsesor(asesorId: Int): Flow<List<AvisoEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(aviso: AvisoEntity)
    @Delete
    suspend fun delete(aviso: AvisoEntity)
}

@Dao
interface CategoriaDao {
    @Query("SELECT * FROM categorias")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(categoria: CategoriaEntity)
    @Update
    suspend fun update(categoria: CategoriaEntity)
    @Delete
    suspend fun delete(categoria: CategoriaEntity)
}

@Dao
interface TransaccionDao {
    @Query("SELECT * FROM transacciones ORDER BY fecha DESC")
    fun getAllTransacciones(): Flow<List<TransaccionEntity>>
    
    @Query("SELECT * FROM transacciones WHERE categoriaId = :catId ORDER BY fecha DESC")
    fun getTransaccionesByCategoria(catId: Int): Flow<List<TransaccionEntity>>

    @Query("SELECT COALESCE(SUM(monto), 0.0) FROM transacciones WHERE categoriaId = :catId AND tipo = 'EGRESO'")
    fun getGastoTotalPorCategoria(catId: Int): Flow<Double>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaccion: TransaccionEntity)

    @Delete
    suspend fun delete(transaccion: TransaccionEntity)
}
