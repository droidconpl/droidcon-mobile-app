package pl.droidcon.app.sessions.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.domain.Session
import util.RxJavaPluginHelper
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class SessionsRepositoryTest {

    companion object {
        private val testScheduler = TestScheduler()

        @BeforeClass
        @JvmStatic
        fun setup() {
            RxJavaPluginHelper.setup(testScheduler)
        }

        @AfterClass
        @JvmStatic
        fun teardown() {
            RxJavaPluginHelper.teardown()
        }
    }

    private val remote: RemoteSessionsSource = mock()
    private val local: LocalSessionsSource = mock()

    private val systemUnderTest = SessionsRepository(remote, local)

    @Test
    fun `loads sessions`() {
        val localSessions = listOf(createSession(1), createSession(2))
        val remoteSessions = listOf(createSession(3), createSession(4), createSession(5))

        whenever(local.get()).thenReturn(Maybe.just(localSessions))
        whenever(remote.get(any())).thenReturn(Single.just(remoteSessions))

        val testObserver = systemUnderTest.get().test()

        testScheduler.advanceTimeBy(400, TimeUnit.MILLISECONDS)

        testObserver.assertValues(remoteSessions)
    }

    //to not create big model without using fields
    private fun createSession(id: Long): Session {
        val session: Session = mock()

        return session.apply {
            whenever(sessionId).thenReturn(id)
        }
    }
}