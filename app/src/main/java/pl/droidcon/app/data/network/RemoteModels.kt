package pl.droidcon.app.data.network

import com.google.gson.annotations.SerializedName

data class SpeakerRemote(
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

data class SessionRemote(
        @SerializedName("sessionId") val sessionId: Long,
        @SerializedName("sessionType") val sessionType: String,
        @SerializedName("sessionTitle") val sessionTitle: String,
        @SerializedName("sessionDescription") val sessionDescription: String,
        @SerializedName("speakerId") val speakerIds: List<Long>,
        @SerializedName("sessionLength") val sessionLength: Double,
        @SerializedName("workshopCapacity") val workshopCapacity: Int
)

data class AgendaRemote(
        @SerializedName("dayId") val dayId: Int,
        @SerializedName("slotId") val slotId: Int,
        @SerializedName("sessionType") val sessionType: String,
        @SerializedName("slotStart") val slotStart: String,
        @SerializedName("slotEnd") val slotEnd: String,
        @SerializedName("slotArray") val slotArray: SlotArrayRemote
)

data class SlotArrayRemote(
        @SerializedName("slotTitle") val slotTitle: String,
        @SerializedName("slotPicture") val slotPicture: String,
        @SerializedName("slotSpeaker") val slotSpeaker: List<Int>,
        @SerializedName("slotSession") val slotSession: Int
)