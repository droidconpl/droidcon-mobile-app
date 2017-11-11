package pl.droidcon.app.speaker

import pl.droidcon.app.domain.Speaker

class SpeakerPresenter(private val speaker: Speaker,
                       private val speakerDetailsMapper: SpeakerDetailsMapper) {

    private var view: SpeakerView? = null

    fun attachView(speakerView: SpeakerView?) {
        this.view = speakerView

        view?.displayDetails(speakerDetailsMapper.map(speaker))
    }
}