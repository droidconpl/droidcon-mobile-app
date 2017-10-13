package pl.droidcon.app.data.network

import io.reactivex.Single
import retrofit2.http.GET

private const val BASE_URL = "/droidconpl/droidcon-2016-web/master/model"

interface SpeakersService {

    @GET("$BASE_URL/speakers.json")
    fun speakers(): Single<List<SpeakerRemote>>
}

interface SessionsService {

    @GET("$BASE_URL/sessions.json")
    fun sessions(): Single<List<SessionRemote>>
}