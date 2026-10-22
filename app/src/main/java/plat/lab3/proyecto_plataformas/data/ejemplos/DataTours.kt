package plat.lab3.proyecto_plataformas.data.ejemplos


data class Tour(
    val tourId:Int, val horas:Double, val tour_nombre:String, val personas_registradas: Int,
    val dia:String, val persona_asignada:String, val guia_necesitado:Int, val lugar:String,
    val hora_inicial:Int, val hora_final:Int) {
}

val ejemplosDeTours = listOf<Tour>(
    Tour(1, 2.5, "Tour del CIT", 14, "Fri, Oct 02",
        "Iosef", 0, "Edificio del CIT", 12, 16),
    Tour(2, 3.5, "Tour del CIT", 13, "Mon, Oct 05",
        "Iosef", 0, "Edificio del CIT", 16, 18),
    Tour(3, 1.5, "Tour del CIT", 16, "Tue, Oct 13",
        "Iosef", 0, "Edificio del CIT", 15, 17)
)

data class CuadroHoras(
    val horas:Double, val tour_nombre:String, val Checked_In:Int, val tour_Slots:Int, val dia:String,
    val persona_asignada:String){

}

val ejemploHoras = listOf<CuadroHoras>(
    CuadroHoras(3.0, "Tour del Cit", 14, 12, "Fri, Oct 02", "Francisco Ordóñez"),
    CuadroHoras(2.5, "Tour del Cit", 13, 16, "Mon, Oct 05", "Iosef Más"),
    CuadroHoras(2.0, "Tour del Cit", 16, 15, "Tue, Oct 17", "Francisco Ordóñez")
)

data class calificacionGuia(
    val calificacion:Double, val nombre: String
)

val ejemploCalificacion = listOf<calificacionGuia>(
    calificacionGuia(98.1, "Iosef")
)

data class Admin(
    val tours_semanales:Int, val tours_noAsignados: Int, val tours_completos_por_asignar: Int, val guiasDeTour: Int
)

val ejemploAdminView = listOf<Admin>(
    Admin(18, 5, 13,27)
)