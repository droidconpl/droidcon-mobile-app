package pl.droidcon.app

import dagger.Component
import pl.droidcon.app.data.DataModule
import pl.droidcon.app.data.NetworkModule
import pl.droidcon.app.speakers.view.SpeakersFragment
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope

@Component(modules = arrayOf(
        NetworkModule::class,
        DataModule::class
))
@ApplicationScope
interface ApplicationComponent {

    fun inject(speakersFragment: SpeakersFragment)
}