package pl.droidcon.app.sessions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_session.*
import pl.droidcon.app.R
import pl.droidcon.app.domain.Session

class SessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        val session = session(intent)

        session_title.text = session.sessionTitle

        session_description.text = session.sessionDescription

        Picasso.with(this).load(session.speakers.first().imageUrl).into(session_picture)
    }

    companion object {
        private const val SESSION_ARG = "sessions"

        private fun session(intent: Intent): Session = intent.extras.getParcelable(SESSION_ARG)

        fun intent(context: Context, session: Session): Intent {
            return Intent(context, SessionActivity::class.java).apply {
                putExtra(SESSION_ARG, session)
            }
        }
    }
}