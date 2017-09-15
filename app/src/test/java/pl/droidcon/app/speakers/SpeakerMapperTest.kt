package pl.droidcon.app.speakers

import com.google.common.truth.Truth.assertThat
import junitparams.JUnitParamsRunner
import junitparams.Parameters
import org.junit.Test
import org.junit.runner.RunWith
import pl.droidcon.app.data.ApiSpeaker
import pl.droidcon.app.data.Speaker

@RunWith(JUnitParamsRunner::class)
class SpeakerMapperTest {

    private val systemUnderTest = SpeakerMapper()

    @Test
    @Parameters(method = "maps_params")
    fun maps(id: Long, firstName: String, lastName: String, title: String, description: String, websiteUrl: String,
             fb: String, twitter: String, gh: String, linkedIn: String, gplus: String, image: String) {

        val expectedSpeaker = Speaker(id, firstName, lastName, title, description, websiteUrl, fb, twitter, websiteUrl, gh, gplus, image)
        val apiSpeaker = ApiSpeaker(id, firstName, lastName, title, description, websiteUrl, fb, twitter, websiteUrl, gh, gplus, image)

        val resultUnderTest = systemUnderTest.map(apiSpeaker)

        assertThat(resultUnderTest).isEqualTo(expectedSpeaker)
    }

    @Suppress("unused")
    private fun maps_params() = arrayOf(
            arrayOf(2L, "foo", "bar", "serious", "long description", "udp:2112", "fb", "twitter:sss", "gith/awe", "lin", "g++", "image"),
            arrayOf(0L, "", "", "", "", "", "", "", "", "", "", "")
    )
}