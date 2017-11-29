package pl.droidcon.app.speaker

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speakers.interactor.SpeakersRepository

class SpeakerPresenter(private val speakerId: Long,
                       private val speakersRepository: SpeakersRepository,
                       private val speakerDetailsMapper: SpeakerDetailsMapper) {

    private var view: SpeakerView? = null

    private val disposables = CompositeDisposable()

    fun attachView(speakerView: SpeakerView?) {
        this.view = speakerView

        if (view == null) {
            disposables.clear()
        } else {
            speakersRepository
                    .get()
                    .flatMap { speakers: List<Speaker> -> Observable.fromIterable(speakers) }
                    .filter { speaker: Speaker -> speaker.id == speakerId }
                    .map { speaker -> speakerDetailsMapper.map(speaker) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ view?.displayDetails(it) }, { it.printStackTrace() })
                    .addTo(disposables)
        }
    }
}