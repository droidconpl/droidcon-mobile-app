package pl.droidcon.app.favorite.interactor

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import pl.droidcon.app.ApplicationScope
import pl.droidcon.app.DataRepository
import pl.droidcon.app.data.local.FavoriteDao
import pl.droidcon.app.data.local.FavoriteLocal
import javax.inject.Inject

@ApplicationScope
class FavoriteRepository @Inject constructor(val dao: FavoriteDao) : DataRepository<List<FavoriteLocal>> {


    private val subject: BehaviorSubject<List<FavoriteLocal>> = BehaviorSubject.create()

    init {
        val observable = get()

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subject::onNext)
    }

    override fun get(): Observable<List<FavoriteLocal>> {
        return dao.getFavorites().toObservable()
    }

    fun delete(favoriteLocal: FavoriteLocal) {
        favoriteLocal.isFavorite = false
        Observable
                .fromCallable { dao.putFavorite(favoriteLocal) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ get().subscribe(subject::onNext) }, { e -> e.printStackTrace() })
    }

    fun put(favoriteLocal: FavoriteLocal) {
        favoriteLocal.isFavorite = true
        Observable
                .fromCallable { dao.putFavorite(favoriteLocal) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ get().subscribe(subject::onNext) }, { e -> e.printStackTrace() })
    }

    fun getFavorite(sessionId: Long): Observable<FavoriteLocal> {
        return subject
                .flatMap { favorites -> Observable.fromIterable(favorites) }
                .filter { favorite -> favorite.sessionId == sessionId }
    }
}