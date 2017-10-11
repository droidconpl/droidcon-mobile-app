package pl.droidcon.app.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = arrayOf(SpeakerLocal::class), version = 1)
abstract class DroidconDatabase : RoomDatabase() {

    abstract fun speakerDao(): SpeakersDao

    companion object {
        private lateinit var database: DroidconDatabase

        fun init(context: Context) {
            database = Room.databaseBuilder(context, DroidconDatabase::class.java, "droidcon")
                    .build()
        }

        fun get() = database
    }
}