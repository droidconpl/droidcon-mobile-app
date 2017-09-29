package pl.droidcon.app.speakers.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pl.droidcon.app.R
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.ext.bind

class SpeakersAdapter(private val speakers: List<Speaker>) : RecyclerView.Adapter<SpeakerViewHolder>() {

    override fun onBindViewHolder(holder: SpeakerViewHolder, position: Int) = holder.bindHolder(speakers[position])

    override fun getItemCount() = speakers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeakerViewHolder = SpeakerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.speaker_item, parent, false)
    )
}

class SpeakerViewHolder(item: View) : RecyclerView.ViewHolder(item) {

    private val name by item.bind<TextView>(R.id.speaker_name)

    fun bindHolder(speaker: Speaker) {
        name.text = speaker.firstName
    }
}