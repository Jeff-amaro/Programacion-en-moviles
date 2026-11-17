
package com.amaro.campusGPS.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.ultimoLugarDataStore by preferencesDataStore(
    name = "campusgps_ajustes"
)

interface UltimoLugarRepository {

    val ultimoLugarId: Flow<Int?>

    suspend fun guardarUltimoLugar(id: Int)
}

class UltimoLugarRepositoryDataStore(
    context: Context
) : UltimoLugarRepository {

    private val dataStore =
        context.applicationContext.ultimoLugarDataStore

    private object Claves {
        val ULTIMO_LUGAR_ID =
            intPreferencesKey("ultimo_lugar_id")
    }

    override val ultimoLugarId: Flow<Int?> =
        dataStore.data
            .catch { error ->
                if (error is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw error
                }
            }
            .map { preferencias: Preferences ->
                preferencias[Claves.ULTIMO_LUGAR_ID]
            }

    override suspend fun guardarUltimoLugar(id: Int) {
        dataStore.edit { preferencias ->
            preferencias[Claves.ULTIMO_LUGAR_ID] = id
        }
    }
}
