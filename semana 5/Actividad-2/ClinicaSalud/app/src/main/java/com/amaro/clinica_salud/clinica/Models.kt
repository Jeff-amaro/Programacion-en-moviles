package com.amaro.clinica_salud.clinica

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: String = "",
    val resenas: String = "",
    val descripcion: String = "",
    val puntuacion: Double
)

data class Cita(
    val id: Int,
    val doctorNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String
)

val listaDoctores = listOf(
    Doctor(1, "Dra. Ana Torres", "Cardiología", "12 años exp.", "4.9 (128 reseñas)", "Especialista en arritmias e hipertensión, formación en la Clínica Mayo.", 4.9),
    Doctor(2, "Dr. Luis Vega", "Pediatría", "8 años exp.", "4.7 (95 reseñas)", "Especialista en pediatría general y neonatología.", 4.7),
    Doctor(3, "Dra. Rosa Díaz", "Dermatología", "10 años exp.", "4.8 (110 reseñas)", "Especialista en dermatología clínica y estética.", 4.8)
)