package pl.droidcon.app.speaker

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import pl.droidcon.app.ApplicationComponent
import pl.droidcon.app.domain.Speaker

@Subcomponent(modules = arrayOf(SpeakerModule::class))
interface SpeakerComponent {

    fun inject(speakerActivity: SpeakerActivity)
}

@Module
class SpeakerModule(private val speaker: Speaker) {

    @Provides
    fun provideSpeakerPresenter(speakerDetailsMapper: SpeakerDetailsMapper): SpeakerPresenter {
        return SpeakerPresenter(speaker, speakerDetailsMapper)
    }
}

fun ApplicationComponent.createSpeakerComponent(speaker: Speaker): SpeakerComponent =
        speakerComponent(SpeakerModule(speaker))