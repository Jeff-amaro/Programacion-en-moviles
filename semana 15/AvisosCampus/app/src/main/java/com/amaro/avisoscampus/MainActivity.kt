
package com.amaro.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amaro.avisoscampus.data.AvisosRepositoryMemoria
import com.amaro.avisoscampus.ui.PantallaAvisos
import com.amaro.avisoscampus.ui.theme.AvisosCampusTheme
import com.amaro.avisoscampus.viewmodel.AvisosViewModel
import com.amaro.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {

    private val repository = AvisosRepositoryMemoria()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AvisosCampusTheme {

                val vm: AvisosViewModel = viewModel(
                    factory = AvisosViewModelFactory(repository)
                )

                PantallaAvisos(vm = vm)
            }
        }
    }
}
