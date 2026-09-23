package com.amaro.tecsupfit

data class ClaseGym(
    val id: String,
    val nombre: String,
    val horario: String,
    val sala: String,
    val duracion: String,
    val descripcion: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val esHoy: Boolean
)

data class Reserva(
    val id: String,
    val claseNombre: String,
    val horario: String,
    val estado: String
)

object MockData {
    val usuarioNombre = "Diego Ramos"
    val usuarioPlan = "Plan Premium"
    val clasesCompletadas = 14
    val rachaDias = 3

    val clasesDisponibles = listOf(
        ClaseGym(
            id = "1",
            nombre = "Yoga funcional",
            horario = "7:00 am",
            sala = "Sala 2",
            duracion = "50 min",
            descripcion = "Mejora tu flexibilidad y equilibrio con sesiones guiadas de bajo impacto.",
            cuposDisponibles = 5,
            cuposTotales = 10,
            esHoy = true
        ),
        ClaseGym(
            id = "2",
            nombre = "Cross Training",
            horario = "6:00 pm",
            sala = "Sala 1",
            duracion = "45 min",
            descripcion = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
            cuposDisponibles = 8,
            cuposTotales = 12,
            esHoy = true
        ),
        ClaseGym(
            id = "3",
            nombre = "Spinning",
            horario = "7:30 pm",
            sala = "Sala 3",
            duracion = "45 min",
            descripcion = "Cardio de alta resistencia en bicicleta estática con ritmo dinámico.",
            cuposDisponibles = 3,
            cuposTotales = 15,
            esHoy = false
        )
    )

    val misReservasIniciales = listOf(
        Reserva("101", "Cross Training", "Hoy, 6:00 pm", "Confirmada"),
        Reserva("102", "Yoga funcional", "Ayer, 7:00 am", "Completada")
    )
}