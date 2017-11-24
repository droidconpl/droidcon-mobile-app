package pl.droidcon.app.agenda.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_agenda.*
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.agenda.AgendaPresenter
import pl.droidcon.app.agenda.AgendaView
import pl.droidcon.app.domain.Agenda
import javax.inject.Inject

class AgendaFragment : Fragment(), AgendaView {

    @Inject
    lateinit var agendaPresenter: AgendaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_agenda, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        agendaPresenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        agendaPresenter.attachView(null)
    }

    override fun display(agenda: Agenda) {
        view?.let {

//            val adapter = AgendaAdapter(agenda.days[0].talkPanels)
//
//            agendaView.layoutManager = LinearLayoutManager(it.context)
//            agendaView.adapter = adapter
        }
    }

    companion object {
        fun instance() = AgendaFragment()
    }
}
