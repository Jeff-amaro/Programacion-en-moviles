
package com.amaro.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.amaro.centrotareas.data.TareasRepository

class TareasViewModelFactory(
    private val repository: TareasRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(
                TareasViewModel::class.java
            )
        ) {
            @Suppress("UNCHECKED_CAST")
            return TareasViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
