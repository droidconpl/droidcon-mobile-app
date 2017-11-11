package pl.droidcon.app.speaker

import pl.droidcon.app.domain.Speaker

interface SpeakerView {

    fun displayDetails(speakerDetails: SpeakerDetails)
}