package plat.lab3.proyecto_plataformas.ui.screens.estudiantes.asignaciontour

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.data.calendar.CalendarHelper
import plat.lab3.proyecto_plataformas.data.calendar.rememberCalendarAuthorizer
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.work.RequestNotificationPermission
import plat.lab3.proyecto_plataformas.work.scheduleTourReminder

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
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    RequestNotificationPermission()

    val authorizeCalendar = rememberCalendarAuthorizer(
        onToken = { token ->
            val tourToSync = state.claimedTourForSync
            if (tourToSync != null) {
                scope.launch {
                    val link = withContext(Dispatchers.IO) {
                        runCatching { CalendarHelper.insertTourEvent(token, tourToSync) }.getOrNull()
                    }
                    scheduleTourReminder(context, tourToSync)
                    viewModel.onEvent(AsignacionTourEvent.ClearClaimedTourForSync)
                    val msg = if (link != null) {
                        "Tour agregado a tu Google Calendar y recordatorio listo"
                    } else {
                        "Cupo reclamado y recordatorio programado"
                    }
                    viewModel.onEvent(AsignacionTourEvent.ShowFeedback(msg))
                }
            }
        },
        onError = { err ->
            state.claimedTourForSync?.let { scheduleTourReminder(context, it) }
            viewModel.onEvent(AsignacionTourEvent.ClearClaimedTourForSync)
            viewModel.onEvent(AsignacionTourEvent.ShowFeedback(err))
        }
    )

    LaunchedEffect(state.claimedTourForSync) {
        if (state.claimedTourForSync != null) {
            authorizeCalendar()
        }
    }

    LaunchedEffect(state.feedbackMessage) {
        state.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onEvent(AsignacionTourEvent.ClearFeedback)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                .padding(innerPadding),
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
                        0 -> {
                            val filteredCards = if (state.filterOnlyAvailable) {
                                state.tours.filter { it.guia_necesitado > 0 }
                            } else {
                                state.tours
                            }
                            TourFeedContent(
                                cards = filteredCards,
                                filterOnlyAvailable = state.filterOnlyAvailable,
                                onFilterChange = { viewModel.onEvent(AsignacionTourEvent.SetFilterAvailable(it)) },
                                onClaim = { viewModel.onEvent(AsignacionTourEvent.ClaimSlot(it)) }
                            )
                        }
                        1 -> {
                            val mine = state.tours.firstOrNull { it.guia_necesitado == 0 }
                            if (mine != null) {
                                TuTourMasRecienteContent(card = mine)
                            } else {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("No tienes tours asignados", modifier = Modifier.padding(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TourFeedContent(
    cards: List<Tour>,
    filterOnlyAvailable: Boolean,
    onFilterChange: (Boolean) -> Unit,
    onClaim: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B4332)),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "BIENVENIDO A UVG-TOURS",
                    color = Color(0xFF95D5B2),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Explora y programa",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Asígnate un tour y gestiona tus actividades del campus.",
                    color = Color(0xFFD8F3DC),
                    fontSize = 13.sp
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = if (!filterOnlyAvailable) Color.Black else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.clickable { onFilterChange(false) }
            ) {
                Text(
                    text = "Todos",
                    color = if (!filterOnlyAvailable) Color.White else Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Surface(
                color = if (filterOnlyAvailable) Color.Black else Color(0xFFE2E8F0),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.clickable { onFilterChange(true) }
            ) {
                Text(
                    text = "Disponibles",
                    color = if (filterOnlyAvailable) Color.White else Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        if (cards.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Actualmente no hay tours disponibles",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cards) { card ->
                    TourFeedCardItem(card = card, onClaim = onClaim)
                }
            }
        }
    }
}

@Composable
fun TourFeedCardItem(card: Tour, onClaim: (Int) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "+${card.horas} horas de servicio",
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Surface(
                    color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${card.personas_registradas} inscritos",
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = card.tour_nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = if (card.guia_necesitado > 0) Color(0xFFD4A373).copy(alpha = 0.15f) else Color(0xFF2D6A4F).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (card.guia_necesitado > 0) "${card.guia_necesitado} Guía Necesitado" else "${card.persona_asignada} Asignado",
                        color = if (card.guia_necesitado > 0) Color(0xFFD4A373) else Color(0xFF2D6A4F),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Button(
                    enabled = card.guia_necesitado > 0,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    onClick = { onClaim(card.tourId) }
                ) {
                    Text(
                        text = if (card.guia_necesitado > 0) "Reclamar espacio" else "Asignado",
                        fontSize = 14.sp
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 4.dp),
                painter = painterResource(id = R.drawable.urgent_notification),
                contentDescription = "Notificación"
            )
            Text(
                text = "Siguiente Tour Asignado",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
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
                        .padding(16.dp),
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
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
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

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.person),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "${card.personas_registradas} personas registradas al tour",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}