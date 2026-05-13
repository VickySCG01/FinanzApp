package com.equipo.finanzapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.equipo.finanzapp.ui.screens.auth.LoginScreen
import com.equipo.finanzapp.ui.screens.home.AsesorAcciones.AsesorAccionesScreen
import com.equipo.finanzapp.ui.screens.home.AsesorPerfil.AsesorPerfilScreen
import com.equipo.finanzapp.ui.screens.home.ClientePerfil.ClientePerfilScreen
import com.equipo.finanzapp.ui.screens.home.HomeScreen
import com.equipo.finanzapp.ui.screens.categorias.CategoriasScreen
import com.equipo.finanzapp.ui.screens.cuentas.CuentasScreen
import com.equipo.finanzapp.ui.screens.metas.MetasAhorroScreen
import com.equipo.finanzapp.ui.screens.security.SecurityScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object ClientePerfil : Screen("cliente_perfil")
    object AsesorPerfil : Screen("asesor_perfil")
    object AsesorAcciones : Screen("asesor_acciones")
    object Categorias : Screen("categorias")
    object Cuentas : Screen("cuentas")
    object MetasAhorro : Screen("metas_ahorro")
    object Security : Screen("security")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToClientePerfil = {
                    navController.navigate(Screen.ClientePerfil.route)
                },
                onNavigateToAsesorPerfil = {
                    navController.navigate(Screen.AsesorPerfil.route)
                },
                onNavigateToAsesorAcciones = {
                    navController.navigate(Screen.AsesorAcciones.route)
                },
                onNavigateToCategorias = {
                    navController.navigate(Screen.Categorias.route)
                },
                onNavigateToCuentas = {
                    navController.navigate(Screen.Cuentas.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.ClientePerfil.route) {
            ClientePerfilScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSecurity = { navController.navigate(Screen.Security.route) },
                onNavigateToMetas = { navController.navigate(Screen.MetasAhorro.route) }
            )
        }
        composable(Screen.Security.route) {
            SecurityScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.MetasAhorro.route) {
            MetasAhorroScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.AsesorPerfil.route) {
            AsesorPerfilScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.AsesorAcciones.route) {
            AsesorAccionesScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Categorias.route) {
            CategoriasScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Cuentas.route) {
            CuentasScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
