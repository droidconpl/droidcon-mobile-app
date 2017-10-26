package pl.droidcon.app.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe

@Dao
interface SpeakersDao {

    @Query("SELECT * from $SPEAKER_TABLE_NAME")
    fun get(): Maybe<List<SpeakerLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(speakers: List<SpeakerLocal>)
}

@Dao
interface SessionsDao {
    @Query("SELECT * from $SESSION_TABLE_NAME")
    fun get(): Maybe<List<SessionLocal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(sessions: List<SessionLocal>)
}