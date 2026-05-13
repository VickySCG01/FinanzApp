package com.equipo.finanzapp

import android.app.Application
import com.equipo.finanzapp.data.local.AppDatabase
import com.equipo.finanzapp.data.repository.MainRepository

class FinanzApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy {
        MainRepository(
            database.clienteDao(),
            database.asesorDao(),
            database.reunionDao(),
            database.avisoDao(),
            database.categoriaDao(),
            database.transaccionDao(),
            database.metaAhorroDao()
        )
    }
}
