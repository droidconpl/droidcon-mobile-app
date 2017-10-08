package pl.droidcon.app.speakers.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.data.local.SpeakerLocal
import pl.droidcon.app.data.local.SpeakersDao
import pl.droidcon.app.data.mapper.SpeakerMapper
import pl.droidcon.app.domain.Speaker
import java.io.IOException

@RunWith(JUnit4::class)
class LocalSpeakersSourceTest {

    private val speakersMapper = SpeakerMapper()
    private val speakersDao: SpeakersDao = mock()

    private val systemUnderTest = LocalSpeakersSource(speakersDao, speakersMapper)

    @Test
    fun returns_empty_list_when_dao_error() {
        whenever(speakersDao.get()).thenReturn(Maybe.error(IOException()))

        systemUnderTest.get().test()
                .assertValue(emptyList())
    }

    @Test
    fun returns_result_from_dao() {
        val speaker = SpeakerLocal(
                id = 12,
                firstName = "fir",
                lastName = "las",
                title = "tit",
                description = "desc",
                websiteUrl = "web",
                facebookUrl = "fac",
                twitterUrl = "tw",
                githubUrl = "ghs",
                linkedinUrl = "linsa1",
                googlePlusUrl = "gpsa",
                imageUrl = "image"
        )
        val response = listOf(speaker)
        val expected = listOf(speakersMapper.map(speaker))
        whenever(speakersDao.get()).thenReturn(Maybe.just(response))

        systemUnderTest.get().test()
                .assertValue(expected)
    }

    @Test
    fun updates_dao() {
        val speaker = Speaker(
                id = 12,
                firstName = "fir",
                lastName = "las",
                title = "tit",
                description = "desc",
                websiteUrl = "web",
                facebookUrl = "fac",
                twitterUrl = "tw",
                githubUrl = "ghs",
                linkedinUrl = "linsa1",
                googlePlusUrl = "gpsa",
                imageUrl = "image"
        )
        val expected = listOf(speakersMapper.map(speaker))

        systemUnderTest.put(listOf(speaker))

        verify(speakersDao).put(expected)
    }
}