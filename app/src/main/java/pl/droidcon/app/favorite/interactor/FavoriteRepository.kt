package pl.droidcon.app.favorite.interactor

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import pl.droidcon.app.DataRepository
import pl.droidcon.app.data.local.FavoriteDao
import pl.droidcon.app.data.local.FavoriteLocal
import javax.inject.Inject

class FavoriteRepository @Inject constructor(val dao: FavoriteDao) : DataRepository<List<FavoriteLocal>> {


    private val subject: BehaviorSubject<List<FavoriteLocal>> = BehaviorSubject.create()

    init {
        val observable = get().startWith(emptyList<FavoriteLocal>())

        observable
                .subscribeOn(Schedulers.io())
                .subscribe(subject)
    }

    override fun get(): Observable<List<FavoriteLocal>> {
        return dao.getFavorites().toObservable()
    }

    fun delete(favoriteLocal: FavoriteLocal) {
        Observable
                .fromCallable { dao.deleteFavorite(favoriteLocal) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ get().subscribe(subject) }, { e -> e.printStackTrace() })
    }

    fun put(favoriteLocal: FavoriteLocal) {
        Observable
                .fromCallable { dao.putFavorite(favoriteLocal) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ get().subscribe(subject) }, { e -> e.printStackTrace() })
    }

    fun getFavorite(sessionId: Long): Observable<FavoriteLocal> {
        return subject
                .flatMap { favorites -> Observable.fromIterable(favorites) }
                .filter { favorite -> favorite.sessionId == sessionId }
    }
}