package pl.droidcon.app.agenda.view

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import pl.droidcon.app.R
import pl.droidcon.app.domain.TalkPanel

class AgendaAdapter(private val talks: List<TalkPanel>) : RecyclerView.Adapter<AgendaViewHolder>() {

    private val subject: PublishSubject<TalkPanel> = PublishSubject.create()

    override fun onBindViewHolder(holder: AgendaViewHolder, position: Int) = holder.bindHolder(talks[position])

    override fun getItemCount() = talks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgendaViewHolder = AgendaViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.agenda_item, parent, false), subject
    )

}

class AgendaViewHolder(private val item: View,
                       private val subject: Subject<TalkPanel>) : RecyclerView.ViewHolder(item) {

    private val startTime: TextView = item.findViewById(R.id.start_time)
    private val endTime: TextView = item.findViewById(R.id.end_time)
    private val title: TextView = item.findViewById(R.id.title)
    private val description: TextView = item.findViewById(R.id.description)

    @SuppressLint("SetTextI18n")
    fun bindHolder(talk: TalkPanel) {
        val talk1 = talk.talks[0]
        val talk2 = talk.talks[1]
        val talk3 = talk.talks[2]

        if (talk.sessionType == "singleSlot") {
            title.text = talk2.title + talk1.title
        }

        if (talk.sessionType == "tripleSlot") {
            title.text = talk1.speakers[0].firstName
            description.text = talk2.speakers.firstOrNull()?.firstName + "     " + talk3.speakers.firstOrNull()?.firstName
        }

        startTime.text = talk.start
        endTime.text = talk.end
    }
}