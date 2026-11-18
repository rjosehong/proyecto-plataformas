package plat.lab3.proyecto_plataformas.data.teamup

object TeamupConfig {

    const val BASE_URL = "https://api.teamup.com"

    const val API_KEY = "9c4948f9f2aa1ee3d29be250f472331f5bd7f60d5e7312cf110808a51d516d4c"

    const val CALENDAR_KEY = "ksgwt5wbk1bvu83c45"

    const val SUBCALENDAR_ID = 0

    fun isConfigured(): Boolean =
        !API_KEY.startsWith("PON_AQUI") && API_KEY.isNotBlank() &&
            !CALENDAR_KEY.startsWith("PON_AQUI") && CALENDAR_KEY.isNotBlank()
}
