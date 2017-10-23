package pl.droidcon.app.speakers.interactor

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.local.SpeakersDao
import pl.droidcon.app.data.mapper.SpeakerMapper
import pl.droidcon.app.data.network.FirebaseSpeaker
import pl.droidcon.app.data.network.SpeakersService
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class RemoteSpeakersSource @Inject constructor(private val speakersService: SpeakersService,
                                               private val speakerMapper: SpeakerMapper)
    : RemoteDataSource<List<Speaker>> {

    override fun get(success: OnRemoteSuccess<List<Speaker>>): Single<List<Speaker>> {
        return speakersService.speakers()
                .map { it.map { apiSpeaker -> speakerMapper.map(apiSpeaker) } }
                .doOnSuccess {
                    if (!it.isEmpty()) {
                        success(it)
                    }
                }
                .onErrorReturn { (emptyList()) }
    }
}

class RemoteFirebaseSpeakers @Inject constructor(private val speakerMapper: SpeakerMapper,
                                                 private val firebaseDatabase: FirebaseDatabase)
    : RemoteDataSource<List<Speaker>> {

    val speakersSubject: PublishSubject<List<Speaker>> = PublishSubject.create()

    override fun get(success: OnRemoteSuccess<List<Speaker>>): Single<List<Speaker>> {


        fun firebaseSpeakers(): Observable<List<Speaker>> {
            val speakersSubject: PublishSubject<List<Speaker>> = PublishSubject.create()


            val speakerReference = firebaseDatabase.getReference("speaker")

            speakerReference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError?) {
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val speakers = mutableListOf<Speaker>()

                    dataSnapshot.children.mapNotNullTo(speakers) {
                        it.getValue<FirebaseSpeaker>(FirebaseSpeaker::class.java)?.let { it1 -> speakerMapper.map(it1) }
                    }

                    speakersSubject.onNext(speakers)
                }
            })

            return speakersSubject
        }

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class LocalSpeakersSource @Inject constructor(private val speakersDao: SpeakersDao,
                                              private val speakerMapper: SpeakerMapper)
    : LocalDataSource<List<Speaker>> {

    override fun get(): Maybe<List<Speaker>> {
        return speakersDao.get()
                .map { it.map { dbSpeaker -> speakerMapper.map(dbSpeaker) } }
                .onErrorReturn { emptyList() }

    }

    override fun put(k: List<Speaker>) {
        val speakers = k.map { speakerMapper.map(it) }
        speakersDao.put(speakers)
    }
}