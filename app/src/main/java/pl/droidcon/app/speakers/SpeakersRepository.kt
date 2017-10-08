package pl.droidcon.app.speakers

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import pl.droidcon.app.data.FirebaseSpeaker
import pl.droidcon.app.data.Speaker
import pl.droidcon.app.network.SpeakersService
import javax.inject.Inject

const val USE_FIREBASE: Boolean = true

class SpeakersRepository @Inject constructor(
        private val speakersService: SpeakersService,
        private val speakerMapper: SpeakerMapper) {

    fun speakers(): Observable<List<Speaker>> {

        return if (USE_FIREBASE) {
            firebaseSpeakers()
        } else {
            apiSpeakers()
        }
    }

    private fun apiSpeakers(): Observable<List<Speaker>> {
        return speakersService.speakers()
                .map { it.map { apiSpeaker -> speakerMapper.map(apiSpeaker) } }
                .toObservable()
    }

    fun firebaseSpeakers(): Observable<List<Speaker>> {
        val speakersSubject: PublishSubject<List<Speaker>> = PublishSubject.create()


        val speakerReference = FirebaseDatabase.getInstance().getReference("speaker")

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
}