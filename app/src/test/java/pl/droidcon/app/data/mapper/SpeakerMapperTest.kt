package pl.droidcon.app.data.mapper

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import pl.droidcon.app.data.local.SpeakerLocal
import pl.droidcon.app.data.network.FirebaseSpeaker
import pl.droidcon.app.domain.Speaker

@RunWith(JUnitParamsRunner::class)
class SpeakerMapperTest {

    private val systemUnderTest = SpeakerMapper()

    @Test
    @Parameters(method = "maps_params")
    fun maps(id: Long, firstName: String, lastName: String, title: String, description: String, websiteUrl: String,
             fb: String, twitter: String, gh: String, linkedIn: String, gplus: String, image: String) {

        val speaker = Speaker(id = id, firstName = firstName, lastName = lastName, title = title, description = description, websiteUrl = websiteUrl, facebookUrl = fb, twitterUrl = twitter, githubUrl = gh, linkedinUrl = linkedIn, googlePlusUrl = gplus, imageUrl = image)
        val speakerLocal = SpeakerLocal(id = id, firstName = firstName, lastName = lastName, title = title, description = description, websiteUrl = websiteUrl, facebookUrl = fb, twitterUrl = twitter, githubUrl = gh, linkedinUrl = linkedIn, googlePlusUrl = gplus, imageUrl = image)

        val speakerFromLocal = systemUnderTest.map(speakerLocal)
        val speakerLocalFromDomain = systemUnderTest.map(speaker)

        assertThat(speakerLocalFromDomain).isEqualTo(speakerLocal)
        assertThat(speakerFromLocal).isEqualTo(speaker)
    }

    @Test
    @Ignore("will implement rest once we setup all firebase speaker attributes")
    fun mapFirebase() {
        val firebaseSpeakerRemote = FirebaseSpeaker(name = "first_name", surname = "last_name", photourl = "photo_url")
//        val speaker = Speaker(firstName = "first_name", lastName = "last_name", imageUrl = "photo_url")
//
//        val speakerFromRemote = systemUnderTest.map(firebaseSpeakerRemote)
//
//        assertThat(speaker).isEqualTo(speakerFromRemote)
    }

    @Suppress("unused")
    private fun maps_params() = arrayOf(
            arrayOf(2L, "foo", "bar", "serious", "long description", "udp:2112", "fb", "twitter:sss", "gith/awe", "lin", "g++", "image"),
            arrayOf(0L, "", "", "", "", "", "", "", "", "", "", "")
    )
}