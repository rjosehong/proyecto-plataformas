package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour

private fun formatHora(hora: Int): String {
    val periodo = if (hora >= 12) "PM" else "AM"
    val hora12 = when {
        hora == 0 -> 12
        hora > 12 -> hora - 12
        else -> hora
    }
    return "$hora12:00 $periodo"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionTourScreen(
    viewModel: AsignacionTourViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = { Text("UVG–Tours") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onEvent(AsignacionTourEvent.SelectTab(0)) },
                    text = { Text("Tour Feed") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onEvent(AsignacionTourEvent.SelectTab(1)) },
                    text = { Text("Tu Tour") }
                )
            }

            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.errorMessage!!, color = Color.Red)
                    }
                }
                else -> {
                    when (state.selectedTab) {
                        0 -> TourFeedContent(cards = state.tours, onClaim = { viewModel.onEvent(AsignacionTourEvent.ClaimSlot(it)) })
                        1 -> {
                            val mine = state.tours.firstOrNull { it.guia_necesitado == 0 }
                            if (mine != null) {
                                TuTourMasRecienteContent(card = mine)
                            } else {
                                Text("No tienes tours asignados", modifier = Modifier.padding(16.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TourFeedContent(cards: List<Tour>, onClaim: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Asígnate un Tour",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 56.sp
        )

        LazyColumn(
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cards) { card ->
                TourFeedCardItem(card = card, onClaim = onClaim)
            }
        }
    }
}

@Composable
fun TourFeedCardItem(card: Tour, onClaim: (Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .width(400.dp)
            .height(150.dp)
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "+${card.horas} horas de servicio",
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.tertiary)
                        .padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = "${card.personas_registradas} espacio disponible",
                        color = Color.White
                    )
                }
            }
            Text(
                text = card.tour_nombre,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    if (card.guia_necesitado > 0) {
                        Text(
                            text = "${card.guia_necesitado} Guía Necesitado",
                            color = Color.White
                        )
                    } else {
                        Text(
                            text = "${card.persona_asignada} Asignado",
                            color = Color.White
                        )
                    }
                }

                Button(
                    modifier = Modifier.wrapContentSize(),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    onClick = { onClaim(card.tourId) }
                ) {
                    Text(
                        text = "Reclamar espacio",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TuTourMasRecienteContent(card: Tour) {
    val horaInicial = formatHora(card.hora_inicial)
    val horaFinal = formatHora(card.hora_final)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .padding(horizontal = 3.dp, vertical = 2.dp),
                painter = painterResource(id = R.drawable.urgent_notification),
                contentDescription = "Menú"
            )
            Text(
                text = "Siguiente Tour Asignado",
                modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.size(width = 400.dp, height = 230.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cit_uvg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$horaInicial - $horaFinal — ${card.dia}",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "+${card.horas}h",
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column {
                        Text(
                            text = card.tour_nombre,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(18.dp)
                                    .padding(end = 2.dp),
                                painter = painterResource(id = R.drawable.location),
                                contentDescription = null,
                                tint = Color.White
                            )
                            Text(
                                text = card.lugar,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "${card.personas_registradas} personas registradas al tour",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}