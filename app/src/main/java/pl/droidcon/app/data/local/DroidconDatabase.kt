package pl.droidcon.app.data.local

import android.arch.persistence.db.SupportSQLiteOpenHelper
import android.arch.persistence.room.*
import android.content.Context
import java.util.regex.Pattern

@Database(entities =
arrayOf(
        SpeakerLocal::class,
        SessionLocal::class,
        DayLocal::class,
        TalkPanelLocal::class,
        TalkLocal::class),
        version = 4)
@TypeConverters(Converters::class)
abstract class DroidconDatabase : RoomDatabase() {

    abstract fun speakerDao(): SpeakersDao

    abstract fun sessionDao(): SessionsDao

    abstract fun agendaDao(): AgendaDao

    companion object {
        private lateinit var database: DroidconDatabase

        fun init(context: Context) {
            database = Room.databaseBuilder(context, DroidconDatabase::class.java, "droidcon")
                    .fallbackToDestructiveMigration()
                    .build()
        }

        fun initInMemory(context: Context, factory: SupportSQLiteOpenHelper.Factory) {
            database = Room.inMemoryDatabaseBuilder(context, DroidconDatabase::class.java)
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
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
        if (longList.isEmpty()) {
            return emptyList()
        }
        return longList.split(regex = Pattern.compile(","))
                .map { it.toLong() }
    }
}
