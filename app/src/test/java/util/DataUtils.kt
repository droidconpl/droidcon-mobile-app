package util

import pl.droidcon.app.domain.Speaker

fun createSpeaker(id: Long) = Speaker(
        id = id,
        firstName = "",
        lastName = "",
        title = "",
        description = "",
        websiteUrl = "",
        facebookUrl = "",
        twitterUrl = "$id",
        githubUrl = "",
        linkedinUrl = "",
        googlePlusUrl = "",
        imageUrl = ""
)