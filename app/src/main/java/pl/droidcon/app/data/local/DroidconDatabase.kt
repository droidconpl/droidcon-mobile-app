package pl.droidcon.app.data.local

@android.arch.persistence.room.Database(entities = arrayOf(pl.droidcon.app.data.SpeakerData::class), version = 1)
abstract class DroidconDatabase : android.arch.persistence.room.RoomDatabase() {

    abstract fun speakerDao(): SpeakersDao

    companion object {

        private lateinit var database: pl.droidcon.app.data.local.DroidconDatabase

        fun init(context: android.content.Context) {
            pl.droidcon.app.data.local.DroidconDatabase.Companion.database = android.arch.persistence.room.Room.databaseBuilder(context, pl.droidcon.app.data.local.DroidconDatabase::class.java, "droidcon")
                    .build()
        }

        fun get() = pl.droidcon.app.data.local.DroidconDatabase.Companion.database
    }
}