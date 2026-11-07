package plat.lab3.proyecto_plataformas.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.reflect.KClass
import plat.lab3.proyecto_plataformas.R

private data class BottomNavItem(
    val label: String,
    val route: Any,
    val routeClass: KClass<*>,
    val icon: @Composable () -> Unit
)

@Composable
fun BottomBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val items = listOf(
        BottomNavItem("Tours", ToursTabRoute, ToursTabRoute::class) {
            Icon(painterResource(id = R.drawable.calendar), contentDescription = null, modifier = Modifier.size(24.dp))
        },
        BottomNavItem("Feedback", FeedbackTabRoute, FeedbackTabRoute::class) {
            Icon(painterResource(R.drawable.calendar), contentDescription = null, modifier = Modifier.size(24.dp))
        },
        BottomNavItem("Profile", ProfileTabRoute, ProfileTabRoute::class) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(24.dp))
        }
    )

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.hasRoute(item.routeClass) } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo<ToursTabRoute> { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = item.icon,
                label = { Text(item.label) }
            )
        }
    }
}