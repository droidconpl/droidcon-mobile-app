package pl.droidcon.app

import android.app.Application
import android.content.Context
import pl.droidcon.app.data.DataModule

class DroidconApp : Application() {

    override fun attachBaseContext(base: Context?) {
        component = DaggerApplicationComponent.builder()
                .dataModule(DataModule(this))
                .build()
        super.attachBaseContext(base)
    }

    companion object {
        lateinit var component: ApplicationComponent
    }
}