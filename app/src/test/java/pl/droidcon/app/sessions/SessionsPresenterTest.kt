package pl.droidcon.app.sessions

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.domain.Session
import pl.droidcon.app.sessions.interactor.SessionsRepository
import util.RxJavaPluginHelper

@RunWith(JUnit4::class)
class SessionsPresenterTest {

    companion object {
        @BeforeClass
        @JvmStatic
        fun setup() {
            RxJavaPluginHelper.setup()
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            RxJavaPluginHelper.teardown()
        }
    }

    private val sessionRepository: SessionsRepository = mock()
    private val view: SessionsView = mock()
    private val sessions: List<Session> = emptyList()

    private val systemUnderTest = SessionsPresenter(sessionRepository)

    @Before
    fun setUp() {
        whenever(sessionRepository.get()).thenReturn(Observable.just(sessions))
    }

    @Test
    fun `displays sessions`() {
        systemUnderTest.attachView(view)

        verify(view).display(sessions)
    }

    @Test
    fun `does not display sessions when detached`() {
        val subject: BehaviorSubject<List<Session>> = BehaviorSubject.create()
        whenever(sessionRepository.get()).thenReturn(subject)

        systemUnderTest.attachView(view)
        systemUnderTest.attachView(null)

        verify(view, times(0)).display(sessions)
    }

    @Test
    fun `displays session`() {
        val session: Session = mock()

        systemUnderTest.attachView(view)
        systemUnderTest.onSessionSelected(session)

        verify(view).display(session)
    }
}