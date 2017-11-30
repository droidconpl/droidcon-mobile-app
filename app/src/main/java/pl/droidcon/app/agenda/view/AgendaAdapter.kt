package pl.droidcon.app.agenda.view

import android.annotation.SuppressLint
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.droidcon.app.R
import pl.droidcon.app.agenda.AgendaItemPresenter
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.domain.Talk
import pl.droidcon.app.domain.TalkPanel


class AgendaAdapter(private val talks: List<TalkPanel>, val agendaItemPresenter: AgendaItemPresenter) : RecyclerView.Adapter<AgendaHolder>() {

    val sortedList = SortedList<TalkPanel>(TalkPanel::class.java, AgendaListAdapterCallback(this))

    init {
        sortedList.addAll(talks)
    }

    override fun onBindViewHolder(holder: AgendaHolder, position: Int) = holder.bindHolder(sortedList[position])

    override fun getItemCount() = sortedList.size()

    override fun getItemViewType(position: Int): Int = if (sortedList[position].sessionType == "meta") 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaHolder {
        when (viewType) {
            0 -> return AgendaSingleHolder(LayoutInflater.from(parent.context).inflate(R.layout.agenda_item, parent, false))
            else -> return AgendaTripleHolder(LayoutInflater.from(parent.context).inflate(R.layout.agenda_item_tripple, parent, false), agendaItemPresenter)
        }
    }

    fun add(talkPanels: List<TalkPanel>) {
        sortedList.addAll(talkPanels)
    }

}


class AgendaSingleHolder(item: View) : AgendaHolder(item) {

    private val startTime: TextView = item.findViewById(R.id.start_time)
    private val endTime: TextView = item.findViewById(R.id.end_time)
    private val title: TextView = item.findViewById(R.id.title)
    private val image: ImageView = item.findViewById(R.id.item_icon)

    @SuppressLint("SetTextI18n")
    override fun bindHolder(talk: TalkPanel) {
        title.text = talk.text

        startTime.text = talk.start
        endTime.text = talk.end

        if (talk.imageUrl.isNotEmpty()) {
            Picasso
                    .with(image.context)
                    .load(talk.imageUrl)
                    .into(image)
        }
    }
}

class AgendaTripleHolder(private val item: View, val agendaItemPresenter: AgendaItemPresenter) : AgendaHolder(item) {

    private val startTime: TextView = item.findViewById(R.id.start_time)
    private val endTime: TextView = item.findViewById(R.id.end_time)

    private val session1: AgendaItemPack = AgendaItemPack(
            speakerImageView = item.findViewById(R.id.speaker_1_picture),
            roomTextView = item.findViewById(R.id.speaker_1_room),
            descriptionTextView = item.findViewById(R.id.speaker_1_description)
    )

    private val session2: AgendaItemPack = AgendaItemPack(
            speakerImageView = item.findViewById(R.id.speaker_2_picture),
            roomTextView = item.findViewById(R.id.speaker_2_room),
            descriptionTextView = item.findViewById(R.id.speaker_2_description)
    )

    private val session3: AgendaItemPack = AgendaItemPack(
            speakerImageView = item.findViewById(R.id.speaker_3_picture),
            roomTextView = item.findViewById(R.id.speaker_3_room),
            descriptionTextView = item.findViewById(R.id.speaker_3_description)
    )

    @SuppressLint("SetTextI18n")
    override fun bindHolder(talk: TalkPanel) {
        startTime.text = talk.start
        endTime.text = talk.end

        val numberOfTalks = talk.talks.size

        setupTalk(talk.talks[0], session1)

        if (numberOfTalks == 1) {
            session2.speakerImageView.visibility = View.GONE
            session2.roomTextView.visibility = View.GONE

            session3.speakerImageView.visibility = View.GONE
            session3.roomTextView.visibility = View.GONE
        } else {
            setupTalk(talk.talks[1], session2)

            session2.speakerImageView.visibility = View.VISIBLE
            session2.roomTextView.visibility = View.VISIBLE

            if (numberOfTalks == 3) {
                setupTalk(talk.talks[2], session3)

                session3.speakerImageView.visibility = View.VISIBLE
                session3.roomTextView.visibility = View.VISIBLE
            } else {
                session3.speakerImageView.visibility = View.GONE
                session3.roomTextView.visibility = View.GONE
            }
        }

    }

    private fun setupTalk(talk: Talk, itemPack: AgendaItemPack) {
        itemPack.speakerImageView.setOnClickListener {
            agendaItemPresenter.openSession(itemPack.speakerImageView, talk.session)
        }

        // fallback for corrupted data
        if (talk.speakers.isEmpty()) {
            itemPack.speakerImageView.setImageResource(R.drawable.ic_sad)
            itemPack.descriptionTextView.text = ""
        } else {
            setSpeakerPicture(talk.speakers, itemPack.speakerImageView)
            itemPack.descriptionTextView.text = talk.session.sessionTitle
        }
    }

    private fun setSpeakerPicture(speakers: List<Speaker>, speakerIv: ImageView) {
        Picasso
                .with(item.context)
                .load(speakers.firstOrNull()?.imageUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_sad)
                .into(speakerIv)
    }

    data class AgendaItemPack(
            val speakerImageView: ImageView,
            val roomTextView: TextView,
            val descriptionTextView: TextView
    )
}

abstract class AgendaHolder(item: View) : RecyclerView.ViewHolder(item) {

    abstract fun bindHolder(talk: TalkPanel)
}