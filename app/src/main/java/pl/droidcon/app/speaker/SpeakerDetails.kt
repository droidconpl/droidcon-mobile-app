package pl.droidcon.app.speaker

import android.content.Intent
import android.net.Uri

data class SpeakerDetails(val name: String,
                          val title: String,
                          val description: String,
                          val photoUrl: String,
                          val socials: List<Social>)


sealed class Social(private val value: String) {

    fun intent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))

    class Website(value: String) : Social(value)
    class Facebook(value: String) : Social(value)
    class Twitter(value: String) : Social(value)
    class Github(value: String) : Social(value)
    class Linkedin(value: String) : Social(value)
    class GooglePlus(value: String) : Social(value)
}
