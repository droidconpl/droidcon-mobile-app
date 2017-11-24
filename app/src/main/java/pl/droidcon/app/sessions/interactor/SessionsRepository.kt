package pl.droidcon.app.sessions.interactor

import pl.droidcon.app.ApplicationScope
import pl.droidcon.app.DataRepository
import pl.droidcon.app.Repository
import pl.droidcon.app.domain.Session
import javax.inject.Inject

@ApplicationScope
class SessionsRepository
@Inject constructor(private val remoteSessionsSource: RemoteFirebaseSessionsSource /* change to RemoteSessionsSource for using old API */,
                    private val localSessionsSource: LocalSessionsSource)
    : DataRepository<List<Session>> by Repository(remoteSessionsSource, localSessionsSource, emptyList())