package pl.droidcon.app.speakers

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import pl.droidcon.app.data.Speaker
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
        speakersRepository.speakers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ view?.display(it) }, { e -> e.printStackTrace() })
                .addTo(disposables)
    }
}