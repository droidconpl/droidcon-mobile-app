package pl.droidcon.app.agenda

import android.widget.ImageView
import pl.droidcon.app.domain.Session

interface AgendaItemView {
    fun openSession(speakerPicture: ImageView, session: Session)

}