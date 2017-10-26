package pl.droidcon.app.data.mapper

import pl.droidcon.app.data.local.SpeakerLocal
import pl.droidcon.app.data.network.SpeakerRemote
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

private const val BASE_IMAGE_URL = "https://raw.githubusercontent.com/droidconpl/droidcon-2016-web/master/assets/photos/speakers/"

class SpeakerMapper @Inject constructor() {

    fun map(speakerRemote: SpeakerRemote): Speaker = Speaker(
            id = speakerRemote.id,
            firstName = speakerRemote.firstName,
            lastName = speakerRemote.lastName,
            title = speakerRemote.title,
            description = speakerRemote.description,
            websiteUrl = speakerRemote.websiteUrl,
            facebookUrl = speakerRemote.facebookUrl,
            twitterUrl = speakerRemote.twitterUrl,
            githubUrl = speakerRemote.githubUrl,
            linkedinUrl = speakerRemote.linkedinUrl,
            googlePlusUrl = speakerRemote.googlePlusUrl,
            imageUrl = "$BASE_IMAGE_URL${speakerRemote.imageUrl}"
    )

    fun map(speakerLocal: SpeakerLocal): Speaker = Speaker(
            id = speakerLocal.id,
            firstName = speakerLocal.firstName,
            lastName = speakerLocal.lastName,
            title = speakerLocal.title,
            description = speakerLocal.description,
            websiteUrl = speakerLocal.websiteUrl,
            facebookUrl = speakerLocal.facebookUrl,
            twitterUrl = speakerLocal.twitterUrl,
            githubUrl = speakerLocal.githubUrl,
            linkedinUrl = speakerLocal.linkedinUrl,
            googlePlusUrl = speakerLocal.googlePlusUrl,
            imageUrl = speakerLocal.imageUrl
    )

    fun map(speaker: Speaker): SpeakerLocal = SpeakerLocal(
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