package pl.droidcon.app.speakers

import pl.droidcon.app.domain.Speaker

interface SpeakersView {

    fun display(speakers: List<Speaker>)

    fun display(speaker: Speaker)
}