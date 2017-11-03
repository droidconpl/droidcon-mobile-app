package pl.droidcon.app.agenda.interactor

import io.reactivex.Observable
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.mapper.AgendaMapper
import pl.droidcon.app.data.network.AgendaRemote
import pl.droidcon.app.data.network.AgendaService
import javax.inject.Inject

class RemoteAgendaSource @Inject constructor(private val agendaService: AgendaService,
                                             private val agendaMapper: AgendaMapper)
    : RemoteDataSource<List<AgendaRemote>> {

    override fun get(success: OnRemoteSuccess<List<AgendaRemote>>): Observable<List<AgendaRemote>> {
        return agendaService.agenda()
                .doOnNext {
                    println("NEXT $it")
                }
    }
}