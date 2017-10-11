package pl.droidcon.app.data.network

interface SpeakersService {

    @retrofit2.http.GET("/droidconpl/droidcon-2016-web/master/model/speakers.json")
    fun speakers(): io.reactivex.Single<List<SpeakerRemote>>
}