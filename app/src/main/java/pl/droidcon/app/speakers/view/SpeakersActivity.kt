package pl.droidcon.app.speakers.view

class SpeakersActivity : android.support.v7.app.AppCompatActivity() {

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, pl.droidcon.app.speakers.view.SpeakersFragment.Companion.instance())
                    .commit()
        }
    }

    companion object {
        fun intent(context: android.content.Context) = android.content.Intent(context, SpeakersActivity::class.java)
    }
}