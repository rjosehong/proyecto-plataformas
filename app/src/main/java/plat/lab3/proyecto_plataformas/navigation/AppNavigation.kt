package plat.lab3.proyecto_plataformas.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.data.auth.AuthRepository
import plat.lab3.proyecto_plataformas.data.firestore.FirestoreSeeder
import plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras.AsignacionHorasScreen
import plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin.GestionAdminScreen
import plat.lab3.proyecto_plataformas.ui.screens.calendar.CalendarScreen
import plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour.AsignacionTourScreen
import plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores.ScoresScreen
import plat.lab3.proyecto_plataformas.ui.screens.login.AppLoginScreen
import plat.lab3.proyecto_plataformas.ui.screens.profile.ProfileScreen
import plat.lab3.proyecto_plataformas.ui.screens.register.AppRegisterScreen
import plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour.RateTourScreen
import plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen.ToursScreen
import plat.lab3.proyecto_plataformas.work.RequestNotificationPermission

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginRoute) {

        composable<LoginRoute> {
            LaunchedEffect(Unit) {
                AuthRepository.currentRole()?.let { role ->
                    navController.navigate(MenuRoute(role)) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            }
            AppLoginScreen(
                onEnterClick = { role: Role ->
                    navController.navigate(MenuRoute(role)) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(RegisterRoute) }
            )
        }

        composable<RegisterRoute> {
            AppRegisterScreen(
                onRegisterDone = { role: Role ->
                    navController.navigate(MenuRoute(role)) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            )
        }

        composable<MenuRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<MenuRoute>()
            MainTabs(
                role = args.role,
                onCalendarClick = { navController.navigate(CalendarRoute) },
                onLogoutClick = {
                    navController.navigate(LoginRoute) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<CalendarRoute> {
            CalendarScreen(onBackClick = { navController.popBackStack() })
        }
    }
}

@Composable
private fun MainTabs(role: Role, onCalendarClick: () -> Unit, onLogoutClick: () -> Unit) {
    val tabsNavController = rememberNavController()
    LaunchedEffect(Unit) {
        FirestoreSeeder.seedIfEmpty()
    }

    val tabEntry by tabsNavController.currentBackStackEntryAsState()
    val onProfileTab = tabEntry?.destination?.hasRoute(ProfileTabRoute::class) == true

    Scaffold(
        bottomBar = { BottomBar(tabsNavController) },
        floatingActionButton = {
            if (!onProfileTab) {
                FloatingActionButton(onClick = onCalendarClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.calendar_icon),
                        contentDescription = "Calendario",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = tabsNavController,
            startDestination = ToursTabRoute,
            modifier = Modifier.padding(bottom = padding.calculateBottomPadding())
        ) {
            composable<ToursTabRoute> {
                when (role) {
                    Role.ADMIN -> GestionAdminScreen()
                    Role.TOUR_GUIDE -> AsignacionTourScreen()
                    Role.USER -> ToursScreen()
                }
            }
            composable<FeedbackTabRoute> {
                when (role) {
                    Role.ADMIN -> AsignacionHorasScreen()
                    Role.TOUR_GUIDE -> ScoresScreen()
                    Role.USER -> RateTourScreen()
                }
            }
            composable<ProfileTabRoute> {
                ProfileScreen(onLogoutClick = onLogoutClick)
            }
        }
    }
}