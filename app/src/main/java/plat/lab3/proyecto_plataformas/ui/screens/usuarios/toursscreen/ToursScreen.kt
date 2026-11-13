package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import plat.lab3.proyecto_plataformas.data.calendar.CalendarHelper
import plat.lab3.proyecto_plataformas.data.calendar.rememberCalendarAuthorizer
import plat.lab3.proyecto_plataformas.data.ejemplos.Tour
import plat.lab3.proyecto_plataformas.work.RequestNotificationPermission
import plat.lab3.proyecto_plataformas.work.scheduleTourReminder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToursScreen(
    viewModel: ToursScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    RequestNotificationPermission()

    val authorizeCalendar = rememberCalendarAuthorizer(
        onToken = { token ->
            val tourToSync = state.pendingCalendarTour
            if (tourToSync != null) {
                scope.launch {
                    val link = withContext(Dispatchers.IO) {
                        runCatching { CalendarHelper.insertTourEvent(token, tourToSync) }.getOrNull()
                    }
                    scheduleTourReminder(context, tourToSync)
                    viewModel.onEvent(ToursScreenEvent.ClearPendingCalendarTour)
                    val msg = if (link != null) {
                        "Agregado a Google Calendar y recordatorio programado"
                    } else {
                        "Tour guardado y recordatorio programado"
                    }
                    viewModel.onEvent(ToursScreenEvent.ShowFeedback(msg))
                }
            }
        },
        onError = { err ->
            state.pendingCalendarTour?.let { scheduleTourReminder(context, it) }
            viewModel.onEvent(ToursScreenEvent.ClearPendingCalendarTour)
            viewModel.onEvent(ToursScreenEvent.ShowFeedback(err))
        }
    )

    LaunchedEffect(state.pendingCalendarTour) {
        if (state.pendingCalendarTour != null) {
            authorizeCalendar()
        }
    }

    LaunchedEffect(state.feedbackMessage) {
        state.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onEvent(ToursScreenEvent.ClearFeedback)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                title = { Text("Bienvenido a UVG–Tours") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(selectedTabIndex = state.selectedTab) {
                Tab(
                    selected = state.selectedTab == 0,
                    onClick = { viewModel.onEvent(ToursScreenEvent.SelectTab(0)) },
                    text = { Text("Tour Feed") }
                )
                Tab(
                    selected = state.selectedTab == 1,
                    onClick = { viewModel.onEvent(ToursScreenEvent.SelectTab(1)) },
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
                        0 -> UserToursHomeContent(
                            onOpenDialog = { viewModel.onEvent(ToursScreenEvent.OpenDialog(it)) }
                        )
                        1 -> UserAssignedToursContent(
                            tours = state.tours.filter { it.tourId in state.registeredTourIds || it.personas_registradas > 0 }
                        )
                    }
                }
            }
        }

        when (state.activeDialog) {
            UserTourDialog.CONSULTAR_TOURS -> {
                ConsultarToursDialog(
                    tours = state.tours,
                    registeredIds = state.registeredTourIds,
                    onRegister = { viewModel.onEvent(ToursScreenEvent.RegisterToTour(it)) },
                    onDismiss = { viewModel.onEvent(ToursScreenEvent.DismissDialog) }
                )
            }
            UserTourDialog.PROGRAMAR_TOUR -> {
                ProgramarTourDialog(
                    onConfirm = { nombre, lugar, dia, ini, fin ->
                        viewModel.onEvent(ToursScreenEvent.CreateNewTour(nombre, lugar, dia, ini, fin))
                    },
                    onDismiss = { viewModel.onEvent(ToursScreenEvent.DismissDialog) }
                )
            }
            UserTourDialog.INFO_CARRERAS -> {
                InfoCarrerasUvgDialog(
                    onDismiss = { viewModel.onEvent(ToursScreenEvent.DismissDialog) }
                )
            }
            UserTourDialog.NONE -> Unit
        }
    }
}

@Composable
fun UserToursHomeContent(
    onOpenDialog: (UserTourDialog) -> Unit
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
                    text = "Gestiona tus actividades y recorridos del campus.",
                    color = Color(0xFFD8F3DC),
                    fontSize = 13.sp
                )
            }
        }

        StyledTourCardItem(
            title = "Consulta sobre tours",
            subtitle = "Ver listado general de recorridos en Firestore",
            onClick = { onOpenDialog(UserTourDialog.CONSULTAR_TOURS) }
        )
        StyledTourCardItem(
            title = "Programa un tour",
            subtitle = "Crear y agendar un nuevo espacio",
            onClick = { onOpenDialog(UserTourDialog.PROGRAMAR_TOUR) }
        )
        StyledTourCardItem(
            title = "Información carreras UVG",
            subtitle = "Detalles de facultades y admisiones",
            onClick = { onOpenDialog(UserTourDialog.INFO_CARRERAS) }
        )
    }
}

