package pl.droidcon.app.agenda

import pl.droidcon.app.domain.Agenda

interface AgendaView {
    fun display(agenda: Agenda)
}