package pl.droidcon.app.domain

import android.os.Parcel
import android.os.Parcelable
// use: https://plugins.jetbrains.com/plugin/8086-parcelable-code-generator-for-kotlin-

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
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(firstName)
        writeString(lastName)
        writeString(title)
        writeString(description)
        writeString(websiteUrl)
        writeString(facebookUrl)
        writeString(twitterUrl)
        writeString(githubUrl)
        writeString(linkedinUrl)
        writeString(googlePlusUrl)
        writeString(imageUrl)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Speaker> = object : Parcelable.Creator<Speaker> {
            override fun createFromParcel(source: Parcel): Speaker = Speaker(source)
            override fun newArray(size: Int): Array<Speaker?> = arrayOfNulls(size)
        }
    }
}

data class Session(
        val sessionId: Long,
        val sessionType: SessionType,
        val sessionTitle: String,
        val sessionDescription: String,
        val speakers: List<Speaker>,
        val sessionLength: Double,
        val workshopCapacity: Int
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readLong(),
            SessionType.values()[source.readInt()],
            source.readString(),
            source.readString(),
            source.createTypedArrayList(Speaker.CREATOR),
            source.readDouble(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(sessionId)
        writeInt(sessionType.ordinal)
        writeString(sessionTitle)
        writeString(sessionDescription)
        writeTypedList(speakers)
        writeDouble(sessionLength)
        writeInt(workshopCapacity)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Session> = object : Parcelable.Creator<Session> {
            override fun createFromParcel(source: Parcel): Session = Session(source)
            override fun newArray(size: Int): Array<Session?> = arrayOfNulls(size)
        }
    }
}

enum class SessionType {
    TALK,
    WORKSHOP
}

data class Agenda(val days: List<Day>) : Parcelable {
    constructor(source: Parcel) : this(
            source.createTypedArrayList(Day.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(days)
    }

    companion object {
        val NULL_OBJECT = Agenda(emptyList())

        @JvmField
        val CREATOR: Parcelable.Creator<Agenda> = object : Parcelable.Creator<Agenda> {
            override fun createFromParcel(source: Parcel): Agenda = Agenda(source)
            override fun newArray(size: Int): Array<Agenda?> = arrayOfNulls(size)
        }
    }
}

data class Day(val id: Long, val talkPanels: List<TalkPanel>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readLong(),
            source.createTypedArrayList(TalkPanel.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeTypedList(talkPanels)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Day> = object : Parcelable.Creator<Day> {
            override fun createFromParcel(source: Parcel): Day = Day(source)
            override fun newArray(size: Int): Array<Day?> = arrayOfNulls(size)
        }
    }
}

data class TalkPanel(val start: String, val end: String, val talks: List<Talk>) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.createTypedArrayList(Talk.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(start)
        writeString(end)
        writeTypedList(talks)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<TalkPanel> = object : Parcelable.Creator<TalkPanel> {
            override fun createFromParcel(source: Parcel): TalkPanel = TalkPanel(source)
            override fun newArray(size: Int): Array<TalkPanel?> = arrayOfNulls(size)
        }
    }
}

data class Talk(val title: String, val speakers: List<Speaker>, val session: Session?) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Speaker.CREATOR),
            source.readParcelable<Session>(Session::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeTypedList(speakers)
        writeParcelable(session, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Talk> = object : Parcelable.Creator<Talk> {
            override fun createFromParcel(source: Parcel): Talk = Talk(source)
            override fun newArray(size: Int): Array<Talk?> = arrayOfNulls(size)
        }
    }
}