package pl.droidcon.app

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import pl.droidcon.app.data.LocalDataModule
import com.google.firebase.FirebaseApp

class DroidconApp : Application() {

    override fun attachBaseContext(base: Context?) {
        component = DaggerApplicationComponent.builder()
                .localDataModule(LocalDataModule(this))
                .build()
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        Stetho.initializeWithDefaults(this)
    }
}