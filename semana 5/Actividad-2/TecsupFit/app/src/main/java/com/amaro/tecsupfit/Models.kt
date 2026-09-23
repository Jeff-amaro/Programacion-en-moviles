package com.amaro.tecsupfit

data class ClaseFit(
    val id: String,
    val nombre: String,
    val horario: String,
    val sala: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int,
    val esHoy: Boolean,
    val descripcion: String
)

data class Reserva(
    val id: String,
    val claseNombre: String,
    val horario: String,
    val estado: String
)