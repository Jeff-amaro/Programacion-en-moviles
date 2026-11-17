
package com.amaro.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amaro.centrotareas.data.TareasRepositoryLocal
import com.amaro.centrotareas.ui.PantallaTareas
import com.amaro.centrotareas.viewmodel.TareasViewModel
import com.amaro.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF1565C0),
                    onPrimary = Color.White
                )
            ) {

                val tareasViewModel: TareasViewModel =
                    viewModel(
                        factory = TareasViewModelFactory(
                            TareasRepositoryLocal()
                        )
                    )

                PantallaTareas(
                    viewModel = tareasViewModel
                )
            }
        }
    }
}
