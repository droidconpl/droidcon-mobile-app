package pl.droidcon.app.data

import io.reactivex.Maybe
import io.reactivex.Observable

typealias OnRemoteSuccess<T> = (T) -> Unit

interface RemoteDataSource<T> {
    fun get(success: OnRemoteSuccess<T>): Observable<T>
}

interface LocalDataSource<K> {
    fun get(): Maybe<K>

    fun put(k: K)

    fun clear()
}
