package com.tecsup.productos.notification

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificadorAndroid(
    private val context: Context
) : Notificador {

    companion object {
        const val CANAL_ID = "canal_productos"
        const val NOTIFICACION_ID = 1001
    }

    init {
        crearCanalNotificacion()
    }

    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombre = "Canal de Productos"
            val descripcionTexto = "Notificaciones de sincronización"
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(CANAL_ID, nombre, importancia).apply {
                description = descripcionTexto
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    @SuppressLint("MissingPermission")
    override fun mostrar(titulo: String, texto: String) {
        val builder = NotificationCompat.Builder(context, CANAL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICACION_ID, builder.build())
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}