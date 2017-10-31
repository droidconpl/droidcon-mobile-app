package pl.droidcon.app.speakers.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.droidcon.app.R
import pl.droidcon.app.domain.Speaker


class SpeakersAdapter(private val speakers: List<Speaker>) : RecyclerView.Adapter<SpeakerViewHolder>() {

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) = holder.bindHolder(speakers[position])

    override fun getItemCount() = speakers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder = SpeakerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.speaker_item, parent, false)
    )
}

class SpeakerViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val name: TextView = item.findViewById(R.id.speaker_name)
    private val image: ImageView = item.findViewById(R.id.speaker_picture)
    private val title: TextView = item.findViewById(R.id.speaker_title)

    @SuppressLint("SetTextI18n")
    fun bindHolder(speaker: Speaker) {
        name.text = "${speaker.firstName} ${speaker.lastName}"
        title.text= speaker.title
        Picasso.with(image.context).load(speaker.imageUrl).placeholder(R.drawable.ic_person).into(image)
    }
}