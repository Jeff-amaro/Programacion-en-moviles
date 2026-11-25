package com.amaro.avisoscampus.notificacion

import android.content.Context
import com.amaro.avisoscampus.model.Aviso

object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: crear el canal de notificación
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: comprobar el permiso
        return false
    }

    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: construir y mostrar la notificación
        // TODO Paso 4: agregar PendingIntent
        return false
    }
}
