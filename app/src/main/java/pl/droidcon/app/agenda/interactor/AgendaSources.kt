package pl.droidcon.app.agenda.interactor

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
import pl.droidcon.app.data.local.AgendaDao
import pl.droidcon.app.data.local.DayLocal
import pl.droidcon.app.data.local.TalkPanelLocal
import pl.droidcon.app.data.mapper.AgendaMapper
import pl.droidcon.app.data.network.AgendaService
import pl.droidcon.app.data.network.FirebaseAgenda
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

class FirebaseAgendaSource @Inject constructor(private val agendaMapper: AgendaMapper,
                                               private val firebaseDatabase: FirebaseDatabase,
                                               private val sessionsRepository: SessionsRepository,
                                               private val speakersRepository: SpeakersRepository)
    : RemoteDataSource<Agenda> {

    val agendaSubject: PublishSubject<Agenda> = PublishSubject.create()

    override fun get(success: OnRemoteSuccess<Agenda>): Observable<Agenda> {
        val agendaReference = firebaseDatabase.getReference("agenda")

        agendaReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                sessionsRepository
                        .get()
                        .distinctUntilChanged()
                        .filter({ it.isNotEmpty() })
                        .map { sessions ->
                            val agendaEntries = mutableListOf<FirebaseAgenda>()

                            dataSnapshot.children.mapNotNullTo(agendaEntries) {
                                it.getValue<FirebaseAgenda>(FirebaseAgenda::class.java)
                            }

                            agendaMapper.map2(agendaEntries, sessions)
                        }
                        .doOnNext(success)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(agendaSubject)
            }
        })

        return agendaSubject
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
                TalkPanelLocal(start = it.start, end = it.end, talks = talkIds, sessionType = it.sessionType, text = it.text)
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

                        TalkPanel(panel.start, panel.end, talks, panel.sessionType, panel.text)
                    })

                    val days = dayLocals.map { Day(it.id, talkPanels) }
                    Agenda(days)
                }
    }
}