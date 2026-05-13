package com.equipo.finanzapp.ui.screens.home.AsesorAcciones

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
import com.equipo.finanzapp.data.local.ReunionEntity
import com.equipo.finanzapp.ui.AppViewModelFactory
import com.equipo.finanzapp.ui.theme.BbvaLightBlue
import com.equipo.finanzapp.ui.theme.BbvaNavy
import com.equipo.finanzapp.ui.theme.BbvaWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsesorAccionesScreen(onNavigateBack: () -> Unit) {
    val application = LocalContext.current.applicationContext as FinanzApplication
    val viewModel: AsesorAccionesViewModel = viewModel(
        factory = AppViewModelFactory(application.repository)
    )

    val reuniones by viewModel.reuniones.collectAsState()
    var showBookingDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Asesoría Financiera", fontWeight = FontWeight.Bold) },
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
            ExtendedFloatingActionButton(
                onClick = { showBookingDialog = true },
                containerColor = BbvaNavy,
                contentColor = BbvaWhite,
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("Nueva Cita") },
                shape = RoundedCornerShape(24.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F4F4))
        ) {
            // Sección de Asesor Asignado
            AsesorAsignadoCard()

            Text(
                "Tus próximas citas",
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = BbvaNavy
            )

            if (reuniones.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    Text("No tienes citas programadas", color = Color.Gray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(reuniones) { reunion ->
                        ReunionItem(reunion)
                    }
                }
            }
        }

        if (showBookingDialog) {
            BookingDialog(
                onDismiss = { showBookingDialog = false },
                onConfirm = { fecha, motivo ->
                    viewModel.guardarReunion(fecha, "10:00 AM", motivo, "")
                    showBookingDialog = false
                }
            )
        }
    }
}

@Composable
fun AsesorAsignadoCard() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(BbvaLightBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.SupportAgent, tint = BbvaNavy, contentDescription = null)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Lic. Roberto Sánchez", fontWeight = FontWeight.Bold, color = BbvaNavy)
                Text("Asesor de Becas y Créditos", color = Color.Gray, fontSize = 12.sp)
                Text("En línea ahora", color = Color(0xFF2E7D32), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ReunionItem(reunion: ReunionEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(reunion.fecha.take(2), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BbvaNavy)
                Text("MAY", fontSize = 10.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reunion.motivo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("${reunion.hora} • Videollamada", color = Color.Gray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, tint = Color.LightGray, contentDescription = null)
        }
    }
}

@Composable
fun BookingDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var fecha by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Programar Asesoría", color = BbvaNavy, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = { fecha = it },
                    label = { Text("Fecha (ej. 24 de Mayo)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = motivo,
                    onValueChange = { motivo = it },
                    label = { Text("Motivo (Beca, Préstamo, etc.)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(fecha, motivo) }, colors = ButtonDefaults.buttonColors(containerColor = BbvaNavy)) {
                Text("Agendar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = BbvaNavy) }
        }
    )
}
