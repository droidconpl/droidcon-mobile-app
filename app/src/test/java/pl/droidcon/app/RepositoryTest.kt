package pl.droidcon.app

import com.nhaarman.mockito_kotlin.*
import io.reactivex.*
import io.reactivex.schedulers.TestScheduler
import io.reactivex.subjects.PublishSubject
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

    @Test
    fun `starts with local`() {
        val localList = createLocal()
        val remoteSubject = createRemoteSubject()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValue(localList)
        testObserver.assertNotTerminated()
    }

    @Test
    fun `returns only one result when one within 300 ms`() {
        val localList = createLocal()
        val remoteList = createRemote()
        val remoteSubject = createRemoteSubject()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()

        testScheduler.advanceTimeBy(200, TimeUnit.MILLISECONDS)
        testObserver.assertNoValues()

        remoteSubject.onNext(remoteList)
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValue(remoteList)
        testObserver.assertNotTerminated()
    }

    @Test
    fun `returns empty list once when both sources empty`() {
        val remoteSubject = createRemoteSubject()
        whenever(local.get()).thenReturn(Maybe.just(emptyList()))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()
        remoteSubject.onNext(emptyList())
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValue(emptyList())
        testObserver.assertNoErrors()
        testObserver.assertNotTerminated()
    }

    @Test
    fun `returns first not empty result when error`() {
        val localList = createLocal()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(Observable.error(IOException()))

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValue(localList)
        testObserver.assertNotTerminated()
    }

    @Test
    fun `returns twice if remote slow`() {
        val localList = createLocal()
        val remoteList = createRemote()
        val remoteSubject = createRemoteSubject()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testScheduler.advanceTimeBy(100, TimeUnit.MILLISECONDS)
        remoteSubject.onNext(remoteList)

        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testScheduler.triggerActions()

        testObserver.assertValues(localList, remoteList)
        testObserver.assertNotTerminated()
    }

    @Test
    fun `returns remote when local error`() {
        val remoteList = createRemote()
        whenever(local.get()).thenReturn(Maybe.error(IOException()))
        whenever(remote.get(any())).thenReturn(Observable.just(remoteList))

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValue(remoteList)
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    @Test
    fun `updates local source with remote when local exists`() {
        val captor = argumentCaptor<OnRemoteSuccess<List<String>>>()

        val localList = createLocal()
        val remoteList = createRemote()
        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(captor.capture())).thenReturn(Observable.just(remoteList))

        val systemUnderTest = createSystemUnderTest()
        systemUnderTest.get().test()

        captor.firstValue.invoke(remoteList)
        inOrder(local).run {
            verify(local).clear()
            verify(local).put(eq(remoteList))
        }
    }

    @Test
    fun `updates local source with remote when local empty`() {
        val captor = argumentCaptor<OnRemoteSuccess<List<String>>>()

        val remoteList = createRemote()
        whenever(local.get()).thenReturn(Maybe.never())
        whenever(remote.get(captor.capture())).thenReturn(Observable.just(remoteList))

        val systemUnderTest = createSystemUnderTest()
        systemUnderTest.get().test()

        captor.firstValue.invoke(remoteList)
        verify(local).put(eq(remoteList))
    }

    @Test
    fun `distinct updates each time emits`() {
        val localList = createLocal()
        val remoteList = createRemote()
        val remoteSubject = createRemoteSubject()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject)

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()

        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        remoteSubject.onNext(remoteList)
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValues(localList, remoteList)

        remoteSubject.onNext(remoteList)
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValues(localList, remoteList)

        remoteSubject.onNext(remoteList)
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        testObserver.assertValues(localList, remoteList)
    }

    @Test
    fun `does not stop when remote error`() {
        val localList = createLocal()
        val remoteList = createRemote()

        val remoteSubject = FakeSubject<List<String>>()

        whenever(local.get()).thenReturn(Maybe.just(localList))
        whenever(remote.get(any())).thenReturn(remoteSubject.asObservable())

        val systemUnderTest = createSystemUnderTest()
        val testObserver = systemUnderTest.get().test()

        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)
        remoteSubject.onError(IOException())
        remoteSubject.onNext(remoteList)
        testScheduler.advanceTimeBy(300, TimeUnit.MILLISECONDS)

        testObserver.assertValues(localList, remoteList)
    }

    private fun createLocal() = listOf("foo.bar")

    private fun createRemote() = listOf("123", "321")

    private fun createRemoteSubject() = PublishSubject.create<List<String>>()

    private fun createSystemUnderTest() = Repository(remote, local, emptyList())
}

private class FakeSubject<T> {
    private var realSubject = PublishSubject.create<Notification<T>>()

    fun asObservable(): Observable<T> = realSubject.dematerialize<T>()

    fun onError(e: Exception) {
        realSubject.onNext(Notification.createOnError(e))
    }

    fun onNext(value: T) {
        realSubject.onNext(Notification.createOnNext(value))
    }
}