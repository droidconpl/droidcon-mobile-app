package pl.droidcon.app.agenda.interactor

import pl.droidcon.app.ApplicationScope
import pl.droidcon.app.DataRepository
import pl.droidcon.app.Repository
import pl.droidcon.app.domain.Agenda
import javax.inject.Inject

@ApplicationScope
class AgendaRepository @Inject constructor(private val remoteAgendaSource: FirebaseAgendaSource /* RemoteAgendaSource */,
                                           private val localAgendaSource: LocalAgendaSource)
    : DataRepository<Agenda> by Repository(remoteAgendaSource, localAgendaSource, Agenda.NULL_OBJECT)