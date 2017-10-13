package pl.droidcon.app.sessions.interactor

import io.reactivex.Observable
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.domain.Session
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SessionsRepository @Inject constructor(private val remoteSessionsSource: RemoteSessionsSource,
                                             private val localSessionsSource: LocalSessionsSource) {

    private val onRemoteSuccess: OnRemoteSuccess<List<Session>> = {
        localSessionsSource.put(it)
    }

    fun sessions(): Observable<List<Session>> {
        val localStream = localSessionsSource.get()
                .toObservable()
                .onErrorResumeNext(Observable.empty())
                .defaultIfEmpty(emptyList())

        return remoteSessionsSource.get(onRemoteSuccess)
                .toObservable()
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .onErrorResumeNext(localStream)
                .defaultIfEmpty(emptyList())
    }
}