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
        localDataSource.clear()
        localDataSource.put(it)
    }

    override fun get(): Observable<List<T>> {
        val localStream = localDataSource.get()
                .toObservable()
                .onErrorResumeNext(Observable.empty())
                .defaultIfEmpty(emptyList())

        return observeRemote()
                .switchIfEmpty(localStream)
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .materialize()
                .filter { it.isOnNext }
                .dematerialize()
    }

    private fun observeRemote(): Observable<List<T>> {
        return remoteDataSource.get(onRemoteSuccess)
                .retry(10) // max 10 retries
                .onErrorResumeNext(Observable.never())
    }
}