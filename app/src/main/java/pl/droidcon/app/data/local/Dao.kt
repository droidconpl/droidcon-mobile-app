package pl.droidcon.app.data.local

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe
import pl.droidcon.app.data.SpeakerData
import pl.droidcon.app.data.SPEAKER_TABLE_NAME

@Dao
interface SpeakersDao {

    @Query("SELECT * from ${SPEAKER_TABLE_NAME}")
    fun get(): Maybe<List<SpeakerData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun put(speakerDatas: List<SpeakerData>)
}