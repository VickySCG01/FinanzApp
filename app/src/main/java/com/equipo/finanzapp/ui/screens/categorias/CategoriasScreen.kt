package com.equipo.finanzapp.ui.screens.categorias

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import com.equipo.finanzapp.data.local.CategoriaEntity
import com.equipo.finanzapp.ui.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(onNavigateBack: () -> Unit) {
    val application = LocalContext.current.applicationContext as FinanzApplication
    val viewModel: CategoriaViewModel = viewModel(
        factory = AppViewModelFactory(application.repository)
    )
    val uiState by viewModel.categoriasUiState.collectAsState()
    
    var showAddCategoryDialog by remember { mutableStateOf(false) }
    var selectedCategoryForExpense by remember { mutableStateOf<CategoriaEntity?>(null) }

    val sugerencias = listOf("Libros", "Transporte", "Cafetería", "Copias", "Ocio")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Categorías y Presupuesto", fontWeight = FontWeight.Bold) },
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
                onClick = { showAddCategoryDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Nueva Categoría")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            SummaryHeader(uiState)

            Text(
                "Sugerencias de gasto estudiantil",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sugerencias) { sugerencia ->
                    SuggestionChip(sugerencia) {
                        viewModel.agregarCategoria(sugerencia, 500.0)
                    }
                }
            }

            if (uiState.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No tienes categorías configuradas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState) { state ->
                        CategoriaProgressCard(
                            state = state,
                            onDelete = { viewModel.eliminarCategoria(state.categoria) },
                            onAddExpense = { selectedCategoryForExpense = state.categoria }
                        )
                    }
                }
            }
        }

        if (showAddCategoryDialog) {
            AddCategoriaDialog(
                onDismiss = { showAddCategoryDialog = false },
                onConfirm = { nombre, presupuesto ->
                    viewModel.agregarCategoria(nombre, presupuesto.toDoubleOrNull() ?: 0.0)
                    showAddCategoryDialog = false
                }
            )
        }

        if (selectedCategoryForExpense != null) {
            AddExpenseDialog(
                categoria = selectedCategoryForExpense!!,
                onDismiss = { selectedCategoryForExpense = null },
                onConfirm = { monto, desc ->
                    viewModel.agregarGasto(selectedCategoryForExpense!!.id, monto.toDoubleOrNull() ?: 0.0, desc)
                    selectedCategoryForExpense = null
                }
            )
        }
    }
}

@Composable
fun SuggestionChip(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SummaryHeader(uiState: List<CategoriaUiState>) {
    val totalPresupuesto = uiState.sumOf { it.categoria.presupuesto }
    val totalGasto = uiState.sumOf { it.gastoActual }
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("PRESUPUESTO TOTAL", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("$ ${String.format("%.2f", totalPresupuesto)}", color = MaterialTheme.colorScheme.primary, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { if (totalPresupuesto > 0) (totalGasto / totalPresupuesto).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(10.dp).clip(RoundedCornerShape(5.dp)),
                color = if (totalGasto > totalPresupuesto) Color.Red else MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Usado: $ ${String.format("%.2f", totalGasto)}", color = Color.Gray, fontSize = 12.sp)
                val disponible = (totalPresupuesto - totalGasto).coerceAtLeast(0.0)
                Text("Disponible: $ ${String.format("%.2f", disponible)}", color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CategoriaProgressCard(state: CategoriaUiState, onDelete: () -> Unit, onAddExpense: () -> Unit) {
    val progress = if (state.categoria.presupuesto > 0) (state.gastoActual / state.categoria.presupuesto).toFloat() else 0f
    val color = when {
        progress >= 1.0f -> Color.Red
        progress >= 0.8f -> Color(0xFFFFA000)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onAddExpense() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(40.dp).clip(CircleShape).background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Label, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(state.categoria.nombre, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                    Text("Límite: $${state.categoria.presupuesto}", color = Color.Gray, fontSize = 12.sp)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Gastado: $ ${String.format("%.2f", state.gastoActual)}", fontSize = 12.sp, color = Color.Gray)
                Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress.coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun AddCategoriaDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Categoría", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = presupuesto,
                    onValueChange = { presupuesto = it },
                    label = { Text("Presupuesto Mensual") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") },
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, presupuesto) }) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun AddExpenseDialog(categoria: CategoriaEntity, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Gasto en ${categoria.nombre}", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") },
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Nota (ej. Fotocopias)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(monto, descripcion) }) {
                Text("Registrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
