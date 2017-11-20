package pl.droidcon.app.agenda.interactor

import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.rxkotlin.zipWith
import pl.droidcon.app.data.LocalDataSource
import pl.droidcon.app.data.OnRemoteSuccess
import pl.droidcon.app.data.RemoteDataSource
import pl.droidcon.app.data.local.AgendaDao
import pl.droidcon.app.data.local.DayLocal
import pl.droidcon.app.data.local.TalkPanelLocal
import pl.droidcon.app.data.mapper.AgendaMapper
import pl.droidcon.app.data.network.AgendaService
import pl.droidcon.app.domain.*
import pl.droidcon.app.sessions.interactor.SessionsRepository
import pl.droidcon.app.speakers.interactor.SpeakersRepository
import javax.inject.Inject

class RemoteAgendaSource @Inject constructor(private val agendaService: AgendaService,
                                             private val agendaMapper: AgendaMapper,
                                             private val sessionsRepository: SessionsRepository,
                                             private val speakersRepository: SpeakersRepository)
    : RemoteDataSource<Agenda> {

    override fun get(success: OnRemoteSuccess<Agenda>): Observable<Agenda> {
        return sessionsRepository.get()
                .distinctUntilChanged()
                .zipWith(speakersRepository.get())
                .filter({ it.second.isNotEmpty() && it.first.isNotEmpty() })
                .flatMap {
                    val sessions = it.first
                    val speakers = it.second

                    agendaService.agenda()
                            .map { agendaMapper.map(it, sessions, speakers) }
                            .doOnNext(success)
                }
    }
}

class LocalAgendaSource @Inject constructor(private val agendaDao: AgendaDao,
                                            private val agendaMapper: AgendaMapper,
                                            private val sessionsRepository: SessionsRepository,
                                            private val speakersRepository: SpeakersRepository)
    : LocalDataSource<Agenda> {

    override fun get(): Maybe<Agenda> {
        val preconditions = sessionsRepository.get()
                .distinctUntilChanged()
                .zipWith(speakersRepository.get())
                .filter({ it.second.isNotEmpty() && it.first.isNotEmpty() })
                .firstElement()

        return preconditions.flatMap { getAgenda(it.first, it.second) }
    }

    override fun put(k: Agenda) {
        val days = k.days.map {
            val talkPanels = it.talkPanels.map {
                val talksLocal = it.talks.map { agendaMapper.map(it) }
                val talkIds = agendaDao.putTalks(talkLocals = talksLocal)
                TalkPanelLocal(start = it.start, end = it.end, talks = talkIds, sessionType = it.sessionType)
            }

            val panelIds = agendaDao.putTalkPanels(talkPanelLocal = talkPanels)
            DayLocal(it.id, panelIds)
        }

        agendaDao.putDays(days = days)
    }

    override fun clear() {
        agendaDao.clear()
    }

    private fun getAgenda(sessions: List<Session>, speakers: List<Speaker>): Maybe<Agenda> {
        return agendaDao.getDayLocal()
                .zipWith(agendaDao.getTalkPanelLocal(), { a, b -> a to b })
                .zipWith(agendaDao.getTalkLocal(), { a, b -> a to b })
                .map {
                    val dayLocals = it.first.first
                    val talkPanelsLocal = it.first.second
                    val talkLocals = it.second

                    val talkPanels = talkPanelsLocal.map({
                        @Suppress("UnnecessaryVariable")
                        val panel = it
                        val talksForPanel = talkLocals.filter { panel.talks.contains(it.id) }
                        val talks = talksForPanel.map { agendaMapper.map(it, speakers, sessions) }

                        TalkPanel(panel.start, panel.end, talks, panel.sessionType)
                    })

                    val days = dayLocals.map { Day(it.id, talkPanels) }
                    Agenda(days)
                }
    }
}