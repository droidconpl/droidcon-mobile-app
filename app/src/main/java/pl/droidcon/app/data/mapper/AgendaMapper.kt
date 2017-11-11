package pl.droidcon.app.data.mapper

import pl.droidcon.app.data.local.TalkLocal
import pl.droidcon.app.data.network.AgendaRemote
import pl.droidcon.app.domain.*
import javax.inject.Inject

class AgendaMapper @Inject constructor() {

    fun map(agendaRemote: List<AgendaRemote>, sessions: List<Session>, speakers: List<Speaker>): Agenda {
        val groupedByDays = agendaRemote.groupBy { it.dayId }

        val days = groupedByDays.map {
            val dayId = it.key
            val agendaForDay = it.value

            val talkPanels = agendaForDay.map {
                val (_, _, _, slotStart, slotEnd, slotArray) = it

                val talks = slotArray
                        .map {
                            Talk(
                                    title = it.slotTitle,
                                    speakers = speakers.findSpeakers(it.slotSpeaker),
                                    session = sessions.findSession(it.slotSession)
                            )
                        }
                TalkPanel(start = slotStart, end = slotEnd, talks = talks)
            }

            Day(dayId, talkPanels)
        }

        return Agenda(days)
    }

    fun map(talk: Talk): TalkLocal {
        return TalkLocal(
                talk.title,
                talk.speakers.toIds(),
                talk.session?.sessionId
        )
    }

    fun map(talkLocal: TalkLocal, speakers: List<Speaker>, sessions: List<Session>): Talk {
        return Talk(
                title = talkLocal.title,
                speakers = speakers.findSpeakers(talkLocal.speakerIds),
                session = sessions.findSessionMaybe(talkLocal.sessionId)
        )
    }
}

private fun List<Speaker>.toIds(): List<Long> = map { it.id }

private fun List<Speaker>.findSpeakers(ids: List<Long>): List<Speaker> = filter { ids.contains(it.id) }

private fun List<Session>.findSessionMaybe(id: Long?): Session? {
    val sessions = this

    return id?.run {
        sessions.find { it.sessionId == this }
    }
}

private fun List<Session>.findSession(id: String): Session? {
    val sessions = this

    return id.toLongOrNull()?.run {
        sessions.find { it.sessionId == this }
    }
}