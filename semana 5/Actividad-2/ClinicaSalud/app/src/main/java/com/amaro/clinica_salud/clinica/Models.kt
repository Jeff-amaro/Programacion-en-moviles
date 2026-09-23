package com.amaro.clinica_salud.clinica

data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val descripcion: String
)

data class Cita(
    val id: Int,
    val doctorNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String
)

object ClinicaData {
    val doctores = listOf(
        Doctor(1, "Dra. Ana Torres", "Cardiología", 4.9, "Especialista en arritmias e hipertensión."),
        Doctor(2, "Dr. Luis Vega", "Pediatría", 4.7, "Especialista en desarrollo infantil."),
        Doctor(3, "Dra. Rosa Díaz", "Dermatología", 4.8, "Experta en dermatología clínica.")
    )

    val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")
}