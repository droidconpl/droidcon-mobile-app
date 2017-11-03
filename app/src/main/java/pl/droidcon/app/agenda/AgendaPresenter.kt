package pl.droidcon.app.agenda

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import pl.droidcon.app.agenda.interactor.AgendaRepository
import javax.inject.Inject

class AgendaPresenter @Inject constructor(private val agendaRepository: AgendaRepository) {

    private var view: AgendaView? = null

    private val disposables = CompositeDisposable()

    fun attachView(agendaView: AgendaView?) {
        this.view = agendaView

        if (view == null) {
            disposables.clear()
        } else {
            agendaRepository.get()
                    .subscribe({}, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }
}