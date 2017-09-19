package pl.droidcon.app.data

import com.google.gson.annotations.SerializedName

data class ApiSpeaker(
        @SerializedName("id") val id: Long,
        @SerializedName("firstName") val firstName: String,
        @SerializedName("lastName") val lastName: String,
        @SerializedName("websiteTitle") val title: String,
        @SerializedName("bio") val description: String,
        @SerializedName("websiteLink") val websiteUrl: String,
        @SerializedName("facebookLink") val facebookUrl: String,
        @SerializedName("twitterHandler") val twitterUrl: String,
        @SerializedName("githubLink") val githubUrl: String,
        @SerializedName("linkedIn") val linkedinUrl: String,
        @SerializedName("googlePlus") val googlePlusUrl: String,
        @SerializedName("imageUrl") val imageUrl: String
)