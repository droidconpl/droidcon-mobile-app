package pl.droidcon.app.agenda.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import pl.droidcon.app.R
import pl.droidcon.app.agenda.AgendaItemPresenter
import pl.droidcon.app.domain.TalkPanel


class AgendaAdapter(private val talks: List<TalkPanel>, val agendaItemPresenter: AgendaItemPresenter) : RecyclerView.Adapter<AgendaHolder>() {

    override fun onBindViewHolder(holder: AgendaHolder, position: Int) = holder.bindHolder(talks[position])

    override fun getItemCount() = talks.size

    override fun getItemViewType(position: Int): Int = if (talks[position].sessionType == "meta") 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaHolder {
        when (viewType) {
            0 -> return AgendaSingleHolder(LayoutInflater.from(parent.context).inflate(R.layout.agenda_item, parent, false))
            else -> return AgendaTripleHolder(LayoutInflater.from(parent.context).inflate(R.layout.agenda_item_tripple, parent, false), agendaItemPresenter)
        }
    }
}

class AgendaSingleHolder(item: View) : AgendaHolder(item) {

    private val startTime: TextView = item.findViewById(R.id.start_time)
    private val endTime: TextView = item.findViewById(R.id.end_time)
    private val title: TextView = item.findViewById(R.id.title)

    @SuppressLint("SetTextI18n")
    override fun bindHolder(talk: TalkPanel) {
        title.text = talk.text

        startTime.text = talk.start
        endTime.text = talk.end
    }
}

class AgendaTripleHolder(private val item: View, val agendaItemPresenter: AgendaItemPresenter) : AgendaHolder(item) {

    private val startTime: TextView = item.findViewById(R.id.start_time)
    private val endTime: TextView = item.findViewById(R.id.end_time)

    private val speaker1picture: ImageView = item.findViewById(R.id.speaker_1_picture)
    private val speaker1room: TextView = item.findViewById(R.id.speaker_1_room)
    private val speaker1description: TextView = item.findViewById(R.id.speaker_1_description)

    private val speaker2picture: ImageView = item.findViewById(R.id.speaker_2_picture)
    private val speaker2room: TextView = item.findViewById(R.id.speaker_2_room)
    private val speaker2description: TextView = item.findViewById(R.id.speaker_2_description)

    private val speaker3picture: ImageView = item.findViewById(R.id.speaker_3_picture)
    private val speaker3room: TextView = item.findViewById(R.id.speaker_3_room)
    private val speaker3description: TextView = item.findViewById(R.id.speaker_3_description)


    @SuppressLint("SetTextI18n")
    override fun bindHolder(talk: TalkPanel) {
        val talk1 = talk.talks[0]


        startTime.text = talk.start
        endTime.text = talk.end

        Picasso
                .with(item.context)
                .load(talk1.speakers.firstOrNull()?.imageUrl)
                .placeholder(R.drawable.ic_person)
                .error(R.drawable.ic_sad)
                .into(speaker1picture)
        speaker1description.text = talk1.session!!.sessionTitle

        // TODO: optimize !
        speaker1picture.setOnClickListener {
            agendaItemPresenter.openSession(speaker1picture, talk1.session)
        }

        if (talk.talks.size == 1) {
            speaker2picture.visibility = View.GONE
            speaker3picture.visibility = View.GONE

            speaker2room.visibility = View.GONE
            speaker3room.visibility = View.GONE
        } else {
            val talk2 = talk.talks[1]
            speaker2picture.visibility = View.VISIBLE
            speaker2room.visibility = View.VISIBLE

            if (talk2.speakers.isEmpty()) {
                speaker2picture.setImageResource(R.drawable.ic_sad)
                speaker2description.text = ""
            } else {
                speaker2description.text = talk2.session!!.sessionTitle
                Picasso
                        .with(item.context)
                        .load(talk2.speakers.firstOrNull()?.imageUrl)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_sad)
                        .into(speaker2picture)
            }
            speaker2picture.setOnClickListener {
                agendaItemPresenter.openSession(speaker2picture, talk2.session)
            }

            if (talk.talks.size == 3) {
                val talk3 = talk.talks[2]
                if (talk3.speakers.isEmpty()) {
                    speaker3picture.setImageResource(R.drawable.ic_sad)
                    speaker3description.text = ""
                } else {
                    speaker3description.text = talk3.session!!.sessionTitle
                    Picasso
                            .with(item.context)
                            .load(talk3.speakers.firstOrNull()?.imageUrl)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_sad)
                            .into(speaker3picture)
                }
                speaker3picture.setOnClickListener {
                    agendaItemPresenter.openSession(speaker3picture, talk3.session)
                }

                speaker3picture.visibility = View.VISIBLE
                speaker3room.visibility = View.VISIBLE
            } else {
                speaker3picture.setImageResource(R.drawable.ic_sad)
            }
        }

    }
}

abstract class AgendaHolder(item: View) : RecyclerView.ViewHolder(item) {

    abstract fun bindHolder(talk: TalkPanel)
}