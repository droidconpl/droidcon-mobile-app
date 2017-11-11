package pl.droidcon.app.domain

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Speaker(
        val id: Long,
        val firstName: String,
        val lastName: String,
        val title: String,
        val description: String,
        val websiteUrl: String,
        val facebookUrl: String,
        val twitterUrl: String,
        val githubUrl: String,
        val linkedinUrl: String,
        val googlePlusUrl: String,
        val imageUrl: String
) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Session(
        val sessionId: Long,
        val sessionType: SessionType,
        val sessionTitle: String,
        val sessionDescription: String,
        val speakers: List<Speaker>,
        val sessionLength: Double,
        val workshopCapacity: Int
) : Parcelable

enum class SessionType {
    TALK,
    WORKSHOP
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Agenda(val days: List<Day>) : Parcelable {
    companion object {
        val NULL_OBJECT = Agenda(emptyList())
    }
}

@SuppressLint("ParcelCreator")
@Parcelize
data class Day(val id: Long, val talkPanels: List<TalkPanel>) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class TalkPanel(val start: String, val end: String, val talks: List<Talk>) : Parcelable

@SuppressLint("ParcelCreator")
@Parcelize
data class Talk(val title: String, val speakers: List<Speaker>, val session: Session?) : Parcelable