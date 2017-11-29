package pl.droidcon.app.sessions

import pl.droidcon.app.domain.Session

interface SessionView {

    fun display(session: Session)
}