package pl.droidcon.app.speakers.interactor

import pl.droidcon.app.DataRepository
import pl.droidcon.app.Repository
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class SpeakersRepository
@Inject constructor(private val remoteSpeakersSource: RemoteSpeakersSource,
                    private val localSpeakersSource: LocalSpeakersSource)
    : DataRepository<Speaker> by Repository(remoteSpeakersSource, localSpeakersSource)