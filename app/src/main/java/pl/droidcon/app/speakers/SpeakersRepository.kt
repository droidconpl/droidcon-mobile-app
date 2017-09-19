package pl.droidcon.app.speakers

import io.reactivex.Observable
import pl.droidcon.app.data.Speaker
import pl.droidcon.app.network.SpeakersService
import javax.inject.Inject

class SpeakersRepository @Inject constructor(
        private val speakersService: SpeakersService,
        private val speakerMapper: SpeakerMapper) {

    fun speakers(): Observable<List<Speaker>> {
        return speakersService.speakers()
                .map { it.map { apiSpeaker -> speakerMapper.map(apiSpeaker) } }
                .toObservable()
    }
}