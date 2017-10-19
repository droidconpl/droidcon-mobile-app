package pl.droidcon.app

import io.reactivex.Observable
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import java.util.concurrent.TimeUnit

interface DataRepository<T> {
    fun get(): Observable<List<T>>
}

class Repository<T>(private val remoteDataSource: RemoteDataSource<List<T>>,
                    private val localDataSource: LocalDataSource<List<T>>) : DataRepository<T> {

    private val onRemoteSuccess: OnRemoteSuccess<List<T>> = {
        localDataSource.put(it)
    }

    override fun get(): Observable<List<T>> {
        val localStream = localDataSource.get()
                .toObservable()
                .onErrorResumeNext(Observable.empty()) // in case of error reading local, we would like still wait for remote and update local
                .defaultIfEmpty(emptyList()) // if empty, return empty list and filter in next stream

        return remoteDataSource.get(onRemoteSuccess)
                .toObservable()
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() } // to not return empty list from remote in case of network error after local is done
                .onErrorResumeNext(localStream) // in case of Network Error return local
                .defaultIfEmpty(emptyList())
    }
}