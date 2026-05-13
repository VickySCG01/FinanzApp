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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.equipo.finanzapp.FinanzApplication
import com.equipo.finanzapp.data.local.ClienteEntity
import com.equipo.finanzapp.data.local.SessionManager
import com.equipo.finanzapp.ui.AppViewModelFactory
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
    val application = LocalContext.current.applicationContext as FinanzApplication
    val homeViewModel: HomeViewModel = viewModel(
        factory = AppViewModelFactory(application.repository)
    )

    val perfil by homeViewModel.perfil.collectAsState()
    val balance by homeViewModel.balance.collectAsState()
    val ingresos by homeViewModel.ingresosTotales.collectAsState()
    val gastos by homeViewModel.gastosTotales.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.primary,
                drawerContentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.width(300.dp)
            ) {
                DrawerHeader(perfil)
                Spacer(modifier = Modifier.height(8.dp))
                NavigationDrawerItem(
                    label = { Text("Resumen", color = MaterialTheme.colorScheme.onPrimary) },
                    selected = true,
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary) },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent, 
                        selectedContainerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)
                    ),
                    shape = RoundedCornerShape(0.dp)
                )
                DrawerMenuItem("Mis Movimientos", Icons.Default.ReceiptLong) {
                    scope.launch { drawerState.close() }
                    onNavigateToCuentas()
                }
                DrawerMenuItem("Presupuesto por Categorías", Icons.Default.Category) {
                    scope.launch { drawerState.close() }
                    onNavigateToCategorias()
                }
                DrawerMenuItem("Asesoría Estudiantil", Icons.Default.SupportAgent) {
                    scope.launch { drawerState.close() }
                    onNavigateToAsesorAcciones()
                }
                DrawerMenuItem("Mi Perfil", Icons.Default.Person) {
                    scope.launch { drawerState.close() }
                    onNavigateToClientePerfil()
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                Divider(color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f))
                DrawerMenuItem("Cerrar Sesión", Icons.Default.Logout) {
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
                            "FinanzApp",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    BalanceCard(balance, ingresos, gastos)
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Acciones Rápidas",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        ShortcutItem("Nuevo Movimiento", Icons.Default.AddCircleOutline) { onNavigateToCuentas() }
                        ShortcutItem("Presupuesto", Icons.Default.PieChart) { onNavigateToCategorias() }
                        ShortcutItem("Mi Perfil", Icons.Default.AccountCircle) { onNavigateToClientePerfil() }
                        ShortcutItem("Asesor", Icons.Default.QuestionAnswer) { onNavigateToAsesorAcciones() }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    FinancialTipCard()
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(perfil: ClienteEntity?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(48.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = if (perfil != null) "${perfil.nombre} ${perfil.apellido}" else "Usuario Estudiante", 
            color = MaterialTheme.colorScheme.onPrimary, 
            fontWeight = FontWeight.Bold, 
            fontSize = 20.sp
        )
        Text(
            text = perfil?.rfc ?: "Controlando mis finanzas", 
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), 
            fontSize = 14.sp
        )
    }
}

@Composable
fun DrawerMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(label, color = MaterialTheme.colorScheme.onPrimary) },
        selected = false,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary) },
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent),
        shape = RoundedCornerShape(0.dp)
    )
}

@Composable
fun BalanceCard(balance: Double, ingresos: Double, gastos: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("BALANCE TOTAL", color = MaterialTheme.colorScheme.secondary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$ ${String.format("%.2f", balance)}", 
                color = if (balance >= 0) MaterialTheme.colorScheme.primary else Color(0xFFC62828), 
                fontSize = 32.sp, 
                fontWeight = FontWeight.ExtraBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Ingresos", fontSize = 12.sp, color = Color.Gray)
                    Text("$ ${String.format("%.2f", ingresos)}", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Gastos", fontSize = 12.sp, color = Color.Gray)
                    Text("$ ${String.format("%.2f", gastos)}", color = Color(0xFFC62828), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ShortcutItem(label: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label, 
            fontSize = 11.sp, 
            fontWeight = FontWeight.Medium, 
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1
        )
    }
}

@Composable
fun FinancialTipCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Tip del día", color = MaterialTheme.colorScheme.onTertiaryContainer, fontWeight = FontWeight.Bold)
                Text("Evita los gastos hormiga para ahorrar un 15% más al mes.", color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f), fontSize = 13.sp)
            }
            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
        }
    }
}
