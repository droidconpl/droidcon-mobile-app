package pl.droidcon.app.speakers

import pl.droidcon.app.data.Speaker

interface SpeakersView {

    fun display(speakers: List<Speaker>)

    fun display(speaker: Speaker)
}