package com.tecsup.cafecampus.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.tecsup.cafecampus.R
import com.tecsup.cafecampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AjustesScreen(
    sesionViewModel: SesionViewModel,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onIrAPanel: () -> Unit,
    onIrAPedidos: () -> Unit,
    onIrAMenu: () -> Unit,
    onVolver: () -> Unit
) {
    val modoOscuro by sesionViewModel.modoOscuro.collectAsState()
    val notificaciones by sesionViewModel.notificaciones.collectAsState()
    val recibirPedidos by sesionViewModel.recibirPedidos.collectAsState()
    val perfilActivo by sesionViewModel.perfilActivo.collectAsState()

    val esAdmin = perfilActivo == "ADMIN"
    val nombreUsuario = if (esAdmin) "Administrador" else "Juan Pérez"
    val correoUsuario = if (esAdmin) "admin@cafe.test" else "juan.perez@tecsup.edu.pe"
    val avatarRes = if (esAdmin) R.drawable.imagen_perfil_admin else R.drawable.imagen_perfil_estudiante

    val colorPurpura = Color(0xFF4F46E5)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Ajustes",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPanel,
                    icon = { Icon(Icons.Default.BarChart, contentDescription = "Panel", tint = Color.Gray) },
                    label = { Text("Panel", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAPedidos,
                    icon = { Icon(Icons.Default.Menu, contentDescription = "Pedidos", tint = Color.Gray) },
                    label = { Text("Pedidos", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onIrAMenu,
                    icon = { Icon(Icons.Default.LocalCafe, contentDescription = "Menú", tint = Color.Gray) },
                    label = { Text("Menú", color = Color.Gray) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = colorPurpura) },
                    label = { Text("Ajustes", color = colorPurpura, fontWeight = FontWeight.Bold) }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Image(
                                painter = painterResource(id = avatarRes),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Column {
                                Text(
                                    text = nombreUsuario,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = correoUsuario,
                                    fontSize = 13.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    color = if (esAdmin) Color(0xFFE0E7FF) else Color(0xFFDCFCE7),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text(
                                        text = if (esAdmin) "Perfil Administrador" else "Perfil Estudiante",
                                        color = if (esAdmin) Color(0xFF2563EB) else Color(0xFF16A34A),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val nuevoPerfil = if (esAdmin) "CLIENTE" else "ADMIN"
                                sesionViewModel.cambiarPerfil(nuevoPerfil)
                                onCambiarPerfil()
                            },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colorPurpura)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Cambiar de perfil",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (esAdmin) "Modo práctica: ir a vista Estudiante" else "Modo práctica: ir a vista Administrador",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Preferencias",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Modo oscuro", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Cambia el tema de la app", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                                Switch(
                                    checked = modoOscuro,
                                    onCheckedChange = { sesionViewModel.setModoOscuro(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = colorPurpura
                                    )
                                )
                            }

                            HorizontalDivider(color = Color(0xFFF3F4F6))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Notificaciones", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Avisar nuevos pedidos", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                                Switch(
                                    checked = notificaciones,
                                    onCheckedChange = { sesionViewModel.setNotificaciones(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = colorPurpura
                                    )
                                )
                            }

                            HorizontalDivider(color = Color(0xFFF3F4F6))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Recibir pedidos", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                    Text("Abrir o cerrar la cafetería", fontSize = 12.sp, color = Color(0xFF9CA3AF))
                                }
                                Switch(
                                    checked = recibirPedidos,
                                    onCheckedChange = { sesionViewModel.setRecibirPedidos(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = colorPurpura
                                    )
                                )
                            }
                        }
                    }
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                        border = BorderStroke(0.dp, Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Se guardan en el dispositivo",
                                fontSize = 12.sp,
                                color = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "con DataStore",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorPurpura
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        sesionViewModel.cerrarSesion()
                        onCerrarSesion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    border = BorderStroke(1.dp, Color(0xFFDC2626)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Cerrar sesión",
                        color = Color(0xFFDC2626),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}