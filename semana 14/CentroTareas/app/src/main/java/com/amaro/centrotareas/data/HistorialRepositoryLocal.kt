package com.amaro.centrotareas.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

// MEJORA 5: DataStore para conservar los últimos 5 resultados.
// El delegado se declara una sola vez para evitar múltiples instancias.
private val Context.historialDataStore by preferencesDataStore(
    name = "historial_centrotareas"
)

data class RegistroHistorial(
    val tipo: String,
    val detalle: String,
    val fechaMillis: Long
)

class HistorialRepositoryLocal(context: Context) {

    private val appContext = context.applicationContext

    private val claveHistorial = stringPreferencesKey("ultimos_resultados")

    val historial: Flow<List<RegistroHistorial>> =
        appContext.historialDataStore.data
            .catch { error ->
                if (error is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw error
                }
            }
            .map { preferencias ->
                leerRegistros(preferencias[claveHistorial] ?: "[]")
            }

    suspend fun guardar(registro: RegistroHistorial) {
        appContext.historialDataStore.edit { preferencias ->
            val anteriores = leerRegistros(
                preferencias[claveHistorial] ?: "[]"
            )

            val ultimosCinco = (listOf(registro) + anteriores).take(5)
            preferencias[claveHistorial] = escribirRegistros(ultimosCinco)
        }
    }

    private fun leerRegistros(json: String): List<RegistroHistorial> {
        return try {
            val arreglo = JSONArray(json)
            buildList {
                for (i in 0 until arreglo.length()) {
                    val objeto = arreglo.getJSONObject(i)
                    add(
                        RegistroHistorial(
                            tipo = objeto.getString("tipo"),
                            detalle = objeto.getString("detalle"),
                            fechaMillis = objeto.getLong("fechaMillis")
                        )
                    )
                }
            }
        } catch (e: JSONException) {
            emptyList()
        }
    }

    private fun escribirRegistros(registros: List<RegistroHistorial>): String {
        val arreglo = JSONArray()
        registros.forEach { registro ->
            arreglo.put(
                JSONObject().apply {
                    put("tipo", registro.tipo)
                    put("detalle", registro.detalle)
                    put("fechaMillis", registro.fechaMillis)
                }
            )
        }
        return arreglo.toString()
    }
}
