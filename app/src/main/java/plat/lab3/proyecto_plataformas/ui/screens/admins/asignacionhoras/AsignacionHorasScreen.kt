package plat.lab3.proyecto_plataformas.ui.screens.admins.asignacionhoras

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.proyecto_plataformas.data.ejemplos.calificacionGuia
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionHorasScreen(
    viewModel: AsignacionHorasViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val greenDark = Color(0xFF1B4332)

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                ),
                title = { Text("UVG–Tours (Panel Admin)") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onEvent(AsignacionHorasEvent.SelectTab(0)) },
                    text = { Text("Promedio por Guía") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onEvent(AsignacionHorasEvent.SelectTab(1)) },
                    text = { Text("Recientes") }
                )
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = greenDark)
                    }
                }
                state.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.errorMessage ?: "Error desconocido",
                            color = Color.Red,
                            fontSize = 16.sp
                        )
                    }
                }
                else -> {
                    when (state.selectedTab) {
                        0 -> PromediosPorGuiaContent(
                            promedioGeneral = state.promedioGeneral,
                            promedios = state.promediosPorGuia
                        )
                        1 -> CalificacionesRecientesContent(
                            calificaciones = state.calificaciones
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PromediosPorGuiaContent(
    promedioGeneral: Double,
    promedios: List<GuiaPromedioItem>
) {
    val greenDark = Color(0xFF1B4332)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = greenDark),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PROMEDIO GENERAL",
                        color = Color(0xFF95D5B2),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Desempeño global de guías",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
                Text(
                    text = String.format(Locale.US, "%.1f / 100", promedioGeneral),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Text(
            text = "Promedio individual por guía",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )

        if (promedios.isEmpty()) {
            EmptyFeedbackCard("Aún no hay evaluaciones para calcular promedios.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(promedios) { item ->
                    GuiaPromedioCardItem(item = item)
                }
            }
        }
    }
}

@Composable
private fun CalificacionesRecientesContent(
    calificaciones: List<calificacionGuia>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Calificaciones recientes por tour",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (calificaciones.isEmpty()) {
            EmptyFeedbackCard("Aún no hay calificaciones registradas por los usuarios.")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(calificaciones) { calif ->
                    CalificacionAdminCardItem(calif = calif)
                }
            }
        }
    }
}

@Composable
private fun GuiaPromedioCardItem(item: GuiaPromedioItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.guiaNombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                val textoEvaluaciones = if (item.totalEvaluaciones == 1) {
                    "1 evaluación recibida"
                } else {
                    "${item.totalEvaluaciones} evaluaciones recibidas"
                }
                Text(
                    text = textoEvaluaciones,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Surface(
                color = Color(0xFF1B4332).copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f / 100", item.promedio),
                    color = Color(0xFF1B4332),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun CalificacionAdminCardItem(calif: calificacionGuia) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = calif.nombre.ifBlank { "Evaluación de Guía" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Nota asignada en recorrido",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Surface(
                color = Color(0xFF2D6A4F).copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.1f / 100", calif.calificacion),
                    color = Color(0xFF2D6A4F),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyFeedbackCard(message: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = message, color = Color.Gray)
        }
    }
}