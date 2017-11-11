package pl.droidcon.app.speaker

import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class SpeakerDetailsMapper @Inject constructor() {

    fun map(speaker: Speaker): SpeakerDetails {
        return speaker.run {
            SpeakerDetails(
                    name = "$firstName $lastName",
                    title = title,
                    description = description,
                    photoUrl = imageUrl,
                    socials = socials())
        }
    }

    private fun Speaker.socials(): List<Social> {
        return mutableListOf<Social>()
                .apply {
                    addIfNotEmpty(websiteUrl) { Social.Website(it) }
                    addIfNotEmpty(facebookUrl) { Social.Facebook(it) }
                    addIfNotEmpty(twitterUrl) { Social.Twitter(it) }
                    addIfNotEmpty(githubUrl) { Social.Github(it) }
                    addIfNotEmpty(linkedinUrl) { Social.Linkedin(it) }
                    addIfNotEmpty(googlePlusUrl) { Social.GooglePlus(it) }
                }
    }

    private fun MutableList<Social>.addIfNotEmpty(item: String, body: (String) -> Social) {
        if (item.isNotEmpty()) {
            add(body(item))
        }
    }
}