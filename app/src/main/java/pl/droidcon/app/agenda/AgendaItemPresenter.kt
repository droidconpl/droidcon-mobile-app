package pl.droidcon.app.agenda

import android.widget.ImageView
import pl.droidcon.app.domain.Session
import javax.inject.Inject

class AgendaItemPresenter @Inject constructor() {

    private var view: AgendaItemView? = null

    fun attachView(agendaItemView: AgendaItemView?) {
        this.view = agendaItemView
    }

    fun openSession(speakerPicture: ImageView, session: Session) {
        view?.openSession(speakerPicture, session)
    }
}