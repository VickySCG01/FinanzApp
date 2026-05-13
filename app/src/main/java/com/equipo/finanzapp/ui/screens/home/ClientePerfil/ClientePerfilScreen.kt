package com.equipo.finanzapp.ui.screens.home.ClientePerfil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.equipo.finanzapp.ui.AppViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientePerfilScreen(
    onNavigateBack: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToMetas: () -> Unit
) {
    val application = LocalContext.current.applicationContext as FinanzApplication
    val viewModel: ClientePerfilViewModel = viewModel(
        factory = AppViewModelFactory(application.repository)
    )
    val perfil by viewModel.perfil.collectAsState()

    var nombre by remember(perfil) { mutableStateOf(perfil?.nombre ?: "") }
    var apellido by remember(perfil) { mutableStateOf(perfil?.apellido ?: "") }
    var email by remember(perfil) { mutableStateOf(perfil?.email ?: "") }
    var universidad by remember(perfil) { mutableStateOf(perfil?.rfc ?: "") }
    var telefono by remember(perfil) { mutableStateOf(perfil?.telefono ?: "") }

    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil Estudiantil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if (isEditing) {
                            viewModel.guardarPerfil(nombre, apellido, email, telefono, universidad)
                        }
                        isEditing = !isEditing
                    }) {
                        Text(if (isEditing) "GUARDAR" else "EDITAR", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(64.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (nombre.isEmpty()) "Usuario" else "$nombre $apellido",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = universidad.ifEmpty { "Estudiante" },
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                ProfileSectionTitle("DATOS PERSONALES")
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (isEditing) {
                            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(value = universidad, onValueChange = { universidad = it }, label = { Text("Universidad") }, modifier = Modifier.fillMaxWidth())
                        } else {
                            InfoRow("Nombre", nombre)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            InfoRow("Correo", email)
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            InfoRow("Universidad", universidad)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                ProfileSectionTitle("OPCIONES")
                ProfileMenuOption("Seguridad y Acceso", Icons.Default.Security, onNavigateToSecurity)
                ProfileMenuOption("Mis Metas de Ahorro", Icons.Default.Flag, onNavigateToMetas)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedButton(
                    onClick = { /* Implementar Logout */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFC62828)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC62828)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(label, color = Color.Gray, fontSize = 12.sp)
        Text(value.ifEmpty { "No especificado" }, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.ExtraBold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun ProfileMenuOption(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = MaterialTheme.colorScheme.onSurface, fontSize = 15.sp, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