@Composable
fun UserAssignedToursContent(tours: List<Tour>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F6F8))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Tours asignados",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (tours.isEmpty()) {
            Text(
                text = "Aún no tienes tours registrados.",
                fontSize = 14.sp,
                color = Color.Gray
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(tours) { tour ->
                    val verificado = tour.guia_necesitado == 0
                    AssignedCardItemView(
                        title = tour.tour_nombre,
                        dateInfo = "${tour.dia} • ${tour.lugar} • Guía: ${tour.persona_asignada}",
                        statusText = if (verificado) "Verificado" else "Pendiente de guía",
                        statusColor = if (verificado) Color(0xFF2D6A4F) else Color(0xFFD4A373)
                    )
                }
            }
        }
    }
}

@Composable
fun StyledTourCardItem(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun AssignedCardItemView(title: String, dateInfo: String, statusText: String, statusColor: Color) {
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
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = dateInfo, fontSize = 12.sp, color = Color.Gray)
            Surface(
                color = statusColor.copy(alpha = 0.15f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = statusText,
                    color = statusColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ConsultarToursDialog(
    tours: List<Tour>,
    registeredIds: Set<Int>,
    onRegister: (Tour) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tours Disponibles en Campus", fontWeight = FontWeight.Bold) },
        text = {
            if (tours.isEmpty()) {
                Text("No hay tours registrados en la base de datos.")
            } else {
                LazyColumn(
                    modifier = Modifier.heightIn(max = 360.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(tours) { tour ->
                        val yaInscrito = tour.tourId in registeredIds
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F8)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(tour.tour_nombre, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Text("${tour.dia} • ${tour.hora_inicial}:00 - ${tour.hora_final}:00", fontSize = 12.sp, color = Color.DarkGray)
                                Text("Lugar: ${tour.lugar} (${tour.personas_registradas} inscritos)", fontSize = 12.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { onRegister(tour) },
                                    enabled = !yaInscrito,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(if (yaInscrito) "Inscrito ✓" else "Inscribirme y agendar")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
private fun ProgramarTourDialog(
    onConfirm: (String, String, String, Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var lugar by remember { mutableStateOf("Edificio del CIT") }
    var dia by remember { mutableStateOf("Fri, Nov 20") }
    var horaIni by remember { mutableStateOf("10") }
    var horaFin by remember { mutableStateOf("12") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Programar Nuevo Tour", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del tour") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = lugar,
                    onValueChange = { lugar = it },
                    label = { Text("Punto de reunión") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dia,
                    onValueChange = { dia = it },
                    label = { Text("Fecha (ej. Fri, Nov 20)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = horaIni,
                        onValueChange = { horaIni = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Hora inicio (24h)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = horaFin,
                        onValueChange = { horaFin = it.filter { ch -> ch.isDigit() } },
                        label = { Text("Hora fin (24h)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B4332)),
                enabled = nombre.isNotBlank() && lugar.isNotBlank(),
                onClick = {
                    val ini = horaIni.toIntOrNull()?.coerceIn(7, 20) ?: 10
                    val fin = horaFin.toIntOrNull()?.coerceIn(ini + 1, 21) ?: (ini + 1)
                    onConfirm(nombre, lugar, dia, ini, fin)
                }
            ) {
                Text("Guardar Tour")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
private fun InfoCarrerasUvgDialog(onDismiss: () -> Unit) {
    val facultades = listOf(
        "Facultad de Ingeniería" to "Ciencias de la Computación, Ingeniería Industrial, Mecánica, Mecatrónica, Electrónica, Química y Civil.",
        "Facultad de Ciencias y Humanidades" to "Bioquímica y Microbiología, Biotecnología Molecular, Matemática Aplicada, Física y Química.",
        "Facultad de Ciencias Sociales" to "Psicología, Antropología, Arqueología y Relaciones Internacionales.",
        "Facultad de Educación" to "Profesorados y Licenciaturas en Educación y Gestión Educativa.",
        "Design, Innovation & Arts School" to "Arquitectura, Diseño de Producto e Innovación y Composición Musical."
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Facultades y Carreras UVG", fontWeight = FontWeight.Bold) },
        text = {
            LazyColumn(
                modifier = Modifier.heightIn(max = 360.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(facultades) { (facultad, descripcion) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F6F8)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(facultad, fontWeight = FontWeight.Bold, color = Color(0xFF1B4332), fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(descripcion, fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Entendido") }
        }
    )
}