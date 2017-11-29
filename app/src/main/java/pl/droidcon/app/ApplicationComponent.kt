package pl.droidcon.app

import dagger.Component
import pl.droidcon.app.about.AboutFragment
import pl.droidcon.app.agenda.view.AgendaFragment
import pl.droidcon.app.agenda.view.AgendaItemFragment
import pl.droidcon.app.data.LocalDataModule
import pl.droidcon.app.data.NetworkModule
import pl.droidcon.app.speaker.SpeakerComponent
import pl.droidcon.app.speaker.SpeakerModule
import pl.droidcon.app.speakers.view.SpeakersFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Component(modules = arrayOf(
        NetworkModule::class,
        LocalDataModule::class
))
@ApplicationScope
interface ApplicationComponent {

    fun inject(speakersFragment: SpeakersFragment)

    fun inject(aboutFragment: AboutFragment)

    fun inject(agendaFragment: AgendaFragment)
    fun inject(agendaItemFragment: AgendaItemFragment)

    fun speakerComponent(speakerModule: SpeakerModule): SpeakerComponent
}