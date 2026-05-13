package com.equipo.finanzapp.data.repository

import com.equipo.finanzapp.data.local.*
import kotlinx.coroutines.flow.Flow

class MainRepository(
    private val clienteDao: ClienteDao,
    private val asesorDao: AsesorDao,
    private val reunionDao: ReunionDao,
    private val avisoDao: AvisoDao,
    private val categoriaDao: CategoriaDao,
    private val transaccionDao: TransaccionDao
) {

    val allClientes: Flow<List<ClienteEntity>> = clienteDao.getAllClientes()
    val allAsesores: Flow<List<AsesorEntity>> = asesorDao.getAllAsesores()
    val allReuniones: Flow<List<ReunionEntity>> = reunionDao.getAllReuniones()
    val allAvisos: Flow<List<AvisoEntity>> = avisoDao.getAllAvisos()
    val allCategorias: Flow<List<CategoriaEntity>> = categoriaDao.getAllCategorias()
    val allTransacciones: Flow<List<TransaccionEntity>> = transaccionDao.getAllTransacciones()

    fun getClienteStream(id: Int): Flow<ClienteEntity?> = clienteDao.getClienteById(id)
    fun getAsesorStream(id: Int): Flow<AsesorEntity?> = asesorDao.getAsesorById(id)
    fun getReunionesByAsesor(asesorId: Int): Flow<List<ReunionEntity>> = reunionDao.getReunionesByAsesor(asesorId)
    fun getAvisosByAsesor(asesorId: Int): Flow<List<AvisoEntity>> = avisoDao.getAvisosByAsesor(asesorId)
    
    fun getTransaccionesByCategoria(catId: Int): Flow<List<TransaccionEntity>> = transaccionDao.getTransaccionesByCategoria(catId)
    fun getGastoTotalPorCategoria(catId: Int): Flow<Double?> = transaccionDao.getGastoTotalPorCategoria(catId)

    suspend fun insertCliente(cliente: ClienteEntity) = clienteDao.insert(cliente)
    suspend fun updateCliente(cliente: ClienteEntity) = clienteDao.update(cliente)
    suspend fun deleteCliente(cliente: ClienteEntity) = clienteDao.delete(cliente)

    suspend fun insertAsesor(asesor: AsesorEntity) = asesorDao.insert(asesor)
    suspend fun updateAsesor(asesor: AsesorEntity) = asesorDao.update(asesor)
    suspend fun deleteAsesor(asesor: AsesorEntity) = asesorDao.delete(asesor)

    suspend fun insertReunion(reunion: ReunionEntity) = reunionDao.insert(reunion)
    suspend fun updateReunion(reunion: ReunionEntity) = reunionDao.update(reunion)
    suspend fun deleteReunion(reunion: ReunionEntity) = reunionDao.delete(reunion)

    suspend fun insertAviso(aviso: AvisoEntity) = avisoDao.insert(aviso)
    suspend fun deleteAviso(aviso: AvisoEntity) = avisoDao.delete(aviso)

    suspend fun insertCategoria(categoria: CategoriaEntity) = categoriaDao.insert(categoria)
    suspend fun updateCategoria(categoria: CategoriaEntity) = categoriaDao.update(categoria)
    suspend fun deleteCategoria(categoria: CategoriaEntity) = categoriaDao.delete(categoria)

    suspend fun insertTransaccion(transaccion: TransaccionEntity) = transaccionDao.insert(transaccion)
    suspend fun deleteTransaccion(transaccion: TransaccionEntity) = transaccionDao.delete(transaccion)
}
