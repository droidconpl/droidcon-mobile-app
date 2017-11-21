package pl.droidcon.app.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val SPEAKER_TABLE_NAME = "speaker"
const val SESSION_TABLE_NAME = "session"
const val DAY_TABLE_NAME = "day"
const val TALK_PANEL_TABLE_NAME = "talk_panel"
const val TALK_TABLE_NAME = "talk"

@Entity(tableName = SPEAKER_TABLE_NAME)
data class SpeakerLocal(
        @PrimaryKey @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "firstName") val firstName: String,
        @ColumnInfo(name = "lastName") val lastName: String,
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "description") val description: String,
        @ColumnInfo(name = "websiteUrl") val websiteUrl: String,
        @ColumnInfo(name = "facebookUrl") val facebookUrl: String,
        @ColumnInfo(name = "twitterUrl") val twitterUrl: String,
        @ColumnInfo(name = "githubUrl") val githubUrl: String,
        @ColumnInfo(name = "linkedinUrl") val linkedinUrl: String,
        @ColumnInfo(name = "googlePlusUrl") val googlePlusUrl: String,
        @ColumnInfo(name = "imageUrl") val imageUrl: String
)

@Entity(tableName = SESSION_TABLE_NAME)
data class SessionLocal(
        @PrimaryKey @ColumnInfo(name = "sessionId") val sessionId: Long,
        @ColumnInfo(name = "sessionType") val sessionType: String,
        @ColumnInfo(name = "sessionTitle") val sessionTitle: String,
        @ColumnInfo(name = "sessionDescription") val sessionDescription: String,
        @ColumnInfo(name = "speakerId") val speakerIds: List<Long>,
        @ColumnInfo(name = "sessionLength") val sessionLength: String,
        @ColumnInfo(name = "workshopCapacity") val workshopCapacity: Int
)

@Entity(tableName = DAY_TABLE_NAME)
data class DayLocal(
        @PrimaryKey @ColumnInfo(name = "id") val id: Long,
        @ColumnInfo(name = "talkPanels") val talks: List<Long>
)

@Entity(tableName = TALK_PANEL_TABLE_NAME)
data class TalkPanelLocal(
        @ColumnInfo(name = "start") val start: String,
        @ColumnInfo(name = "end") val end: String,
        @ColumnInfo(name = "talks") val talks: List<Long>,
        @ColumnInfo(name = "sessionType") val sessionType: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}


@Entity(tableName = TALK_TABLE_NAME)
data class TalkLocal(
        @ColumnInfo(name = "title") val title: String,
        @ColumnInfo(name = "speakerId") val speakerIds: List<Long>,
        @ColumnInfo(name = "sessionId") val sessionId: Long?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}