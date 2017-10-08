package pl.droidcon.app.speakers.interactor

import io.reactivex.Observable
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.domain.Speaker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeakersRepository @Inject constructor(private val remoteSpeakersSource: RemoteSpeakersSource,
                                             private val localSpeakersSource: LocalSpeakersSource) {

    private val onRemoteDone: OnRemoteSuccess<List<Speaker>> = {
        localSpeakersSource.put(it)
    }

    fun speakers(): Observable<List<Speaker>> {
        val localStream = localSpeakersSource.get()
                .toObservable()
                .onErrorResumeNext(Observable.empty()) // in case of error reading local, we would like still wait for remote and update local
                .defaultIfEmpty(emptyList()) // if empty, return empty list and filter in next stream

        return remoteSpeakersSource.get(onRemoteDone)
                .toObservable()
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { !it.isEmpty() } // to not return empty list from remote in case of network error after local is done
                .onErrorResumeNext(localStream) // in case of Network Error return local
                .defaultIfEmpty(emptyList())
    }
}