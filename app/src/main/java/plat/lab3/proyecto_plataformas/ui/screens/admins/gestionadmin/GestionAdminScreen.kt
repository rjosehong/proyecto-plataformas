package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionAdminScreen(
    viewModel: GestionAdminViewModel = viewModel()
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
                title = { Text("UVG–Tours (Resumen Admin)") }
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F6F8))
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = greenDark)
                }
            }
            state.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F6F8))
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F6F8))
                        .padding(innerPadding)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Panel de Gestión",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = String.format(
                                Locale.US,
                                "Total acumulado: %.1f horas de servicio en campus",
                                state.horasTotalesProgramadas
                            ),
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MetricKpiCard(
                                title = "Tours semanales",
                                value = "${state.toursSemanales}",
                                subtitle = "programados",
                                accentColor = Color(0xFF1B4332),
                                modifier = Modifier.weight(1f)
                            )
                            MetricKpiCard(
                                title = "Tours sin asignar",
                                value = "${state.toursSinAsignar}",
                                subtitle = "por asignar",
                                accentColor = Color(0xFFD4A373),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            MetricKpiCard(
                                title = "Pendientes de asignar",
                                value = "${state.guiasPendientesDeAsignar}",
                                subtitle = "cupos de guía",
                                accentColor = Color(0xFFE76F51),
                                modifier = Modifier.weight(1f)
                            )
                            MetricKpiCard(
                                title = "Guías totales",
                                value = "${state.guiasTotales}",
                                subtitle = "guías activos",
                                accentColor = Color(0xFF2D6A4F),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    ToursAssignmentDonutCard(
                        asignados = state.toursAsignados,
                        sinAsignar = state.toursSinAsignar
                    )

                    GuidesWorkloadBarCard(
                        cargaPorGuia = state.cargaPorGuia
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricKpiCard(
    title: String,
    value: String,
    subtitle: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ToursAssignmentDonutCard(
    asignados: Int,
    sinAsignar: Int
) {
    val total = asignados + sinAsignar
    val greenAssigned = Color(0xFF2D6A4F)
    val amberPending = Color(0xFFD4A373)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Estado de Asignación de Tours",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(130.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 22.dp.toPx()
                        val diameter = size.minDimension - strokeWidth
                        val topLeft = Offset(strokeWidth / 2f, strokeWidth / 2f)
                        val arcSize = Size(diameter, diameter)

                        if (total == 0) {
                            drawArc(
                                color = Color(0xFFE2E8F0),
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                topLeft = topLeft,
                                size = arcSize,
                                style = Stroke(width = strokeWidth)
                            )
                        } else {
                            val assignedSweep = (asignados.toFloat() / total) * 360f
                            val pendingSweep = 360f - assignedSweep

                            if (assignedSweep > 0f) {
                                drawArc(
                                    color = greenAssigned,
                                    startAngle = -90f,
                                    sweepAngle = assignedSweep,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                            }
                            if (pendingSweep > 0f) {
                                drawArc(
                                    color = amberPending,
                                    startAngle = -90f + assignedSweep,
                                    sweepAngle = pendingSweep,
                                    useCenter = false,
                                    topLeft = topLeft,
                                    size = arcSize,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val porcentaje = if (total > 0) (asignados * 100) / total else 0
                        Text(
                            text = "$porcentaje%",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Text(
                            text = "cubiertos",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LegendRow(
                        color = greenAssigned,
                        label = "Asignados",
                        count = asignados
                    )
                    LegendRow(
                        color = amberPending,
                        label = "Sin asignar",
                        count = sinAsignar
                    )
                }
            }
        }
    }
}

@Composable
private fun LegendRow(color: Color, label: String, count: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = label, fontSize = 13.sp, color = Color.DarkGray)
            Text(
                text = "$count tours",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun GuidesWorkloadBarCard(
    cargaPorGuia: List<GuiaCargaItem>
) {
    val maxTours = (cargaPorGuia.maxOfOrNull { it.toursAsignados } ?: 1).coerceAtLeast(1)
    val barColor = Color(0xFF1B4332)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Tours y Horas por Guía",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            if (cargaPorGuia.isEmpty()) {
                Text(
                    text = "Aún no hay guías asignados a los tours.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            } else {
                cargaPorGuia.forEach { item ->
                    val fraction = (item.toursAsignados.toFloat() / maxTours).coerceIn(0.08f, 1f)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.nombre,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "%d tours (%.1fh)",
                                    item.toursAsignados,
                                    item.horasTotales
                                ),
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFFE2E8F0))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(fraction)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(barColor)
                            )
                        }
                    }
                }
            }
        }
    }
}