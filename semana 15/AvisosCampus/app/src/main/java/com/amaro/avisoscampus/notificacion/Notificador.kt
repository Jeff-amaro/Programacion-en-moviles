
package com.amaro.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.amaro.avisoscampus.model.Aviso
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.amaro.avisoscampus.R
import android.app.PendingIntent
import android.content.Intent
import com.amaro.avisoscampus.MainActivity
import com.amaro.avisoscampus.model.Origen

object Notificador {

    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {
        // TODO Paso 1: crear el canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val canal = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = context.getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(canal)

            // Mejora 1: canal de notificaciones urgentes
            val canalUrgente = NotificationChannel(
                CANAL_URGENTE,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            )

            manager.createNotificationChannel(canalUrgente)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: comprobar el permiso
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {
        // TODO Paso 3: construir y mostrar la notificación
        if (!tienePermiso(context)) {
            return false
        }

        val intent = Intent(context, MainActivity::class.java)

        val pendiente = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // Mejora 1: seleccionar canal según el origen
        val esUrgente = aviso.origen == Origen.RECORDATORIO

        val canalSeleccionado = if (esUrgente) {
            CANAL_URGENTE
        } else {
            CANAL
        }

        val prioridad = if (esUrgente) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        val notificacion = NotificationCompat.Builder(context, canalSeleccionado)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(prioridad)
            .setAutoCancel(true)
            .setContentIntent(pendiente)
            .build()

        NotificationManagerCompat.from(context)
            .notify(aviso.id, notificacion)

        // TODO Paso 4: agregar PendingIntent
        return true
    }
}
