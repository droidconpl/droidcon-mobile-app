package pl.droidcon.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import pl.droidcon.app.speakers.view.SpeakersActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //TODO setup main view according to one of the inputs
        startActivity(SpeakersActivity.intent(this))
    }
}
