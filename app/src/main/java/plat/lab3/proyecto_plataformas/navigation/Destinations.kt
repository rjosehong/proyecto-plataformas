package plat.lab3.proyecto_plataformas.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login{
}

@Serializable
object Register{
}


@Serializable
enum class Role { ADMIN, TOUR_GUIDE, USER }

@Serializable
data class Menu(val role: Role)

// Bottom bar tabs (inside Menu)
@Serializable
object ToursTab

@Serializable
object FeedbackTab

@Serializable
object ProfileTab