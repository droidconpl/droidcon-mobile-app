package pl.droidcon.app.about

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.agenda.AgendaPresenter
import pl.droidcon.app.agenda.AgendaView
import javax.inject.Inject

class AboutFragment : Fragment(), AgendaView {

    @Inject
    lateinit var agendaPresenter: AgendaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_sessions, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        agendaPresenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        agendaPresenter.attachView(null)
    }

    companion object {
        fun instance() = AboutFragment()
    }
}