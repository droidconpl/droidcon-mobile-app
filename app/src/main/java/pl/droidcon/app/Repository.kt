package pl.droidcon.app

import io.reactivex.Observable
import io.reactivex.ObservableOperator
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
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
                .lift(IgnoreErrorOperator())
                .startWith(localStream)
                .debounce(300, TimeUnit.MILLISECONDS)
                .filter { it.isNotEmpty() }
                .defaultIfEmpty(emptyList())
    }
}

private class IgnoreErrorOperator<T> : ObservableOperator<List<T>, List<T>> {

    override fun apply(observer: Observer<in List<T>>): Observer<in List<T>> = IgnoreErrorObserver(observer)
}

private class IgnoreErrorObserver<T>(private val child: Observer<T>) : Observer<T> {

    override fun onError(e: Throwable) {
        // ignore error
    }

    override fun onSubscribe(d: Disposable) {
        child.onSubscribe(d)
    }

    override fun onNext(t: T) {
        child.onNext(t)
    }

    override fun onComplete() {
        child.onComplete()
    }
}