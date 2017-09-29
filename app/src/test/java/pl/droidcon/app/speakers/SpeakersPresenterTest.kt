package pl.droidcon.app.speakers

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import util.RxJavaPluginHelper


@RunWith(JUnit4::class)
class SpeakersPresenterTest {

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

    private val speakerRepository: SpeakersRepository = mock()
    private val view: SpeakersView = mock()
    private val speakers: List<Speaker> = mock()

    private val systemUnderTest = SpeakersPresenter(speakerRepository)

    @Before
    fun setUp() {
        whenever(speakerRepository.speakers()).thenReturn(Observable.just(speakers))
    }

    @Test
    fun display_speakers() {
        systemUnderTest.attachView(view)

        verify(view).display(speakers)
    }

    @Test
    fun does_not_display_speakers_when_detached() {
        val subject: BehaviorSubject<List<Speaker>> = BehaviorSubject.create()

        whenever(speakerRepository.speakers()).thenReturn(subject)

        systemUnderTest.attachView(view)
        systemUnderTest.attachView(null)

        verify(view, times(0)).display(speakers)
    }

    @Test
    fun display_speaker() {
        val speaker: Speaker = mock()

        systemUnderTest.attachView(view)
        systemUnderTest.onSpeakerSelected(speaker)

        verify(view).display(speaker)
    }
}