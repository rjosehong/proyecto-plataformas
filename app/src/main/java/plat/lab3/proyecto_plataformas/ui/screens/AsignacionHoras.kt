package plat.lab3.proyecto_plataformas.ui.screens

import android.graphics.Paint
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import plat.lab3.proyecto_plataformas.R
import plat.lab3.proyecto_plataformas.ui.theme.ProyectoplataformasTheme

data class CuadroHoras(
    val horas:Double, val tour_nombre:String, val Checked_In:Int, val tour_Slots:Int, val dia:String,
    val persona_asignada:String){

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsignacionHoras(card: CuadroHoras, content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

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
                        titleContentColor = Color.White,
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
                Text(
                    text = "Horas por Asignar",
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 53.sp
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
                            Column(modifier = Modifier){
                                Row(modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically){
                                    Row(verticalAlignment = Alignment.CenterVertically){
                                        Box(modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .wrapContentSize()
                                        ){
                                            Image(
                                                painter = painterResource(id = R.drawable.person),
                                                contentDescription = null,
                                                modifier = Modifier.size(45.dp).padding(start = 2.dp, end = 4.dp)
                                            )
                                        }
                                        Column(modifier = Modifier){
                                            Text(
                                                text = "${card.persona_asignada}",
                                                fontSize = 20.sp
                                            )

                                            Text(
                                                text = "${card.tour_nombre}",
                                                fontSize = 17.sp
                                            )
                                        }
                                    }
                                    Text(
                                        text = "+${card.horas}h",
                                        fontSize = 28.sp
                                    )

                                }

                                Row(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.SpaceBetween){
                                    Box(modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .wrapContentSize()
                                        .background(MaterialTheme.colorScheme.secondary)
                                        .align(Alignment.CenterVertically)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)){
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Image(
                                                painter = painterResource(id = R.drawable.calendar),
                                                contentDescription = null,
                                                modifier = Modifier.size(25.dp).padding(start = 2.dp, end = 4.dp)
                                            )

                                            Text(
                                                text = "${card.dia}",
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Box(modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .wrapContentSize()
                                        .background(MaterialTheme.colorScheme.tertiary)
                                        .align(Alignment.CenterVertically)){
                                        Row(verticalAlignment = Alignment.CenterVertically){
                                            Image(
                                                painter = painterResource(id = R.drawable.person_check),
                                                contentDescription = null,
                                                modifier = Modifier.size(25.dp).padding(start = 2.dp, end = 4.dp)
                                            )

                                            Text(
                                                text = "${card.Checked_In}/${card.tour_Slots} Checked In",
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                                Row(modifier = Modifier.fillMaxWidth().padding(start=4.dp,end=4.dp)){
                                    Button(
                                        modifier = Modifier.weight(2f),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        onClick = {

                                        }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.check_circle),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp).padding(start = 2.dp, end = 4.dp)
                                        )

                                        Text(
                                            text = "Aprobar +${card.horas}h",
                                            fontSize = 18.sp
                                        )
                                    }

                                    Button(
                                        modifier = Modifier.weight(1f),
                                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                        onClick = {

                                        }) {
                                        Image(
                                            painter = painterResource(id = R.drawable.review_icon),
                                            contentDescription = null,
                                            modifier = Modifier.size(25.dp).padding(start = 2.dp, end = 4.dp)
                                        )

                                        Text(
                                            text = "Review",
                                            fontSize = 18.sp
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
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun AsigHorasPreview(){
    ProyectoplataformasTheme(darkTheme = false){
        AsignacionHoras(card = CuadroHoras(
            horas = 3.0,
            tour_nombre = "Tour del CIT",
            Checked_In = 14,
            tour_Slots = 16,
            dia = "Fri, Oct 02",
            persona_asignada = "Francisco Ordóñez"
        )){

        }
    }
}