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
        return remoteSpeakersSource.get(onRemoteDone)
                .toObservable()
                .startWith(localSpeakersSource.get().toObservable())
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { !it.isEmpty() } // to not return empty list from remote in case of network error after local is done
                .defaultIfEmpty(emptyList()) // in case of 1. Network Error and Empty DB just return empty list
    }
}