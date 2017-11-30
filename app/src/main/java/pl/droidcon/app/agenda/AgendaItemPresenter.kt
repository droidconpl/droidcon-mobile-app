package pl.droidcon.app.agenda

import android.util.Log
import android.widget.ImageView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.agenda.interactor.AgendaRepository
import pl.droidcon.app.domain.Agenda
import pl.droidcon.app.domain.Session
import javax.inject.Inject

class AgendaItemPresenter @Inject constructor(private val agendaRepository: AgendaRepository) {

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
                    .flatMap { agenda ->
                        Log.d("DDD", "MRR " + agenda.days.size)
                        Observable.fromIterable(agenda.days)
                    }
                    .filter { day ->
                        Log.d("DDD", "DAYYS  " + day.talkPanels.size +  "  id: " + day.id)
                        day.id == dayId.toLong() + 1
                    }
//                    .map { agenda -> agenda.days[dayId].talkPanels }
                    .map { day ->
                        Log.d("DDD", "HMM " + day.id + " " + day.talkPanels.size)
                        day.talkPanels
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.display(it) }, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }

    fun openSession(speakerPicture: ImageView, session: Session) {
        view?.openSession(speakerPicture, session)
    }
}