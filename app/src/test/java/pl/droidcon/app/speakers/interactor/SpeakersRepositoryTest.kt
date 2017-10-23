package pl.droidcon.app.speakers.interactor

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.domain.Speaker
import util.RxJavaPluginHelper
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class SpeakersRepositoryTest {

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

    private val remote: RemoteSpeakersSource = mock()
    private val local: LocalSpeakersSource = mock()

    private val systemUnderTest = SpeakersRepository(remote, local)

    @Test
    fun `loads speakers`() {
        val localSpeakers = listOf(createSpeaker(1), createSpeaker(2))
        val remoteSpeakers = listOf(createSpeaker(3), createSpeaker(4), createSpeaker(5))

        whenever(local.get()).thenReturn(Maybe.just(localSpeakers))
        whenever(remote.get(any())).thenReturn(Observable.just(remoteSpeakers))

        val testObserver = systemUnderTest.get().test()

        testScheduler.advanceTimeBy(400, TimeUnit.MILLISECONDS)

        testObserver.assertValues(remoteSpeakers)
    }

    //to not create big model without using fields
    private fun createSpeaker(id: Long): Speaker {
        val speaker: Speaker = mock()

        return speaker.apply {
            whenever(this.id).thenReturn(id)
        }
    }
}