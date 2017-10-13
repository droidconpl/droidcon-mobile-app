package pl.droidcon.app.sessions

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.sessions.interactor.SessionsRepository
import javax.inject.Inject

class SessionsPresenter @Inject constructor(private val sessionsRepository: SessionsRepository) {

    private var view: SessionsView? = null
    private val disposables = CompositeDisposable()

    fun attachView(sessionsView: SessionsView?) {
        this.view = sessionsView

        if (view == null) {
            disposables.clear()
        } else {
            sessionsRepository.sessions()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.displaySessions(it) }, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }
}