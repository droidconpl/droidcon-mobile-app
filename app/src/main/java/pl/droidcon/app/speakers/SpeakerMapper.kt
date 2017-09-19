package pl.droidcon.app.speakers

import pl.droidcon.app.data.ApiSpeaker
import pl.droidcon.app.data.Speaker
import javax.inject.Inject

class SpeakerMapper @Inject constructor() {

    fun map(apiSpeaker: ApiSpeaker): Speaker = Speaker(
            id = apiSpeaker.id,
            firstName = apiSpeaker.firstName,
            lastName = apiSpeaker.lastName,
            title = apiSpeaker.title,
            description = apiSpeaker.description,
            websiteUrl = apiSpeaker.websiteUrl,
            facebookUrl = apiSpeaker.facebookUrl,
            twitterUrl = apiSpeaker.twitterUrl,
            githubUrl = apiSpeaker.githubUrl,
            linkedinUrl = apiSpeaker.linkedinUrl,
            googlePlusUrl = apiSpeaker.googlePlusUrl,
            imageUrl = apiSpeaker.imageUrl
    )
}