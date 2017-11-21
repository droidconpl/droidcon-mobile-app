package pl.droidcon.app.data.mapper

import pl.droidcon.app.data.local.SessionLocal
import pl.droidcon.app.data.network.FirebaseSession
import pl.droidcon.app.data.network.SessionRemote
import pl.droidcon.app.domain.Session
import pl.droidcon.app.domain.SessionType
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class SessionsMapper @Inject constructor(private val sessionTypeMapper: SessionTypeMapper) {

    fun map(sessionRemote: SessionRemote, speakers: List<Speaker>): Session {
        return Session(
                sessionId = sessionRemote.sessionId,
                sessionType = sessionTypeMapper.map(sessionRemote.sessionType),
                sessionTitle = sessionRemote.sessionTitle,
                sessionDescription = sessionRemote.sessionDescription,
                sessionLength = "$sessionRemote.sessionLength",
                workshopCapacity = sessionRemote.workshopCapacity,
                speakers = sessionRemote.findSpeakers(speakers)
        )
    }

    fun map(firebaseSession: FirebaseSession, speakers: List<Speaker>): Session {
        return Session(
                sessionId = firebaseSession.id,
                sessionType = SessionType.TALK,
                sessionTitle = firebaseSession.title,
                sessionDescription = firebaseSession.description,
                sessionLength = firebaseSession.duration,
                workshopCapacity = 0,
                speakers = speakers.filter { speaker -> speaker.talkId == firebaseSession.id }
        )
    }

    fun map(sessionLocal: SessionLocal, speakers: List<Speaker>): Session {
        return Session(
                sessionId = sessionLocal.sessionId,
                sessionType = sessionTypeMapper.map(sessionLocal.sessionType),
                sessionTitle = sessionLocal.sessionTitle,
                sessionDescription = sessionLocal.sessionDescription,
                sessionLength = sessionLocal.sessionLength,
                workshopCapacity = sessionLocal.workshopCapacity,
                speakers = sessionLocal.findSpeakers(speakers)
        )
    }

    fun map(session: Session): SessionLocal {
        return SessionLocal(
                sessionId = session.sessionId,
                sessionType = sessionTypeMapper.map(session.sessionType),
                sessionTitle = session.sessionTitle,
                sessionDescription = session.sessionDescription,
                sessionLength = session.sessionLength,
                workshopCapacity = session.workshopCapacity,
                speakerIds = session.toSpeakersId()
        )
    }
}

private fun SessionRemote.findSpeakers(speakers: List<Speaker>) = speakers.filter { speakerIds.contains(it.id) }

private fun SessionLocal.findSpeakers(speakers: List<Speaker>) = speakers.filter { speakerIds.contains(it.id) }

private fun Session.toSpeakersId() = speakers.map { it.id }