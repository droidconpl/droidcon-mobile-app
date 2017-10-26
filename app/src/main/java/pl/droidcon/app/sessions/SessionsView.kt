package pl.droidcon.app.sessions

import pl.droidcon.app.domain.Session

interface SessionsView {

    fun display(sessions: List<Session>)

    fun display(session: Session)
}