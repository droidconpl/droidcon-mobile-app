package pl.droidcon.app.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import pl.droidcon.app.ApplicationScope
import pl.droidcon.app.data.network.SessionsService
import pl.droidcon.app.data.network.SpeakersService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT_SECOND = 20L
//TODO change to correct url here
private const val URL = "https://raw.githubusercontent.com"

@Module
class NetworkModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @ApplicationScope
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
        readTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
        writeTimeout(TIME_OUT_SECOND, TimeUnit.SECONDS)
    }.build()

    @Provides
    @ApplicationScope
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder().apply {
        addConverterFactory(GsonConverterFactory.create(gson))
        addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        client(okHttpClient)
        baseUrl(URL)
    }.build()

    @Provides
    @ApplicationScope
    fun provideSpeakersService(retrofit: Retrofit): SpeakersService = retrofit.create(SpeakersService::class.java)

    @Provides
    @ApplicationScope
    fun provideSessionsService(retrofit: Retrofit): SessionsService = retrofit.create(SessionsService::class.java)
}