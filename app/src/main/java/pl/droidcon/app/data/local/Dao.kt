package pl.droidcon.app.data.local

import android.arch.persistence.room.*
import io.reactivex.Maybe

@Dao
interface SpeakersDao {

    @Query("SELECT * FROM $SPEAKER_TABLE_NAME")
    fun get(): Maybe<List<SpeakerLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(speakers: List<SpeakerLocal>)

    @Query("DELETE FROM $SPEAKER_TABLE_NAME")
    fun clear()
}

@Dao
interface SessionsDao {
    @Query("SELECT * FROM $SESSION_TABLE_NAME")
    fun get(): Maybe<List<SessionLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(sessions: List<SessionLocal>)

    @Query("DELETE FROM $SESSION_TABLE_NAME")
    fun clear()
}

@Dao
abstract class AgendaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun putTalks(talkLocals: List<TalkLocal>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun putTalkPanels(talkPanelLocal: List<TalkPanelLocal>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun putDays(days: List<DayLocal>): List<Long>

    @Query("SELECT * FROM $TALK_TABLE_NAME")
    abstract fun getTalkLocal(): Maybe<List<TalkLocal>>

    @Query("SELECT * FROM $TALK_PANEL_TABLE_NAME")
    abstract fun getTalkPanelLocal(): Maybe<List<TalkPanelLocal>>

    @Query("SELECT * FROM $DAY_TABLE_NAME")
    abstract fun getDayLocal(): Maybe<List<DayLocal>>

    @Query("DELETE FROM $TALK_TABLE_NAME")
    abstract fun clearTalkLocal()

    @Query("DELETE FROM $TALK_PANEL_TABLE_NAME")
    abstract fun clearTalkPanelLocal()

    @Query("DELETE FROM $DAY_TABLE_NAME")
    abstract fun clearDayLocal()

    @Transaction
    open fun clear() {
        clearTalkLocal()
        clearTalkPanelLocal()
        clearDayLocal()
    }
}