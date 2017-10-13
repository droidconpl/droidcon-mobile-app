package pl.droidcon.app.sessions.interactor

import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.rxkotlin.zipWith
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.local.SessionsDao
import pl.droidcon.app.data.mapper.SessionsMapper
import pl.droidcon.app.data.network.SessionsService
import pl.droidcon.app.domain.Session
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import javax.inject.Inject

class RemoteSessionsSource @Inject constructor(private val sessionsService: SessionsService,
                                               private val sessionsMapper: SessionsMapper,
                                               private val speakersRepository: SpeakersRepository)
    : RemoteDataSource<List<Session>> {

    override fun get(success: OnRemoteSuccess<List<Session>>): Single<List<Session>> {
        return sessionsService.sessions()
                .zipWith(speakersRepository.speakers().firstOrError())
                .map { (sessionsRemote, speakers) -> sessionsRemote.map { sessionsMapper.map(it, speakers) } }
                .doOnSuccess {
                    if (it.isNotEmpty()) {
                        success(it)
                    }
                }
                .onErrorReturn { emptyList() }
    }
}

class LocalSessionsSource @Inject constructor(private val sessionsDao: SessionsDao,
                                              private val sessionsMapper: SessionsMapper,
                                              private val speakersRepository: SpeakersRepository)
    : LocalDataSource<List<Session>> {

    override fun get(): Maybe<List<Session>> {
        return speakersRepository.speakers()
                .firstElement()
                .zipWith(sessionsDao.get())
                .map { (speakers, sessionsLocal) -> sessionsLocal.map { sessionsMapper.map(it, speakers) } }
                .onErrorReturn { emptyList() }
    }

    override fun put(k: List<Session>) {
        val sessions = k.map { sessionsMapper.map(it) }
        sessionsDao.put(sessions)
    }
}