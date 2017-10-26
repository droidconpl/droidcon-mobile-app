package pl.droidcon.app.sessions.interactor

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.data.local.SessionLocal
import pl.droidcon.app.data.local.SessionsDao
import pl.droidcon.app.data.mapper.SessionTypeMapper
import pl.droidcon.app.data.mapper.SessionsMapper
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import util.createSpeaker
import java.io.IOException

@RunWith(JUnit4::class)
class LocalSessionsSourceTest {

    private val sessionsMapper = SessionsMapper(SessionTypeMapper())
    private val speakersRepository: SpeakersRepository = mock()
    private val sessionsDao: SessionsDao = mock()

    private val sessionLocal = SessionLocal(
            sessionId = 123L,
            sessionType = "Talk",
            sessionTitle = "title",
            sessionDescription = "1234566",
            speakerIds = listOf(111),
            sessionLength = 45.0,
            workshopCapacity = 13
    )

    private val speaker1 = createSpeaker(111)
    private val speaker2 = createSpeaker(231)
    private val speakers = listOf(speaker1, speaker2)
    private val response = listOf(sessionLocal)
    private val session = sessionsMapper.map(sessionLocal, speakers)

    private val systemUnderTest = LocalSessionsSource(sessionsDao, sessionsMapper, speakersRepository)

    @Before
    fun setUp() {
        whenever(sessionsDao.get()).thenReturn(Maybe.just(response))
        whenever(speakersRepository.get()).thenReturn(Observable.just(speakers))
    }

    @Test
    fun `returns empty list when dao error`() {
        whenever(sessionsDao.get()).thenReturn(Maybe.error(NoSuchFieldError()))

        systemUnderTest.get().test()
                .assertValue(emptyList())
    }

    @Test
    fun `returns nothing when speakers not emitted`() {
        whenever(speakersRepository.get()).thenReturn(Observable.empty())

        systemUnderTest.get().test()
                .assertNoValues()
                .assertComplete()
    }

    @Test
    fun `returns empty list when speakers error`() {
        whenever(speakersRepository.get()).thenReturn(Observable.error(IOException()))

        systemUnderTest.get().test()
                .assertValue(emptyList())
    }

    @Test
    fun `returns result from dao`() {
        systemUnderTest.get().test()
                .assertValue(listOf(session))
    }

    @Test
    fun `updates dao`() {
        systemUnderTest.put(listOf(session))

        verify(sessionsDao).put(listOf(sessionLocal))
    }
}