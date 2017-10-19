package pl.droidcon.app

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
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import util.RxJavaPluginHelper
import java.io.IOException
import java.util.concurrent.TimeUnit

@RunWith(JUnit4::class)
class RepositoryTest {

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

    private val remote: RemoteDataSource<List<String>> = mock()
    private val local: LocalDataSource<List<String>> = mock()

    private val systemUnderTest = Repository(remote, local)

    @Test
    fun `starts with local`() {
        val localList = createLocal()
        val remoteSubject = createRemoteSubject()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(localList)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun `returns only one result when one within 300 ms`() {
        val localList = createLocal()
        val remoteList = createRemote()
        val remoteSubject = createRemoteSubject()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        testObserver.assertNoValues()
        remoteSubject.onSuccess(remoteList)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        testObserver.assertValue(remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `returns empty list once when both sources empty`() {
        whenever(local.get()).thenReturn(Maybe.just(emptyList()))
        whenever(remote.get(any())).thenReturn(Single.just(emptyList()))

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(emptyList())
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `returns first not empty result when error`() {
        val localList = createLocal()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(Single.error(IOException()))

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValue(localList)
        testObserver.assertComplete()
    }

    @Test
    fun `returns twice if remote slow`() {
        val localList = createLocal()
        val remoteList = createRemote()
        val remoteSubject = createRemoteSubject()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        remoteSubject.onSuccess(remoteList)
        testObserver.assertValues(localList, remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `returns remote when local error`() {
        val remoteList = createRemote()

        whenever(local.get()).thenReturn(Maybe.error(IOException()))
        whenever(remote.get(any())).thenReturn(Single.just(remoteList))

        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValue(remoteList)
        testObserver.assertNoErrors()
        testObserver.assertComplete()
    }

    @Test
    fun `updates local source with remote when local exists`() {
        val captor = argumentCaptor<OnRemoteSuccess<List<String>>>()

        val localList = createLocal()
        val remoteList = createRemote()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(captor.capture())).thenReturn(Single.just(remoteList))

        systemUnderTest.get().test()

        captor.firstValue.invoke(remoteList)
        verify(local).put(eq(remoteList))
    }

    @Test
    fun `updates local source with remote when local empty`() {
        val captor = argumentCaptor<OnRemoteSuccess<List<String>>>()

        val remoteList = createRemote()

        whenever(local.get()).thenReturn(Maybe.never())
        whenever(remote.get(captor.capture())).thenReturn(Single.just(remoteList))

        systemUnderTest.get().test()

        captor.firstValue.invoke(remoteList)
        verify(local).put(eq(remoteList))
    }

    private fun createLocal() = listOf("foo.bar")

    private fun createRemote() = listOf("123", "321")

    private fun createRemoteSubject() = SingleSubject.create<List<String>>()
}
