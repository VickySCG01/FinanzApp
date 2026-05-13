package com.equipo.finanzapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.finanzapp.data.local.SessionManager
import com.equipo.finanzapp.ui.theme.BbvaLightBlue
import com.equipo.finanzapp.ui.theme.BbvaNavy
import com.equipo.finanzapp.ui.theme.BbvaWhite
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToClientePerfil: () -> Unit,
    onNavigateToAsesorPerfil: () -> Unit,
    onNavigateToAsesorAcciones: () -> Unit,
    onNavigateToCategorias: () -> Unit,
    onNavigateToCuentas: () -> Unit,
    onLogout: () -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BbvaNavy,
                drawerContentColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerHeader()
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Inicio", color = Color.White) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Home, contentDescription = null, tint = Color.White) },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent, selectedContainerColor = BbvaLightBlue.copy(alpha = 0.2f)),
                    shape = RoundedCornerShape(0.dp)
                )
                DrawerMenuItem("Categorías", Icons.Default.Category) {
                    scope.launch { drawerState.close() }
                    onNavigateToCategorias()
                }
                DrawerMenuItem("Mi Perfil", Icons.Default.Person) {
                    scope.launch { drawerState.close() }
                    onNavigateToClientePerfil()
                }
                DrawerMenuItem("Asesoría", Icons.Default.SupportAgent) {
                    scope.launch { drawerState.close() }
                    onNavigateToAsesorAcciones()
                }
                DrawerMenuItem("Cuentas", Icons.Default.AccountBalanceWallet) {
                    scope.launch { drawerState.close() }
                    onNavigateToCuentas()
                }
                DrawerMenuItem("Préstamos", Icons.Default.School) { /* TODO */ }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Divider(color = Color.White.copy(alpha = 0.2f))
                DrawerMenuItem("Cerrar Sesión", Icons.Default.ExitToApp) {
                    scope.launch { drawerState.close() }
                    sessionManager.clearAuthToken()
                    onLogout()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            "Hola, Estudiante",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = BbvaWhite
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = BbvaWhite)
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = BbvaWhite)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = BbvaNavy
                    )
                )
            },
            containerColor = Color(0xFFF4F4F4)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    MainAccountCard()
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Accesos directos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = BbvaNavy
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShortcutItem("Transferir", Icons.Default.SwapHoriz)
                        ShortcutItem("Categorías", Icons.Default.Category) { onNavigateToCategorias() }
                        ShortcutItem("Retiro sin tarjeta", Icons.Default.Atm)
                        ShortcutItem("Más", Icons.Default.Add)
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    PromotionCard()
                }
            }
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Juan Pérez", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text("Ver mi perfil", color = BbvaLightBlue, fontSize = 14.sp)
    }
}

@Composable
fun DrawerMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, color = Color.White) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null, tint = Color.White) },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
        shape = RoundedCornerShape(0.dp)
    )
}

@Composable
fun MainAccountCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("CUENTAS EN PESOS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BbvaNavy)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("001ah9234", color = BbvaNavy, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("$ 12,450.00", color = BbvaNavy, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text("Saldo disponible", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun ShortcutItem(label: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = BbvaNavy)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(label, fontSize = 12.sp, color = BbvaNavy)
    }
}

@Composable
fun PromotionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BbvaNavy),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("¿Necesitas apoyo para tu colegiatura?", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Conoce nuestros préstamos estudiantiles con tasa preferencial.", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
            Icon(Icons.Default.School, contentDescription = null, tint = Color.White, modifier = Modifier.size(40.dp))
        }
    }
}
