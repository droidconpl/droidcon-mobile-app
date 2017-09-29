package pl.droidcon.app.data.network

import io.reactivex.Single
import pl.droidcon.app.data.SpeakerData
import retrofit2.http.GET

interface SpeakersService {

    @retrofit2.http.GET("/droidconpl/droidcon-2016-web/master/model/speakers.json")
    fun speakers(): io.reactivex.Single<List<SpeakerData>>
}