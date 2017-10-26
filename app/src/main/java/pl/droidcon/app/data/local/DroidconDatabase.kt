package pl.droidcon.app.data.local

import android.arch.persistence.room.*
import android.content.Context
import java.util.regex.Pattern

@Database(entities = arrayOf(SpeakerLocal::class, SessionLocal::class), version = 1)
@TypeConverters(Converters::class)
abstract class DroidconDatabase : RoomDatabase() {

    abstract fun speakerDao(): SpeakersDao

    abstract fun sessionDao(): SessionsDao

    companion object {
        private lateinit var database: DroidconDatabase

        fun init(context: Context) {
            database = Room.databaseBuilder(context, DroidconDatabase::class.java, "droidcon")
                    .build()
        }

        fun get() = database
    }
}

@Suppress("UseExpressionBody")
class Converters {

    @TypeConverter
    fun fromLongList(longList: List<Long>): String {
        return longList.joinToString(separator = ",", transform = { it.toString() })
    }

    @TypeConverter
    fun fromStringToLongList(longList: String): List<Long> {
        return longList.split(regex = Pattern.compile(","))
                .map { it.toLong() }
    }
}
