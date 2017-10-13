package pl.droidcon.app.sessions

import pl.droidcon.app.domain.Session

interface SessionsView {

    fun displaySessions(sessions: List<Session>)
}