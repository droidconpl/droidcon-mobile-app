package pl.droidcon.app.data.local

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

const val SPEAKER_TABLE_NAME = "speaker"

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