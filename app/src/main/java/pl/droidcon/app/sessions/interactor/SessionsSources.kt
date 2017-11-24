package pl.droidcon.app.sessions.interactor

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.local.SessionsDao
import pl.droidcon.app.data.mapper.SessionsMapper
import pl.droidcon.app.data.network.FirebaseSession
import pl.droidcon.app.data.network.SessionsService
import pl.droidcon.app.domain.Session
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import javax.inject.Inject

class RemoteSessionsSource @Inject constructor(private val sessionsService: SessionsService,
                                               private val sessionsMapper: SessionsMapper,
                                               private val speakersRepository: SpeakersRepository)
    : RemoteDataSource<List<Session>> {

    override fun get(success: OnRemoteSuccess<List<Session>>): Observable<List<Session>> {
        return sessionsService.sessions()
                .zipWith(speakersRepository.get().filter({ it.isNotEmpty() }))
                .map { (sessionsRemote, speakers) -> sessionsRemote.map { sessionsMapper.map(it, speakers) } }
                .doOnNext {
                    if (it.isNotEmpty()) {
                        success(it)
                    }
                }
    }
}

class RemoteFirebaseSessionsSource @Inject constructor(private val sessionsMapper: SessionsMapper,
                                                       private val firebaseDatabase: FirebaseDatabase,
                                                       private val speakersRepository: SpeakersRepository)
    : RemoteDataSource<List<Session>> {

    val sessionsSubject: PublishSubject<List<Session>> = PublishSubject.create()


    override fun get(success: OnRemoteSuccess<List<Session>>): Observable<List<Session>> {
        val speakerReference = firebaseDatabase.getReference("talks")

        speakerReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                speakersRepository
                        .get()
                        .filter({ it.isNotEmpty() })
                        .map { speakers ->
                            val sessions = mutableListOf<Session>()

                            dataSnapshot.children.mapNotNullTo(sessions) {
                                it.getValue<FirebaseSession>(FirebaseSession::class.java)?.let { it1 -> sessionsMapper.map(it1, speakers) }
                            }
                        }
                        .doOnNext(success)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(sessionsSubject)
            }
        })

        return sessionsSubject
    }
}

class LocalSessionsSource @Inject constructor(private val sessionsDao: SessionsDao,
                                              private val sessionsMapper: SessionsMapper,
                                              private val speakersRepository: SpeakersRepository)
    : LocalDataSource<List<Session>> {

    override fun get(): Maybe<List<Session>> {
        return speakersRepository.get()
                .firstElement()
                .zipWith(sessionsDao.get())
                .map { (speakers, sessionsLocal) -> sessionsLocal.map { sessionsMapper.map(it, speakers) } }
                .onErrorReturn { emptyList() }
    }

    override fun put(k: List<Session>) {
        val sessions = k.map { sessionsMapper.map(it) }
        sessionsDao.put(sessions)
    }

    override fun clear() = sessionsDao.clear()
}