package pl.droidcon.app.data

import dagger.Module
import dagger.Provides
import pl.droidcon.app.ApplicationScope
import pl.droidcon.app.data.local.DroidconDatabase

@Module
class LocalDataModule(context: android.content.Context) {

    init {
        DroidconDatabase.init(context)
    }

    @Provides
    @ApplicationScope
    fun provideSpeakerDao() = DroidconDatabase.get().speakerDao()
}