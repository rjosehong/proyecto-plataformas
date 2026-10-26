package plat.lab3.proyecto_plataformas.ui.screens.usuarios.toursscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToursScreen(
    viewModel: ToursScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
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
                        0 -> UserToursHomeContent()
                        1 -> UserAssignedToursContent()
                    }
                }
            }
        }
    }
}

@Composable
fun UserToursHomeContent() {
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                color = Color.Black,
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Todos",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            Surface(
                color = Color(0xFFE2E8F0),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Disponibles",
                    color = Color.DarkGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        StyledTourCardItem(title = "Consulta sobre tours", subtitle = "Ver listado general de recorridos")
        StyledTourCardItem(title = "Programa un tour", subtitle = "Crear y agendar un nuevo espacio")
        StyledTourCardItem(title = "Información carreras UVG", subtitle = "Detalles de facultades y admisiones")
    }
}

@Composable
fun UserAssignedToursContent() {
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

        AssignedCardItemView(
            title = "Tour en el campus en familia",
            dateInfo = "Oct 18 • Aprobado por Rodrigo Navas",
            statusText = "Verificado",
            statusColor = Color(0xFF2D6A4F)
        )

        AssignedCardItemView(
            title = "Tours de laboratorios",
            dateInfo = "Oct 08 • Pendiente",
            statusText = "En revisión",
            statusColor = Color(0xFFD4A373)
        )
    }
}

@Composable
fun StyledTourCardItem(title: String, subtitle: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
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