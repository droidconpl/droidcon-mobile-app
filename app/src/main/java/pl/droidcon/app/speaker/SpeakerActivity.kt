package pl.droidcon.app.speaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import android.widget.ImageButton
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.activity_speaker.*
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.domain.Speaker
import javax.inject.Inject

class SpeakerActivity : AppCompatActivity(), SpeakerView {

    @Inject
    lateinit var presenter: SpeakerPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.createSpeakerComponent(speaker(intent)).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speaker)

        presenter.attachView(this)
    }

    override fun onDestroy() {
        presenter.attachView(null)
        super.onDestroy()
    }

    override fun displayDetails(speakerDetails: SpeakerDetails) {
        with(speakerDetails) {
            Picasso.with(this@SpeakerActivity)
                    .load(photoUrl)
                    .transform(CropCircleTransformation())
                    .placeholder(R.drawable.ic_person)
                    .resizeDimen(R.dimen.photo_size, R.dimen.photo_size)
                    .into(speaker_photo)

            speaker_full_name.text = name
            speaker_title.text = title
            speaker_bio.text = Html.fromHtml(description)

            socials.forEach {
                when (it) {
                    is Social.Website -> website_link.setAndDisplay(it)
                    is Social.Facebook -> facebook_link.setAndDisplay(it)
                    is Social.Twitter -> twitter_link.setAndDisplay(it)
                    is Social.Github -> github_link.setAndDisplay(it)
                    is Social.Linkedin -> linkedin_link.setAndDisplay(it)
                    is Social.GooglePlus -> google_link.setAndDisplay(it)
                }
            }
        }
    }

    private fun ImageButton.setAndDisplay(social: Social) {
        visibility = View.VISIBLE
        setOnClickListener {
            startActivity(social.intent())
        }
    }

    companion object {
        private const val SPEAKER_ARG = "speaker"

        private fun speaker(intent: Intent): Speaker = intent.extras.getParcelable(SPEAKER_ARG)

        fun intent(context: Context, speaker: Speaker): Intent {
            return Intent(context, SpeakerActivity::class.java).apply {
                putExtra(SPEAKER_ARG, speaker)
            }
        }
    }
}