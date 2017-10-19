package pl.droidcon.app.speakers.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SpeakersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(android.R.id.content, SpeakersFragment.instance())
                    .commit()
        }
    }

    companion object {
        fun intent(context: Context) = Intent(context, SpeakersActivity::class.java)
    }
}