package pl.droidcon.app.data

import io.reactivex.Maybe
import io.reactivex.Single

typealias OnRemoteSuccess<T> = (T) -> Unit

interface RemoteDataSource<T> {
    fun get(onRemoteDone: OnRemoteSuccess<T>): Single<T>
}

interface LocalDataSource<K> {
    fun get(): Maybe<K>

    fun put(k: K)
}
