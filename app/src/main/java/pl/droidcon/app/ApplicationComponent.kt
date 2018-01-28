package pl.droidcon.app

import dagger.Component
import pl.droidcon.app.about.AboutFragment
import pl.droidcon.app.agenda.view.AgendaFragment
import pl.droidcon.app.agenda.view.AgendaItemFragment
import pl.droidcon.app.data.LocalDataModule
import pl.droidcon.app.data.NetworkModule
import pl.droidcon.app.sessions.SessionComponent
import pl.droidcon.app.sessions.SessionModule
import pl.droidcon.app.speaker.SpeakerComponent
import pl.droidcon.app.speaker.SpeakerModule
import pl.droidcon.app.speakers.view.SpeakersFragment
import javax.inject.Scope

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

    fun sessionComponent(sessionModule: SessionModule): SessionComponent
}