package pl.droidcon.app.agenda.view

import android.support.v7.widget.util.SortedListAdapterCallback
import pl.droidcon.app.domain.TalkPanel

class AgendaListAdapterCallback constructor(adapter: AgendaAdapter) : SortedListAdapterCallback<TalkPanel>(adapter) {

    override fun compare(o1: TalkPanel, o2: TalkPanel): Int {
        return o1.slotId.compareTo(o2.slotId)
    }

    override fun areItemsTheSame(item1: TalkPanel, item2: TalkPanel): Boolean {
        return item1.talks == item2.talks
    }

    override fun areContentsTheSame(oldItem: TalkPanel, newItem: TalkPanel): Boolean {
        return oldItem.talks == newItem.talks
    }
}