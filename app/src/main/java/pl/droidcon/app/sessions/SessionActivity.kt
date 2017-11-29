package pl.droidcon.app.sessions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_session.*
import pl.droidcon.app.R
import pl.droidcon.app.domain.Session
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speaker.SpeakerActivity

class SessionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        val session = session(intent)
        val firstSpeaker = session.speakers[0]

        session_title.text = session.sessionTitle
        session_description.text = session.sessionDescription
        Picasso.with(this).load(firstSpeaker.imageUrl).into(session_picture)

        setupSpeaker(
                firstSpeaker,
                session_speaker_1_name,
                session_speaker_1_title,
                session_speaker_1_picture,
                session_speaker_1_container
        )

        if (session.speakers.size == 2) {
            setupSpeaker(
                    session.speakers[1],
                    session_speaker_2_name,
                    session_speaker_2_title,
                    session_speaker_2_picture,
                    session_speaker_2_container
            )
        }

    }

    private fun setupSpeaker(speaker: Speaker, nameTextView: TextView, titleTextView: TextView, speakerImageView: ImageView, container: ConstraintLayout) {
        nameTextView.text = "${speaker.firstName} ${speaker.lastName}"
        titleTextView.text = speaker.title
        Picasso.with(this).load(speaker.imageUrl).transform(CropCircleTransformation()).into(speakerImageView)

        container.setOnClickListener { startActivity(SpeakerActivity.intent(this, speaker.id)) }
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
