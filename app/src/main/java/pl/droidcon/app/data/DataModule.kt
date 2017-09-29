package pl.droidcon.app.data

import pl.droidcon.app.data.local.DroidconDatabase

@dagger.Module
class DataModule(context: android.content.Context) {

    init {
        DroidconDatabase.init(context)
    }

    @dagger.Provides
    @pl.droidcon.app.ApplicationScope
    fun provideSpeakerDao() = DroidconDatabase.get().speakerDao()
}