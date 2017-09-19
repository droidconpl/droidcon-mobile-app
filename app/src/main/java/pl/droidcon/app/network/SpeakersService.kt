package pl.droidcon.app.network

import io.reactivex.Single
import pl.droidcon.app.data.ApiSpeaker
import retrofit2.http.GET

interface SpeakersService {

    @GET("/droidconpl/droidcon-2016-web/master/model/speakers.json")
    fun speakers(): Single<List<ApiSpeaker>>
}