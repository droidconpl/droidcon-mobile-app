package pl.droidcon.app.data.local

import android.arch.persistence.room.*
import io.reactivex.Maybe

@Dao
interface SpeakersDao {

    @Query("SELECT * from $SPEAKER_TABLE_NAME")
    fun get(): Maybe<List<SpeakerLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(speakers: List<SpeakerLocal>)

    @Query("DELETE FROM $SPEAKER_TABLE_NAME")
    fun clear()
}

@Dao
interface SessionsDao {
    @Query("SELECT * from $SESSION_TABLE_NAME")
    fun get(): Maybe<List<SessionLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(sessions: List<SessionLocal>)

    @Query("DELETE FROM $SESSION_TABLE_NAME")
    fun clear()
}