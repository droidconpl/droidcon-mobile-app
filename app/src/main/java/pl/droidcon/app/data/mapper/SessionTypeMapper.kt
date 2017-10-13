package pl.droidcon.app.data.mapper

import pl.droidcon.app.domain.SessionType
import javax.inject.Inject

private const val TALK = "Talk"
private const val WORKSHOP = "Workshop"

class SessionTypeMapper @Inject constructor() {

    fun map(sessionType: String): SessionType = when (sessionType) {
        TALK -> SessionType.TALK
        WORKSHOP -> SessionType.WORKSHOP
        else -> throw IllegalStateException("Not expecting $sessionType as session type")
    }

    fun map(sessionType: SessionType): String = when (sessionType) {
        SessionType.TALK -> TALK
        SessionType.WORKSHOP -> WORKSHOP
    }
}