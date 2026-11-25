
package com.amaro.murocampus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        PostEntity::class,
        PaginaEntity::class,
        CambioPostEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MuroCampusDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {

        @Volatile
        private var INSTANCE: MuroCampusDatabase? = null

        fun obtenerDatabase(
            context: Context
        ): MuroCampusDatabase {

            return INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MuroCampusDatabase::class.java,
                    "murocampus_db"
                )
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}
