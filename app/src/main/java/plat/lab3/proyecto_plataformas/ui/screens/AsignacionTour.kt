package plat.lab3.proyecto_plataformas.ui.screens

import android.R.attr.onClick
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

data class CuadroAsignacion(
    val horas:Double, val tour_nombre:String, val personas_registradas: Int, val dia:String,
    val persona_asignada:String, val guia_necesitado:Int, val lugar:String,
    val hora_inicial:Int, val hora_final:Int)

@Composable
fun TourFeed(card: CuadroAsignacion, modifier:Modifier=Modifier){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Asignate un Tour",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 56.sp
        )

        LazyColumn(modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .fillMaxWidth()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .width(400.dp)
                        .height(150.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Row(modifier = Modifier
                        .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(end=4.dp)){
                            Text(
                                text = "+${card.horas} horas de servicio",
                                color = Color.White
                            )
                        }
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(end=4.dp)){
                            Text(
                                text = "${card.personas_registradas} espacio disponible",
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = "${card.tour_nombre}",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = "${card.dia}",
                        modifier = Modifier.padding(4.dp)
                    )
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween){
                        Box(modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .wrapContentSize()
                            .background(MaterialTheme.colorScheme.secondary)
                            .align(Alignment.CenterVertically)){
                            if (card.guia_necesitado > 0){
                                Text(
                                    text = "${card.guia_necesitado} Guia Necesitado",
                                    color = Color.White
                                )
                            } else if(card.guia_necesitado == 0) {
                                Text(
                                    text = "${card.persona_asignada} Asignado",
                                    color = Color.White
                                )
                            }
                        }

                        Button(
                            modifier = Modifier
                                .wrapContentSize(),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                            onClick = {

                            }) {
                            Text(
                                text = "Claim Slot",
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TuTourMasReciente(card: CuadroAsignacion, modifier:Modifier=Modifier){
    var hora_inicial:String
    var hora_final:String

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier){
        Row(modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start){
            Icon(modifier = Modifier.size(25.dp).padding(horizontal = 3.dp, vertical = 2.dp),
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
            modifier = Modifier
                .size(width = 400.dp, height = 200.dp)
        ) {
            if (card.hora_inicial >= 12){
                hora_inicial = "${card.hora_inicial}:00 PM"
            }
            else {
                hora_inicial = "${card.hora_inicial}:00 AM"
            }

            if (card.hora_final >= 12){
                hora_final = "${card.hora_final}:00 PM"
            }
            else {
                hora_final = "${card.hora_final}:00 AM"
            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()){
                Text(
                    text = "${hora_inicial} - ${hora_final}",
                    fontSize = 11.sp
                )

                Box(modifier = Modifier.clip(shape = RoundedCornerShape(6.dp))){
                    Text("+${card.horas}")
                }
            }

            Column(modifier = Modifier){
                Text (
                    text = "Tour ${card.tour_nombre}",
                    fontSize = 35.sp
                )
                Row(modifier = Modifier.fillMaxWidth()){
                    Icon(modifier = Modifier.size(25.dp).padding(horizontal = 3.dp, vertical = 2.dp),
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null
                    )
                    Text(
                        text = "${card.lugar}",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Estudiantes(card: CuadroAsignacion, content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember {
        mutableStateOf(0)
    }


    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("UVG–Tours", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
                    HorizontalDivider()

                    NavigationDrawerItem(
                        label = { Text("Tours") },
                        selected = false,
                        onClick = { }
                    )
                    NavigationDrawerItem(
                        label = { Text("Estudiantes") },
                        selected = false,
                        onClick = { }
                    )

                    NavigationDrawerItem(
                        label = { Text("Administrador") },
                        selected = false,
                        onClick = { }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    title = { Text("UVG–Tours") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }
                        ) {
                            Icon(modifier = Modifier.fillMaxSize(0.8f),
                                painter = painterResource(id = R.drawable.icon_menu),
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top=8.dp, start = 8.dp, end = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally){
                TabRow(selectedTabIndex = selectedTab) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Tour Feed") })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("Your Tour") })
                }
                when(selectedTab){
                    0 -> TourFeed(card)
                    1 -> TuTourMasReciente(card)
                }
            }//
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AsignacionesPreview(){
    ProyectoplataformasTheme(darkTheme = false){
        Estudiantes(card = CuadroAsignacion(
            horas = 2.0,
            tour_nombre = "Tour del CIT",
            personas_registradas = 14,
            dia = "Fri, Oct 02",
            persona_asignada = "Iosef",
            guia_necesitado = 0,
            lugar = "Edificio del CIT",
            hora_inicial = 12,
            hora_final = 16
        )){

        }
    }
}