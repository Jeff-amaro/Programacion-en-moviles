package com.tecsup.productos.ui.lista

import com.tecsup.productos.data.repository.ProductoRepository
import com.tecsup.productos.domain.model.Producto
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.ui.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    private lateinit var repository: ProductoRepository
    private lateinit var notificador: Notificador
    private lateinit var viewModel: ListaViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = mockk()
        notificador = mockk(relaxed = true) // relaxed = responde sin errores a funciones Unit
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `al iniciar con exito, el estado es Exito y se muestra notificacion`() = runTest {

        val mockProductos = listOf(mockk<Producto>())
        every { repository.items } returns flowOf(mockProductos)
        coEvery { repository.refrescar() } returns Result.success(5)

        viewModel = ListaViewModel(repository, notificador)

        val currentState = viewModel.estado.value
        assertTrue(currentState is UiState.Exito)
        assertEquals(mockProductos, (currentState as UiState.Exito).datos)

        coVerify { notificador.mostrar("Datos actualizados", "Se guardaron 5 elementos") }
    }

    @Test
    fun `al fallar refresco sin datos locales, el estado es Error`() = runTest {
        every { repository.items } returns flowOf(emptyList())
        coEvery { repository.refrescar() } returns Result.failure(IOException())
        coEvery { repository.cantidadGuardada() } returns 0

        viewModel = ListaViewModel(repository, notificador)

        val currentState = viewModel.estado.value
        assertTrue(currentState is UiState.Error)
        assertEquals("No hay conexión a internet", (currentState as UiState.Error).mensaje)
    }

    @Test
    fun `alternarFavorito llama al repositorio correctamente`() = runTest {
        every { repository.items } returns flowOf(emptyList())
        coEvery { repository.refrescar() } returns Result.success(0)
        coEvery { repository.alternarFavorito(any()) } returns Unit

        viewModel = ListaViewModel(repository, notificador)

        viewModel.alternarFavorito(99)
        coVerify { repository.alternarFavorito(99) }
    }
}