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
import androidx.compose.material.icons.filled.Category
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
import com.equipo.finanzapp.ui.theme.BbvaLightBlue
import com.equipo.finanzapp.ui.theme.BbvaNavy
import com.equipo.finanzapp.ui.theme.BbvaWhite

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
                title = { Text("Mis Presupuestos", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás", tint = BbvaWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BbvaNavy,
                    titleContentColor = BbvaWhite
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddCategoryDialog = true },
                containerColor = BbvaNavy,
                contentColor = BbvaWhite,
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
                .background(Color(0xFFF4F4F4))
        ) {
            SummaryHeader(uiState)

            Text(
                "Sugerencias para ti",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BbvaNavy
            )
            
            LazyRow(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sugerencias) { sugerencia ->
                    SuggestionChip(sugerencia) {
                        viewModel.agregarCategoria(sugerencia, 500.0) // Presupuesto default
                    }
                }
            }

            if (uiState.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No tienes presupuestos activos", color = Color.Gray)
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
        color = BbvaWhite,
        border = androidx.compose.foundation.BorderStroke(1.dp, BbvaNavy.copy(alpha = 0.2f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = BbvaNavy
        )
    }
}

@Composable
fun SummaryHeader(uiState: List<CategoriaUiState>) {
    val totalPresupuesto = uiState.sumOf { it.categoria.presupuesto }
    val totalGasto = uiState.sumOf { it.gastoActual }
    
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("TU LÍMITE MENSUAL", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("$ ${String.format("%.2f", totalPresupuesto)}", color = BbvaNavy, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { if (totalPresupuesto > 0) (totalGasto / totalPresupuesto).toFloat() else 0f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = if (totalGasto > totalPresupuesto) Color.Red else BbvaLightBlue,
                trackColor = Color(0xFFE0E0E0)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Gastado: $ ${String.format("%.2f", totalGasto)}", color = Color.Gray, fontSize = 12.sp)
                Text("Disponible: $ ${String.format("%.2f", (totalPresupuesto - totalGasto).coerceAtLeast(0.0))}", color = BbvaNavy, fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
        else -> BbvaLightBlue
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onAddExpense() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(color.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Category, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(state.categoria.nombre, fontWeight = FontWeight.Bold, color = BbvaNavy, fontSize = 16.sp)
                    Text("Límite: $${state.categoria.presupuesto}", color = Color.Gray, fontSize = 12.sp)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.LightGray)
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
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = color,
                trackColor = Color(0xFFEEEEEE)
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
        title = { Text("Configurar Presupuesto", color = BbvaNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de categoría") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = presupuesto,
                    onValueChange = { presupuesto = it },
                    label = { Text("Monto máximo mensual") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(nombre, presupuesto) }, colors = ButtonDefaults.buttonColors(containerColor = BbvaNavy)) {
                Text("Crear")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BbvaNavy) }
        }
    )
}

@Composable
fun AddExpenseDialog(categoria: CategoriaEntity, onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Gasto: ${categoria.nombre}", color = BbvaNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto del gasto") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(monto, descripcion) }, colors = ButtonDefaults.buttonColors(containerColor = BbvaNavy)) {
                Text("Confirmar Gasto")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BbvaNavy) }
        }
    )
}
