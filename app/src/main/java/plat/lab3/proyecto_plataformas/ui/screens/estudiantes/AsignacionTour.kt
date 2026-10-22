package plat.lab3.proyecto_plataformas.ui.screens.estudiantes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.data.ejemplos.ejemplosDeTours
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme


private fun formatHora(hora: Int): String {
    val periodo = if (hora >= 12) "PM" else "AM"
    val hora12 = when {
        hora == 0 -> 12
        hora > 12 -> hora - 12
        else -> hora
    }
    return "$hora12:00 $periodo"
}

@Composable
fun TourFeed(cards: List<Tour>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Asignate un Tour",
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
                TourFeedCard(card)
            }
        }
    }
}

@Composable
fun TourFeedCard(card: Tour) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .width(400.dp)
            .height(150.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .wrapContentSize()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(end = 4.dp)
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
                    .padding(end = 4.dp)
            ) {
                Text(
                    text = "${card.personas_registradas} espacio disponible",
                    color = Color.White
                )
            }
        }
        Text(
            text = card.tour_nombre,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(4.dp)
        )
        Text(
            text = card.dia,
            modifier = Modifier.padding(4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .wrapContentSize()
                    .background(MaterialTheme.colorScheme.secondary)
                    .align(Alignment.CenterVertically)
            ) {
                if (card.guia_necesitado > 0) {
                    Text(
                        text = "${card.guia_necesitado} Guia Necesitado",
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
                onClick = {

                }
            ) {
                Text(
                    text = "Claim Slot",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun TuTourMasReciente(card: Tour, modifier: Modifier = Modifier) {
    val horaInicial = formatHora(card.hora_inicial)
    val horaFinal = formatHora(card.hora_final)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                modifier = Modifier
                    .size(25.dp)
                    .padding(horizontal = 3.dp, vertical = 2.dp),
                painter = painterResource(id = R.drawable.urgent_notification),
                contentDescription = "Menu"
            )
            Text(
                text = "Siguiente Tour Asignado",
                modifier = Modifier.padding(horizontal = 3.dp, vertical = 2.dp),
                fontSize = 20.sp
            )
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier.size(width = 400.dp, height = 200.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$horaInicial - $horaFinal — ${card.dia}",
                    fontSize = 11.sp
                )

                Box(modifier = Modifier.clip(shape = RoundedCornerShape(6.dp))) {
                    Text("+${card.horas}")
                }
            }

            Column {
                Text(
                    text = "Tour ${card.tour_nombre}",
                    fontSize = 35.sp
                )
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier
                            .size(25.dp)
                            .padding(horizontal = 3.dp, vertical = 2.dp),
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null
                    )
                    Text(
                        text = card.lugar,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.padding(vertical = 6.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cit_uvg),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp),
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
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AsignacionTour() {
    AsignacionTourContent(cards = ejemplosDeTours)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionTourContent(cards: List<Tour>) {
    var selectedTab by remember { mutableStateOf(0) }

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
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Tour Feed") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Your Tour") })
            }
            when (selectedTab) {
                0 -> TourFeed(cards)
                1 -> {
                    val mine = cards.firstOrNull { it.guia_necesitado == 0 }
                    if (mine != null) {
                        TuTourMasReciente(mine)
                    } else {
                        Text("No tienes tours asignados", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AsignacionesPreview() {
    ProyectoplataformasTheme(darkTheme = false) {
        AsignacionTourContent(cards = ejemplosDeTours)
    }
}