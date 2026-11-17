
package com.amaro.campusGPS

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amaro.campusGPS.data.LugaresRepositoryLocal
import com.amaro.campusGPS.data.UbicacionRepositoryGoogle
import com.amaro.campusGPS.data.UltimoLugarRepositoryDataStore
import com.amaro.campusGPS.ui.PantallaMapa
import com.amaro.campusGPS.ui.theme.CampusGPSTheme
import com.amaro.campusGPS.viewmodel.CampusViewModelFactory
import com.amaro.campusGPS.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val factory = CampusViewModelFactory(
            ubicacionRepo = UbicacionRepositoryGoogle(applicationContext),
            lugaresRepo = LugaresRepositoryLocal(),
            ultimoLugarRepo = UltimoLugarRepositoryDataStore(applicationContext)
        )

        setContent {
            CampusGPSTheme {

                val vm: MapaViewModel = viewModel(
                    factory = factory
                )

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    PantallaMapa(vm = vm)
                }
            }
        }
    }
}
