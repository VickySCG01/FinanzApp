package com.equipo.finanzapp.ui.screens.metas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equipo.finanzapp.FinanzApplication
import com.equipo.finanzapp.data.local.MetaAhorroEntity
import com.equipo.finanzapp.ui.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetasAhorroScreen(onNavigateBack: () -> Unit) {
    val application = LocalContext.current.applicationContext as FinanzApplication
    val viewModel: MetaAhorroViewModel = viewModel(
        factory = AppViewModelFactory(application.repository, sessionManager = application.sessionManager)
    )
    val metas by viewModel.metas.collectAsState()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedMetaForAbono by remember { mutableStateOf<MetaAhorroEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Metas de Ahorro", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Meta")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (metas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Flag, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Aún no tienes metas de ahorro", color = Color.Gray)
                        Text("¡Crea una para comenzar!", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(metas) { meta ->
                        MetaAhorroItem(
                            meta = meta,
                            onAbonar = { selectedMetaForAbono = meta },
                            onDelete = { viewModel.eliminarMeta(meta) }
                        )
                    }
                }
            }
        }

        if (showAddDialog) {
            AddMetaDialog(
                onDismiss = { showAddDialog = false },
                onConfirm = { nombre, objetivo, fecha ->
                    viewModel.agregarMeta(nombre, objetivo.toDoubleOrNull() ?: 0.0, fecha)
                    showAddDialog = false
                }
            )
        }

        if (selectedMetaForAbono != null) {
            AbonoDialog(
                meta = selectedMetaForAbono!!,
                onDismiss = { selectedMetaForAbono = null },
                onConfirm = { monto ->
                    viewModel.abonarAMeta(selectedMetaForAbono!!, monto.toDoubleOrNull() ?: 0.0)
                    selectedMetaForAbono = null
                }
            )
        }
    }
}

@Composable
fun MetaAhorroItem(meta: MetaAhorroEntity, onAbonar: () -> Unit, onDelete: () -> Unit) {
    val progress = if (meta.montoObjetivo > 0) (meta.montoActual / meta.montoObjetivo).toFloat() else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Flag, tint = MaterialTheme.colorScheme.primary, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(meta.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Meta: $${String.format("%.2f", meta.montoObjetivo)} • Límite: ${meta.fechaLimite}", color = Color.Gray, fontSize = 12.sp)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Ahorrado: $${String.format("%.2f", meta.montoActual)}", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.primary)
                Text("${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onAbonar,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Abonar a la meta")
            }
        }
    }
}

@Composable
fun AddMetaDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Meta de Ahorro", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("¿Para qué estás ahorrando?") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = objetivo, onValueChange = { objetivo = it }, label = { Text("Monto objetivo") }, modifier = Modifier.fillMaxWidth(), prefix = { Text("$") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = fecha, onValueChange = { fecha = it }, label = { Text("Fecha límite (ej. Dic 2025)") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, objetivo, fecha) }) { Text("Crear") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun AbonoDialog(meta: MetaAhorroEntity, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var monto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Abonar a: ${meta.nombre}", fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(value = monto, onValueChange = { monto = it }, label = { Text("Monto a depositar") }, modifier = Modifier.fillMaxWidth(), prefix = { Text("$") })
        },
        confirmButton = {
            Button(onClick = { onConfirm(monto) }) { Text("Confirmar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
