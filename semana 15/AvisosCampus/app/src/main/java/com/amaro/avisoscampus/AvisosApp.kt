package com.amaro.avisoscampus

import android.app.Application
import com.amaro.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO Paso 1: crear el canal de notificación
        Notificador.crearCanal(this)
    }
}
