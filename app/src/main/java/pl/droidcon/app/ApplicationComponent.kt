package pl.droidcon.app

import dagger.Component
import pl.droidcon.app.network.NetworkModule
import pl.droidcon.app.speakers.SpeakersFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Component(modules = arrayOf(
        NetworkModule::class
))
@ApplicationScope
interface ApplicationComponent {

    fun inject(speakersFragment: SpeakersFragment)
}