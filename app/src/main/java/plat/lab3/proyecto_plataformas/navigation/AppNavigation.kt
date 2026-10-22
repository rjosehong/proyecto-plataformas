package plat.lab3.proyecto_plataformas.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploAdminView
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemploCalificacion
import plat.lab3.proyecto_plataformas.ui.screens.AppLogin
import plat.lab3.proyecto_plataformas.ui.screens.AppRegister
import plat.lab3.proyecto_plataformas.ui.screens.Profile
import plat.lab3.proyecto_plataformas.ui.screens.admins.AsignacionHoras
import plat.lab3.proyecto_plataformas.ui.screens.admins.gestionAdmin
import plat.lab3.proyecto_plataformas.ui.screens.estudiantes.AsignacionTour
import plat.lab3.proyecto_plataformas.ui.screens.estudiantes.ScoresScreen
import plat.lab3.proyecto_plataformas.ui.screens.usuarios.ToursScreen
import plat.lab3.proyecto_plataformas.ui.screens.usuarios.RateTourScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Login) {

        composable<Login> {
            AppLogin(
                onEnterClick = { role ->
                    navController.navigate(Menu(role)) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Register) }
            )
        }

        composable<Register> {
            AppRegister(
                onRegisterDone = { navController.popBackStack() }
            )
        }

        composable<Menu> { backStackEntry ->
            val args = backStackEntry.toRoute<Menu>()
            MainTabs(
                role = args.role,
                onLogoutClick = {
                    navController.navigate(Login) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun MainTabs(role: Role, onLogoutClick: () -> Unit) {
    val tabsNavController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(tabsNavController) }
    ) { padding ->
        NavHost(
            navController = tabsNavController,
            startDestination = ToursTab,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable<ToursTab> {
                when (role) {
                    Role.ADMIN -> gestionAdmin(cards = ejemploAdminView)
                    Role.TOUR_GUIDE -> AsignacionTour()
                    Role.USER -> ToursScreen()
                }
            }
            composable<FeedbackTab> {
                when (role) {
                    Role.ADMIN -> AsignacionHoras()
                    Role.TOUR_GUIDE -> ScoresScreen(cards = ejemploCalificacion)
                    Role.USER -> RateTourScreen()
                }
            }
            composable<ProfileTab> {
                Profile(onLogoutClick = onLogoutClick)
            }
        }
    }
}

@Composable
private fun BlankScreen() {
    Box(modifier = Modifier.fillMaxSize())
}