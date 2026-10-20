package com.tecsup.cafecampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class PreferenciasStore(private val context: Context) {

    companion object {
        val PERFIL_KEY = stringPreferencesKey("perfil_activo")
        val MODO_OSCURO_KEY = booleanPreferencesKey("modo_oscuro")
        val NOTIFICACIONES_KEY = booleanPreferencesKey("notificaciones")
        val RECIBIR_PEDIDOS_KEY = booleanPreferencesKey("recibir_pedidos")
    }

    val perfilActivo: Flow<String?> = context.dataStore.data.map { it[PERFIL_KEY] ?: "ADMIN" }
    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[MODO_OSCURO_KEY] ?: false }
    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[NOTIFICACIONES_KEY] ?: true }
    val recibirPedidos: Flow<Boolean> = context.dataStore.data.map { it[RECIBIR_PEDIDOS_KEY] ?: true }

    suspend fun guardarPerfilActivo(perfil: String) {
        context.dataStore.edit { it[PERFIL_KEY] = perfil }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[MODO_OSCURO_KEY] = activo }
    }

    suspend fun guardarNotificaciones(activo: Boolean) {
        context.dataStore.edit { it[NOTIFICACIONES_KEY] = activo }
    }

    suspend fun guardarRecibirPedidos(activo: Boolean) {
        context.dataStore.edit { it[RECIBIR_PEDIDOS_KEY] = activo }
    }

    suspend fun limpiarSesion() {
        context.dataStore.edit {
            it.remove(PERFIL_KEY)
        }
    }
}