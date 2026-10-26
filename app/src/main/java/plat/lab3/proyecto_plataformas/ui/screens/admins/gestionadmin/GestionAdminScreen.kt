package plat.lab3.proyecto_plataformas.ui.screens.admins.gestionadmin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import plat.lab3.proyecto_plataformas.data.ejemplos.Admin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionAdminScreen(
    viewModel: GestionAdminViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 8.dp, start = 8.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Horas por Asignar",
                fontSize = 50.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 53.sp
            )

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
                    LazyColumn(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.adminData) { card ->
                            GestionAdminCardItem(card = card)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GestionAdminCardItem(card: Admin) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        modifier = Modifier
            .width(400.dp)
            .height(400.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Column {
                        Text("Tours semanales")
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                                    append("${card.tours_semanales}")
                                }
                                append(" programados")
                            }
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Column {
                        Text("Tours sin asignar")
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                                    append("${card.tours_noAsignados}")
                                }
                                append(" por asignar")
                            }
                        )
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Column {
                        Text("Pendiente de asignar")
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                                    append("${card.tours_completos_por_asignar}")
                                }
                                append(" completos por asignar")
                            }
                        )
                    }
                }
                Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                    Column {
                        Text("Guías")
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)) {
                                    append("${card.tours_semanales}")
                                }
                                append(" guías de tour")
                            }
                        )
                    }
                }
            }
        }
    }
}