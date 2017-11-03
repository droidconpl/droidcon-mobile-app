package pl.droidcon.app.agenda.interactor

import io.reactivex.Observable
import pl.droidcon.app.data.network.AgendaRemote
import javax.inject.Inject

class AgendaRepository @Inject constructor(private val remoteAgendaSource: RemoteAgendaSource) {

    fun get(): Observable<List<AgendaRemote>> {
        return remoteAgendaSource.get { }
    }
}