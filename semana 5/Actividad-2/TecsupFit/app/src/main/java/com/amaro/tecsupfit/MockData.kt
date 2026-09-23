package com.amaro.tecsupfit

import androidx.compose.runtime.mutableStateListOf

object MockData {
    val usuarioNombre = "Jeff Amaro"
    val usuarioPlan = "Plan Premium"
    var clasesCompletadas = 14
    var rachaDias = 3

    val clasesDisponibles = mutableStateListOf(
        ClaseFit("1", "Yoga funcional", "7:00 am", "Sala 2", 5, 10, true, "Mejora tu flexibilidad y equilibrio con sesiones guiadas de bajo impacto."),
        ClaseFit("2", "Cross Training", "6:00 pm", "Sala 1", 8, 12, true, "Entrenamiento funcional de alta intensidad. Cupos limitados."),
        ClaseFit("3", "Spinning", "7:30 pm", "Sala 3", 3, 15, false, "Cardio de alta intensidad en bicicleta estática.")
    )

    val misReservasIniciales = mutableStateListOf(
        Reserva("1", "Cross Training", "Hoy, 6:00 pm", "Confirmada"),
        Reserva("2", "Yoga funcional", "Ayer, 7:00 am", "Completada")
    )

    fun agregarReserva(claseNombre: String, horario: String) {
        val yaExiste = misReservasIniciales.any { it.claseNombre == claseNombre && it.estado == "Confirmada" }
        if (!yaExiste) {
            misReservasIniciales.add(0, Reserva(System.currentTimeMillis().toString(), claseNombre, "Hoy, $horario", "Confirmada"))
            val claseIndex = clasesDisponibles.indexOfFirst { it.nombre == claseNombre }
            if (claseIndex != -1) {
                val clase = clasesDisponibles[claseIndex]
                if (clase.cuposDisponibles > 0) {
                    clasesDisponibles[claseIndex] = clase.copy(cuposDisponibles = clase.cuposDisponibles - 1)
                }
            }
        }
    }
}