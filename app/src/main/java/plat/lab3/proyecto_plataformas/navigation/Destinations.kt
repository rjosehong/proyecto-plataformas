package plat.lab3.proyecto_plataformas.navigation

import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object RegisterRoute

@Serializable
enum class Role { ADMIN, TOUR_GUIDE, USER }

@Serializable
data class MenuRoute(val role: Role)

// Bottom bar tabs
@Serializable
object ToursTabRoute

@Serializable
object FeedbackTabRoute

@Serializable
object ProfileTabRoute