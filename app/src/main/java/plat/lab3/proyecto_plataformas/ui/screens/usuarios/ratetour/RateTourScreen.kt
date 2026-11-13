package plat.lab3.proyecto_plataformas.ui.screens.usuarios.ratetour

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateTourScreen(
    viewModel: RateTourViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var dropdownExpanded by remember { mutableStateOf(false) }

    val greenDark = Color(0xFF1B4332)
    val greenMedium = Color(0xFF2D6A4F)

    LaunchedEffect(state.feedbackMessage) {
        state.feedbackMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.onEvent(RateTourEvent.ClearFeedback)
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
                title = { Text("UVG–Tours") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFFF4F6F8))
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                        .padding(20.dp)
                ) {
                    Text(
                        text = "TU OPINIÓN IMPORTA",
                        color = Color(0xFF95D5B2),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Califica tu tour",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Evalúa el recorrido y asigna una nota sobre 100 al guía.",
                        color = Color(0xFFD8F3DC),
                        fontSize = 13.sp
                    )
                }
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = greenDark)
                    }
                }
                state.errorMessage != null -> {
                    Text(text = state.errorMessage!!, color = Color.Red)
                }
                else -> {
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
                                text = "1. Selecciona el recorrido",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            ExposedDropdownMenuBox(
                                expanded = dropdownExpanded,
                                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                            ) {
                                OutlinedTextField(
                                    value = state.selectedTour?.let {
                                        "${it.tour_nombre} (${it.dia})"
                                    } ?: "No hay tours disponibles",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Tour realizado") },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                        .fillMaxWidth()
                                )

                                ExposedDropdownMenu(
                                    expanded = dropdownExpanded,
                                    onDismissRequest = { dropdownExpanded = false }
                                ) {
                                    state.availableTours.forEach { tour ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(
                                                        text = tour.tour_nombre,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = "${tour.dia} • Guía: ${tour.persona_asignada.ifBlank { "Por asignar" }}",
                                                        fontSize = 12.sp,
                                                        color = Color.Gray
                                                    )
                                                }
                                            },
                                            onClick = {
                                                viewModel.onEvent(RateTourEvent.SelectTour(tour))
                                                dropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            state.selectedTour?.let { tour ->
                                Surface(
                                    color = greenMedium.copy(alpha = 0.12f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "Guía que realizó el tour:",
                                            fontSize = 12.sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = tour.persona_asignada.ifBlank { "Por asignar" },
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = greenDark
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(color = Color(0xFFE2E8F0))

                            Text(
                                text = "2. Calificación al guía (0 a 100)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )

                            OutlinedTextField(
                                value = state.scoreInput,
                                onValueChange = { viewModel.onEvent(RateTourEvent.ScoreChanged(it)) },
                                label = { Text("Nota sobre 100 (ej. 95)") },
                                suffix = { Text("/ 100", fontWeight = FontWeight.Bold) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = state.commentInput,
                                onValueChange = { viewModel.onEvent(RateTourEvent.CommentChanged(it)) },
                                label = { Text("Comentarios sobre el guía o recorrido (opcional)") },
                                minLines = 2,
                                maxLines = 4,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )

                            Button(
                                onClick = { viewModel.onEvent(RateTourEvent.SubmitRating) },
                                enabled = !state.isSubmitting && state.selectedTour != null && state.scoreInput.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = greenDark),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                if (state.isSubmitting) {
                                    CircularProgressIndicator(
                                        color = Color.White,
                                        modifier = Modifier.size(22.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        text = "Enviar calificación",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}