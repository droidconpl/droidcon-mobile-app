package pl.droidcon.app.sessions

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import pl.droidcon.app.ApplicationComponent
import pl.droidcon.app.favorite.interactor.FavoriteRepository
import pl.droidcon.app.sessions.interactor.SessionsRepository

@Subcomponent(modules = arrayOf(SessionModule::class))
interface SessionComponent {

    fun inject(sessionActivity: SessionActivity)
}

@Module
class SessionModule(private val sessionId: Long) {

    @Provides
    fun provideSessionPresenter(sessionsRepository: SessionsRepository, favoriteRepository: FavoriteRepository): SessionPresenter {
        return SessionPresenter(sessionId, sessionsRepository, favoriteRepository)
    }
}

fun ApplicationComponent.createSessionComponent(speakerId: Long): SessionComponent =
        sessionComponent(SessionModule(speakerId))