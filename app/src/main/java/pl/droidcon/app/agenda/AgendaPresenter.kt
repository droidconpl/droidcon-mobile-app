package pl.droidcon.app.agenda

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.agenda.interactor.AgendaRepository
import pl.droidcon.app.sessions.interactor.SessionsRepository
import java.util.concurrent.TimeUnit
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
                    .subscribeOn(Schedulers.io())
                    .distinctUntilChanged()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.display(it) }, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }
}