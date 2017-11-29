package pl.droidcon.app.speaker

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import pl.droidcon.app.ApplicationComponent
import pl.droidcon.app.speakers.interactor.SpeakersRepository

@Subcomponent(modules = arrayOf(SpeakerModule::class))
interface SpeakerComponent {

    fun inject(speakerActivity: SpeakerActivity)
}

@Module
class SpeakerModule(private val speakerId: Long) {

    @Provides
    fun provideSpeakerPresenter(speakerDetailsMapper: SpeakerDetailsMapper, speakersRepository: SpeakersRepository): SpeakerPresenter {
        return SpeakerPresenter(speakerId, speakersRepository, speakerDetailsMapper)
    }
}

fun ApplicationComponent.createSpeakerComponent(speakerId: Long): SpeakerComponent =
        speakerComponent(SpeakerModule(speakerId))