package com.equipo.finanzapp.data.repository

import com.equipo.finanzapp.data.local.*
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val clienteDao: ClienteDao,
    private val asesorDao: AsesorDao,
    private val reunionDao: ReunionDao,
    private val avisoDao: AvisoDao,
    private val categoriaDao: CategoriaDao,
    private val transaccionDao: TransaccionDao,
    private val metaAhorroDao: MetaAhorroDao
) {

    val allClientes: Flow<List<ClienteEntity>> = clienteDao.getAllClientes()
    val allAsesores: Flow<List<AsesorEntity>> = asesorDao.getAllAsesores()
    val allReuniones: Flow<List<ReunionEntity>> = reunionDao.getAllReuniones()
    val allAvisos: Flow<List<AvisoEntity>> = avisoDao.getAllAvisos()
    val allCategorias: Flow<List<CategoriaEntity>> = categoriaDao.getAllCategorias()
    val allTransacciones: Flow<List<TransaccionEntity>> = transaccionDao.getAllTransacciones()
    val allMetas: Flow<List<MetaAhorroEntity>> = metaAhorroDao.getAllMetas()

    suspend fun login(email: String, password: String): ClienteEntity? = clienteDao.login(email, password)
    suspend fun getClienteByEmail(email: String): ClienteEntity? = clienteDao.getClienteByEmail(email)

    suspend fun insertCliente(cliente: ClienteEntity) = clienteDao.insert(cliente)
    suspend fun updateCliente(cliente: ClienteEntity) = clienteDao.update(cliente)
    suspend fun deleteCliente(cliente: ClienteEntity) = clienteDao.delete(cliente)

    suspend fun insertAsesor(asesor: AsesorEntity) = asesorDao.insert(asesor)
    suspend fun insertReunion(reunion: ReunionEntity) = reunionDao.insert(reunion)
    suspend fun insertCategoria(categoria: CategoriaEntity) = categoriaDao.insert(categoria)
    suspend fun deleteCategoria(categoria: CategoriaEntity) = categoriaDao.delete(categoria)

    suspend fun insertTransaccion(transaccion: TransaccionEntity) = transaccionDao.insert(transaccion)
    suspend fun deleteTransaccion(transaccion: TransaccionEntity) = transaccionDao.delete(transaccion)
    
    fun getGastoTotalPorCategoria(catId: Int): Flow<Double> = transaccionDao.getGastoTotalPorCategoria(catId)

    suspend fun insertMeta(meta: MetaAhorroEntity) = metaAhorroDao.insert(meta)
    suspend fun updateMeta(meta: MetaAhorroEntity) = metaAhorroDao.update(meta)
    suspend fun deleteMeta(meta: MetaAhorroEntity) = metaAhorroDao.delete(meta)
}
