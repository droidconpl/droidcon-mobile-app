package pl.droidcon.app.speakers.interactor

import com.nhaarman.mockito_kotlin.*
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.SingleSubject
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.domain.Speaker
import util.RxJavaPluginHelper
import java.io.IOException
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
    private val firebaseRemote: RemoteFirebaseSpeakerSource = mock()
    private val speaker = Speaker(1L, "", "", "", "", "", "", "", "gh", "l", "gp", "image")

    private val systemUnderTest = SpeakersRepository(remote, local, firebaseRemote)

    @Test
    fun starts_with_local() {
        val localList = listOf(speaker)
        val remoteSubject = SingleSubject.create<List<Speaker>>()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(localList)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun returns_only_one_result_when_one_within_300_ms() {
        val localList = listOf(speaker)
        val remoteList = listOf(speaker, speaker)
        val remoteSubject = SingleSubject.create<List<Speaker>>()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        testObserver.assertNoValues()
        remoteSubject.onSuccess(remoteList)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        testObserver.assertValue(remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun returns_empty_list_once_when_both_sources_empty() {
        whenever(local.get()).thenReturn(Maybe.just(emptyList()))
        whenever(remote.get(any())).thenReturn(Single.just(emptyList()))

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(emptyList())
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun returns_first_not_empty_result_when_error() {
        val localList = listOf(speaker)
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(Single.error(IOException()))

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValue(localList)
        testObserver.assertComplete()
    }

    @Test
    fun returns_twice_if_remote_slow() {
        val localList = listOf(speaker)
        val remoteList = listOf(speaker, speaker)
        val remoteSubject = SingleSubject.create<List<Speaker>>()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        remoteSubject.onSuccess(remoteList)
        testObserver.assertValues(localList, remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun returns_remote_when_local_error() {
        val remoteList = listOf(speaker, speaker)

        whenever(local.get()).thenReturn(Maybe.error(IOException()))
        whenever(remote.get(any())).thenReturn(Single.just(remoteList))

        val testObserver = systemUnderTest.speakers().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun updates_local_source_with_remote_when_local_exists() {
        val captor = argumentCaptor<OnRemoteSuccess<List<Speaker>>>()

        val localList = listOf(speaker)
        val remoteList = listOf(speaker, speaker)

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(captor.capture())).thenReturn(Single.just(remoteList))

        systemUnderTest.speakers().test()

        captor.firstValue.invoke(remoteList)
        verify(local).put(eq(remoteList))
    }

    @Test
    fun updates_local_source_with_remote_when_local_empty() {
        val captor = argumentCaptor<OnRemoteSuccess<List<Speaker>>>()

        val remoteList = listOf(speaker, speaker)

        whenever(local.get()).thenReturn(Maybe.never())
        whenever(remote.get(captor.capture())).thenReturn(Single.just(remoteList))

        systemUnderTest.speakers().test()

        captor.firstValue.invoke(remoteList)
        verify(local).put(eq(remoteList))
    }
}