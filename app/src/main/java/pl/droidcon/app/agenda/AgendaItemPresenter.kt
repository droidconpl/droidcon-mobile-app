package pl.droidcon.app.agenda

import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.agenda.interactor.AgendaRepository
import pl.droidcon.app.data.local.FavoriteLocal
import pl.droidcon.app.domain.Agenda
import pl.droidcon.app.domain.Session
import pl.droidcon.app.favorite.interactor.FavoriteRepository
import javax.inject.Inject

class AgendaItemPresenter @Inject constructor(
        private val agendaRepository: AgendaRepository,
        private val favoriteRepository: FavoriteRepository

) {

    private var view: AgendaItemView? = null

    private val disposables = CompositeDisposable()

    fun attachView(agendaItemView: AgendaItemView?, dayId: Int) {
        this.view = agendaItemView

        if (view == null) {
            disposables.clear()
        } else {
            agendaRepository.get()
                    .subscribeOn(Schedulers.io())
                    .distinctUntilChanged()
                    .filter { agenda: Agenda -> agenda.days.isNotEmpty() }
                    .map { agenda -> agenda.days[dayId].talkPanels }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.display(it) }, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }

    fun openSession(speakerPicture: ImageView, session: Session) {
        view?.openSession(speakerPicture, session)
    }

    fun observeFavorite(sessionId: Long): Observable<Boolean> {
        return favoriteRepository.getFavorite(sessionId).map { favorite: FavoriteLocal -> favorite.isFavorite }
    }
}