package pl.droidcon.app

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import java.util.concurrent.TimeUnit

interface DataRepository<T> {
    fun get(): Observable<T>
}

class Repository<T>(private val remoteDataSource: RemoteDataSource<T>,
                    private val localDataSource: LocalDataSource<T>,
                    defaultIfEmpty: T) : DataRepository<T> {

    private val subject: BehaviorSubject<T> = BehaviorSubject.create()

    private val onRemoteSuccess: OnRemoteSuccess<T> = {
        localDataSource.clear()
        localDataSource.put(it)
    }

    init {
        val localStream = localDataSource.get()
                .toObservable()
                .doOnError { it.printStackTrace() }
                .onErrorResumeNext(Observable.empty())
                .defaultIfEmpty(defaultIfEmpty)
                .distinctUntilChanged()

        val observable: Observable<T> = observeRemote()
                .distinctUntilChanged()
                .doOnError { it.printStackTrace() }
                .switchIfEmpty(localStream)
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .materialize()
                .filter { it.isOnNext }
                .distinctUntilChanged()
                .dematerialize()

        observable.subscribe({ subject.onNext(it) }, { subject.onNext(defaultIfEmpty) })
    }

    override fun get(): Observable<T> = subject

    private fun observeRemote(): Observable<T> {
        return remoteDataSource.get(onRemoteSuccess)
                .retry(10) // max 10 retries
                .onErrorResumeNext(Observable.never())
    }
}