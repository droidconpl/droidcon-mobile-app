package pl.droidcon.app.speakers

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import javax.inject.Inject

class SpeakersPresenter @Inject constructor(private val speakersRepository: SpeakersRepository) {

    private var view: SpeakersView? = null
    private val disposables = CompositeDisposable()

    fun attachView(view: SpeakersView?) {
        this.view = view

        if (view != null) {
            loadSpeakers()
        } else {
            disposables.clear()
        }
    }

    fun onSpeakerSelected(speaker: Speaker) {
        view?.display(speaker)
    }

    private fun loadSpeakers() {
        speakersRepository.get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.display(it) }, { e -> e.printStackTrace() })
                .addTo(disposables)
    }
}