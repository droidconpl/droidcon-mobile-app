package pl.droidcon.app.sessions.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.mapper.SessionTypeMapper
import pl.droidcon.app.data.mapper.SessionsMapper
import pl.droidcon.app.data.network.SessionRemote
import pl.droidcon.app.data.network.SessionsService
import pl.droidcon.app.domain.Session
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import util.createSpeaker
import java.io.IOException

@RunWith(JUnit4::class)
class RemoteSessionsSourceTest {

    private val sessionsService: SessionsService = mock()
    private val speakersRepository: SpeakersRepository = mock()
    private val success: OnRemoteSuccess<List<Session>> = mock()

    private val sessionsMapper = SessionsMapper(SessionTypeMapper())

    private val systemUnderTest = RemoteSessionsSource(sessionsService, sessionsMapper, speakersRepository)

    private val sessionRemote = SessionRemote(
            sessionId = 123L,
            sessionType = "Talk",
            sessionTitle = "title",
            sessionDescription = "1234566",
            speakerIds = listOf(1, 2),
            sessionLength = 45.0,
            workshopCapacity = 13
    )

    private val speaker1 = createSpeaker(1)
    private val speaker2 = createSpeaker(2)
    private val speakers = listOf(speaker1, speaker2)
    private val response = listOf(sessionRemote)
    private val expected = sessionsMapper.map(sessionRemote, speakers)

    @Before
    fun setUp() {
        whenever(speakersRepository.get()).thenReturn(Observable.just(speakers))
        whenever(sessionsService.sessions()).thenReturn(Observable.just(response))
    }

    @Test
    fun `calls success when loaded from remote`() {
        systemUnderTest.get(success).test()

        verify(success).invoke(listOf(expected))
    }

    @Test
    fun `returns remote when subscribed`() {
        systemUnderTest.get(success).test()
                .assertValue(listOf(expected))
    }

    @Test
    fun `does not call success when empty response`() {
        whenever(sessionsService.sessions()).thenReturn(Observable.just(emptyList()))

        systemUnderTest.get(success).test()

        verify(success, times(0)).invoke(listOf(expected))
    }

    @Test
    fun `returns empty list when speakers failed`() {
        whenever(speakersRepository.get()).thenReturn(Observable.error(IOException()))

        systemUnderTest.get(success).test()
                .assertValue(emptyList())
    }

    @Test
    fun `returns empty list when sessions failed`() {
        whenever(sessionsService.sessions()).thenReturn(Observable.error(IOException()))

        systemUnderTest.get(success).test()
                .assertValue(emptyList())
    }
}