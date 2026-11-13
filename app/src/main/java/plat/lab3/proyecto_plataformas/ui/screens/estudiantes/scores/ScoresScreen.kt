package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.scores

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
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoresScreen(
    viewModel: ScoresViewModel = viewModel(),
    modifier: Modifier = Modifier
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
                title = { Text("UVG–Tours") }
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                Box(
                    modifier = modifier
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
                    modifier = modifier
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
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F6F8))
                        .padding(innerPadding)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = greenDark),
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "RENDIMIENTO DEL GUÍA",
                                        color = Color(0xFF95D5B2),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = state.guideName,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (state.guideEmail.isNotBlank()) {
                                        Text(
                                            text = state.guideEmail,
                                            color = Color(0xFFD8F3DC),
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Surface(
                                    color = Color.White.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = String.format(Locale.US, "%.1f", state.promedioTotal),
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "de 100 pts",
                                            color = Color(0xFF95D5B2),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                            val resumenEvaluaciones = when (state.totalEvaluaciones) {
                                0 -> "Aún no tienes evaluaciones registradas"
                                1 -> "Basado en 1 evaluación recibida"
                                else -> "Basado en ${state.totalEvaluaciones} evaluaciones recibidas"
                            }
                            Text(
                                text = resumenEvaluaciones,
                                color = Color(0xFFD8F3DC),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Text(
                        text = "Calificaciones recientes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    if (state.calificacionesRecientes.isEmpty()) {
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
                                Text(
                                    text = "Todavía no has recibido calificaciones en tus tours.",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.calificacionesRecientes) { item ->
                                GuiaRatingCardItem(item = item)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GuiaRatingCardItem(item: GuiaRatingItem) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.tourNombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = Color(0xFF2D6A4F).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = String.format(Locale.US, "%.1f / 100", item.calificacion),
                        color = Color(0xFF2D6A4F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            if (item.comentario.isNotBlank()) {
                Surface(
                    color = Color(0xFFF4F6F8),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "\"${item.comentario}\"",
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            } else {
                Text(
                    text = "Sin comentarios adicionales",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}