package pl.droidcon.app.speakers.interactor

import io.reactivex.Maybe
import io.reactivex.Single
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.local.SpeakersDao
import pl.droidcon.app.data.mapper.SpeakerMapper
import pl.droidcon.app.data.network.SpeakersService
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class RemoteSpeakersSource @Inject constructor(private val speakersService: SpeakersService,
                                               private val speakerMapper: SpeakerMapper)
    : RemoteDataSource<List<Speaker>> {

    override fun get(onRemoteDone: OnRemoteSuccess<List<Speaker>>): Single<List<Speaker>> {
        return speakersService.speakers()
                .map { it.map { apiSpeaker -> speakerMapper.map(apiSpeaker) } }
                .doOnSuccess {
                    if (!it.isEmpty()) {
                        onRemoteDone(it)
                    }
                }
                .onErrorReturn { (emptyList()) }
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