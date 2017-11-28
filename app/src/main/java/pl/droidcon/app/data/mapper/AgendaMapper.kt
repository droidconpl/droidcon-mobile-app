package pl.droidcon.app.data.mapper

import pl.droidcon.app.data.local.TalkLocal
import pl.droidcon.app.data.network.AgendaRemote
import pl.droidcon.app.data.network.FirebaseAgenda
import pl.droidcon.app.domain.*
import javax.inject.Inject

class AgendaMapper @Inject constructor() {

    fun map(agendaRemote: List<AgendaRemote>, sessions: List<Session>, speakers: List<Speaker>): Agenda {
        val groupedByDays = agendaRemote.groupBy { it.dayId }

        val days = groupedByDays.map {
            val dayId = it.key
            val agendaForDay = it.value

            val talkPanels = agendaForDay.map {
                val (_, _, sessionType, slotStart, slotEnd, slotArray) = it

                val talks = slotArray
                        .map {
                            Talk(
                                    title = it.slotTitle,
                                    speakers = speakers.findSpeakers(it.slotSpeaker),
                                    session = sessions.findSession(it.slotSession)
                            )
                        }
                TalkPanel(start = slotStart, end = slotEnd, talks = talks, sessionType = sessionType, text = "to_be_removed", imageUrl = "")
            }

            Day(dayId, talkPanels)
        }

        return Agenda(days)
    }

    fun map2(firebaseAgenda: List<FirebaseAgenda>, sessions: List<Session>, speakers: List<Speaker>): Agenda {

        val groupedByDays = firebaseAgenda.groupBy { it.dayid }

        val days = groupedByDays.map {
            val dayId = it.key
            val agendaForDay = it.value

            val talkPanels = agendaForDay.map { agenda ->
                val talks = mutableListOf<Talk>()

                if (agenda.session1id > 0) {
                    val session1 = sessions.findSession(agenda.session1id)
                    val session1speakers = speakers.findSpeakersByTalkId(agenda.session1id)

                    val talk1 = Talk(title = session1!!.sessionTitle, speakers = session1speakers, session = session1)
                    talks.add(talk1)
                }

                if (agenda.session2id > 0) {
                    val session2 = sessions.findSession(agenda.session2id)
                    val session2speakers = speakers.findSpeakersByTalkId(agenda.session2id)

                    val talk2 = Talk(title = session2!!.sessionTitle, speakers = session2speakers, session = session2)
                    talks.add(talk2)
                }

                if (agenda.session3id > 0) {
                    val session3 = sessions.findSession(agenda.session3id)
                    val session3speakers = speakers.findSpeakersByTalkId(agenda.session3id)

                    val talk3 = Talk(title = session3!!.sessionTitle, speakers = session3speakers, session = session3)
                    talks.add(talk3)
                }

                if (talks.isEmpty())
                    TalkPanel(start = agenda.starthour, end = agenda.endhour, talks = emptyList(), sessionType = "meta", text = agenda.text, imageUrl = agenda.imageurl)
                else
                    TalkPanel(start = agenda.starthour, end = agenda.endhour, talks = talks, sessionType = "talk", text = "", imageUrl = "")

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

private fun List<Speaker>.findSpeakersByTalkId(id: Long): List<Speaker> = filter { id == it.talkId }

private fun List<Session>.findSessionMaybe(id: Long?): Session {
    val sessions = this

    return id?.run {
        sessions.find { it.sessionId == this }
    }!!
}

private fun List<Session>.findSession(id: String): Session {
    val sessions = this

    return id.toLongOrNull()?.run {
        sessions.find { it.sessionId == this }
    }!!
}

private fun List<Session>.findSession(id: Long): Session? {
    val sessions = this

    return id.run {
        sessions.find { it.sessionId == this }
    }
}