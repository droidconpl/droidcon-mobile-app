package pl.droidcon.app.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val SPEAKER_TABLE_NAME = "speaker"

@Entity(tableName = SPEAKER_TABLE_NAME)
data class SpeakerData(
        @PrimaryKey @ColumnInfo(name = "id") @SerializedName("id") val id: Long,
        @ColumnInfo(name = "firstName") @SerializedName("firstName") val firstName: String,
        @ColumnInfo(name = "lastName") @SerializedName("lastName") val lastName: String,
        @ColumnInfo(name = "title") @SerializedName("websiteTitle") val title: String,
        @ColumnInfo(name = "description") @SerializedName("bio") val description: String,
        @ColumnInfo(name = "websiteUrl") @SerializedName("websiteLink") val websiteUrl: String,
        @ColumnInfo(name = "facebookUrl") @SerializedName("facebookLink") val facebookUrl: String,
        @ColumnInfo(name = "twitterUrl") @SerializedName("twitterHandler") val twitterUrl: String,
        @ColumnInfo(name = "githubUrl") @SerializedName("githubLink") val githubUrl: String,
        @ColumnInfo(name = "linkedinUrl") @SerializedName("linkedIn") val linkedinUrl: String,
        @ColumnInfo(name = "googlePlusUrl") @SerializedName("googlePlus") val googlePlusUrl: String,
        @ColumnInfo(name = "imageUrl") @SerializedName("imageUrl") val imageUrl: String
)
