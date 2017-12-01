package pl.droidcon.app.sessions

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.data.local.FavoriteLocal
import pl.droidcon.app.domain.Session
import pl.droidcon.app.favorite.interactor.FavoriteRepository
import pl.droidcon.app.sessions.interactor.SessionsRepository

class SessionPresenter constructor(val sessionId: Long, val sessionsRepository: SessionsRepository, val favoriteRepository: FavoriteRepository) {

    private var view: SessionView? = null

    private val disposables = CompositeDisposable()

    fun attachView(sessionView: SessionView?) {

        this.view = sessionView

        if (view == null) {
            disposables.clear()
        } else {
            sessionsRepository
                    .get()
                    .flatMap { sessions: List<Session> -> Observable.fromIterable(sessions) }
                    .filter { session: Session -> session.sessionId == sessionId }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.display(it) }, { it.printStackTrace() })
                    .addTo(disposables)

            favoriteRepository
                    .getFavorite(sessionId)
                    .distinctUntilChanged()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { view?.setFavoriteSelected(false) }
                    .subscribe {
                        view?.setFavoriteSelected(it.isFavorite)
                    }
        }
    }

    fun setFavoriteSelected(selected: Boolean) {
        val favoriteLocal = FavoriteLocal(sessionId = sessionId)
        if (selected)
            favoriteRepository.put(favoriteLocal)
        else
            favoriteRepository.delete(favoriteLocal)
    }


}