package pl.droidcon.app.agenda

import android.widget.ImageView
import pl.droidcon.app.domain.Session
import pl.droidcon.app.domain.TalkPanel

interface AgendaItemView {
    fun display(talkPanels: List<TalkPanel>)

    fun openSession(speakerPicture: ImageView, session: Session)

}