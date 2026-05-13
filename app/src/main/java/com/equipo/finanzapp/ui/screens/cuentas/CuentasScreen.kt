package com.equipo.finanzapp.ui.screens.cuentas

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
import com.equipo.finanzapp.data.local.TransaccionEntity
import com.equipo.finanzapp.ui.AppViewModelFactory
import com.equipo.finanzapp.ui.theme.BbvaLightBlue
import com.equipo.finanzapp.ui.theme.BbvaNavy
import com.equipo.finanzapp.ui.theme.BbvaWhite
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CuentasScreen(onNavigateBack: () -> Unit) {
    val application = LocalContext.current.applicationContext as FinanzApplication
    val viewModel: TransaccionViewModel = viewModel(
        factory = AppViewModelFactory(application.repository)
    )
    val transacciones by viewModel.transacciones.collectAsState()
    val saldoTotal by viewModel.saldoTotal.collectAsState()
    
    var showAddIncomeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Cuenta", fontWeight = FontWeight.Bold) },
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
                onClick = { showAddIncomeDialog = true },
                containerColor = BbvaNavy,
                contentColor = BbvaWhite,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Ingreso")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F4F4))
        ) {
            // Balance Card
            BalanceHeader(saldoTotal)

            Text(
                "Movimientos recientes",
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BbvaNavy
            )

            if (transacciones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay movimientos registrados", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transacciones) { transaccion ->
                        TransaccionItem(transaccion)
                    }
                }
            }
        }

        if (showAddIncomeDialog) {
            AddIncomeDialog(
                onDismiss = { showAddIncomeDialog = false },
                onConfirm = { monto, desc ->
                    viewModel.agregarIngreso(monto.toDoubleOrNull() ?: 0.0, desc)
                    showAddIncomeDialog = false
                }
            )
        }
    }
}

@Composable
fun BalanceHeader(saldo: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("SALDO TOTAL DISPONIBLE", color = Color.Gray, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "$ ${String.format("%.2f", saldo)}",
                color = BbvaNavy,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF2E7D32))
                    Text("Ingresos", fontSize = 10.sp, color = Color.Gray)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.TrendingDown, contentDescription = null, tint = Color.Red)
                    Text("Gastos", fontSize = 10.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun TransaccionItem(transaccion: TransaccionEntity) {
    val isIngreso = transaccion.tipo == "INGRESO"
    val color = if (isIngreso) Color(0xFF2E7D32) else BbvaNavy
    val icon = if (isIngreso) Icons.Default.ArrowCircleUp else Icons.Default.ArrowCircleDown
    val dateFormat = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = if (isIngreso) Color(0xFF2E7D32) else Color.Red.copy(alpha = 0.7f),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaccion.descripcion.ifBlank { if (isIngreso) "Ingreso" else "Gasto" },
                    fontWeight = FontWeight.Bold,
                    color = BbvaNavy,
                    fontSize = 14.sp
                )
                Text(
                    text = dateFormat.format(Date(transaccion.fecha)),
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
            Text(
                text = "${if (isIngreso) "+" else "-"} $ ${String.format("%.2f", transaccion.monto)}",
                fontWeight = FontWeight.Bold,
                color = if (isIngreso) Color(0xFF2E7D32) else Color.Black,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun AddIncomeDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var monto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Ingreso", color = BbvaNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto") },
                    modifier = Modifier.fillMaxWidth(),
                    prefix = { Text("$") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Concepto (ej. Depósito, Beca)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(monto, descripcion) }, colors = ButtonDefaults.buttonColors(containerColor = BbvaNavy)) {
                Text("Cargar saldo")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BbvaNavy) }
        }
    )
}
