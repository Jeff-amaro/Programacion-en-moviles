package com.amaro.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PostsViewModelFactory(
    private val crear: () -> ViewModel
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        val viewModel = crear()

        if (modelClass.isInstance(viewModel)) {
            return viewModel as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
