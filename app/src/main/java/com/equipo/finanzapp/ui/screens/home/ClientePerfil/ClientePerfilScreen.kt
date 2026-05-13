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
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.finanzapp.ui.theme.BbvaNavy
import com.equipo.finanzapp.ui.theme.BbvaWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientePerfilScreen(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = BbvaWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BbvaNavy,
                    titleContentColor = BbvaWhite
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF4F4F4))
                .verticalScroll(rememberScrollState())
        ) {
            // Header Perfil
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BbvaNavy)
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(BbvaWhite.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = BbvaWhite, modifier = Modifier.size(60.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Juan Pérez", color = BbvaWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text("Estudiante de Ingeniería", color = BbvaWhite.copy(alpha = 0.7f), fontSize = 14.sp)
                }
            }

            // Info Sections
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileSectionTitle("DATOS PERSONALES")
                ProfileInfoCard(
                    listOf(
                        "Correo" to "juan.perez@universidad.edu.mx",
                        "Teléfono" to "+52 55 1234 5678",
                        "Matrícula" to "202300456"
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileSectionTitle("SEGURIDAD")
                ProfileMenuOption("Cambiar contraseña", Icons.Default.Lock)
                ProfileMenuOption("Autenticación en dos pasos", Icons.Default.CheckCircle)
                ProfileMenuOption("Dispositivos vinculados", Icons.Default.Settings)

                Spacer(modifier = Modifier.height(24.dp))

                ProfileSectionTitle("PREFERENCIAS")
                ProfileMenuOption("Notificaciones", Icons.Default.Notifications)
                ProfileMenuOption("Idioma", Icons.Filled.Public)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { /* Logout logic or Delete account */ },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Eliminar mi cuenta", color = Color.Red.copy(alpha = 0.7f))
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
    )
}

@Composable
fun ProfileInfoCard(items: List<Pair<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            items.forEachIndexed { index, item ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(item.first, color = Color.Gray, fontSize = 14.sp)
                    Text(item.second, color = BbvaNavy, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
                if (index < items.size - 1) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                }
            }
        }
    }
}

@Composable
fun ProfileMenuOption(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { /* Action */ },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = BbvaNavy, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(label, color = BbvaNavy, fontSize = 14.sp, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
