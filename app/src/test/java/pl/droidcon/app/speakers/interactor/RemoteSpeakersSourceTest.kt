package pl.droidcon.app.speakers.interactor

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Observable
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.mapper.SpeakerMapper
import pl.droidcon.app.data.network.SpeakerRemote
import pl.droidcon.app.data.network.SpeakersService
import pl.droidcon.app.domain.Speaker
import java.io.IOException

@RunWith(JUnit4::class)
class RemoteSpeakersSourceTest {

    private val speakersService: SpeakersService = mock()
    private val success: OnRemoteSuccess<List<Speaker>> = mock()
    private val speakerMapper = SpeakerMapper()

    private val systemUnderTest = RemoteSpeakersSource(speakersService, speakerMapper)

    @Test
    fun `calls success when loaded from remote`() {
        val speaker = SpeakerRemote(
                id = 12,
                firstName = "name",
                lastName = "last",
                title = "title",
                description = "desc",
                websiteUrl = "web",
                facebookUrl = "fac",
                twitterUrl = "tw",
                githubUrl = "gh",
                linkedinUrl = "in",
                googlePlusUrl = "gp",
                imageUrl = "image"
        )
        val response = listOf(speaker)
        val expected = speakerMapper.map(speaker)
        whenever(speakersService.speakers()).thenReturn(Observable.just(response))

        systemUnderTest.get(success).test()

        verify(success).invoke(listOf(expected))
    }

    @Test
    fun `returns remote when subscribed`() {
        val speaker = SpeakerRemote(
                id = 12,
                firstName = "name",
                lastName = "last",
                title = "title",
                description = "desc",
                websiteUrl = "web",
                facebookUrl = "fac",
                twitterUrl = "tw",
                githubUrl = "gh",
                linkedinUrl = "in",
                googlePlusUrl = "gp",
                imageUrl = "image"
        )
        val response = listOf(speaker)
        val expected = speakerMapper.map(speaker)
        whenever(speakersService.speakers()).thenReturn(Observable.just(response))

        systemUnderTest.get(success).test()
                .assertValue(listOf(expected))
    }

    @Test
    fun `does not call success when loaded empty`() {
        whenever(speakersService.speakers()).thenReturn(Observable.just(emptyList()))

        systemUnderTest.get(success).test()

        verify(success, times(0)).invoke(any())
    }

    @Test
    fun `returns error if error`() {
        val error = IOException()
        whenever(speakersService.speakers()).thenReturn(Observable.error(error))

        systemUnderTest.get(success).test()
                .assertError(error)
    }
}