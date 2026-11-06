package com.tecsup.productos.ui.admin

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: AdminViewModel = viewModel(factory = AdminViewModel.Factory),
    onVerCatalogo: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val totalGuardados by viewModel.totalGuardados.collectAsState()
    val totalFavoritos by viewModel.totalFavoritos.collectAsState()


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {  }

    LaunchedEffect(Unit) {
        viewModel.actualizarTotales()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Panel de Administrador") },
                actions = {
                    Button(onClick = {
                        viewModel.cerrarSesion()
                        onCerrarSesion()
                    }) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Total Guardados: $totalGuardados", style = MaterialTheme.typography.titleLarge)
                    Text(text = "Total Favoritos: $totalFavoritos", style = MaterialTheme.typography.titleLarge)
                }
            }

            Button(
                onClick = { viewModel.sincronizar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sincronizar (Descargar de Internet)")
            }

            Button(
                onClick = { viewModel.limpiarCache() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Limpiar Caché Local")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onVerCatalogo,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Ver Catálogo como Usuario")
            }
        }
    }
}