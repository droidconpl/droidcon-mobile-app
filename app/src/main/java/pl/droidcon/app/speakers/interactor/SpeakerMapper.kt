package pl.droidcon.app.speakers.interactor

import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.data.SpeakerData
import javax.inject.Inject

class SpeakerMapper @Inject constructor() {

    fun map(speakerData: SpeakerData): Speaker = Speaker(
            id = speakerData.id,
            firstName = speakerData.firstName,
            lastName = speakerData.lastName,
            title = speakerData.title,
            description = speakerData.description,
            websiteUrl = speakerData.websiteUrl,
            facebookUrl = speakerData.facebookUrl,
            twitterUrl = speakerData.twitterUrl,
            githubUrl = speakerData.githubUrl,
            linkedinUrl = speakerData.linkedinUrl,
            googlePlusUrl = speakerData.googlePlusUrl,
            imageUrl = speakerData.imageUrl
    )

    fun map(speaker: Speaker): SpeakerData = SpeakerData(
            id = speaker.id,
            firstName = speaker.firstName,
            lastName = speaker.lastName,
            title = speaker.title,
            description = speaker.description,
            websiteUrl = speaker.websiteUrl,
            facebookUrl = speaker.facebookUrl,
            twitterUrl = speaker.twitterUrl,
            githubUrl = speaker.githubUrl,
            linkedinUrl = speaker.linkedinUrl,
            googlePlusUrl = speaker.googlePlusUrl,
            imageUrl = speaker.imageUrl
    )
}