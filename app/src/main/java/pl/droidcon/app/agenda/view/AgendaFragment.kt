package pl.droidcon.app.agenda.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
            agenda_view_pager.adapter = AgendaPagerAdapter(agenda, fragmentManager)
        }
    }

    companion object {
        fun instance() = AgendaFragment()
    }
}

class AgendaPagerAdapter constructor(val agenda: Agenda, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = AgendaItemFragment.newInstance(agenda, position)

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0) "Friday " else "Saturday"
    }
}

class AgendaItemFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_agenda_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val agendaRecyclerView = view.findViewById<RecyclerView>(R.id.agendaView)

        val dayId = arguments.getInt(AgendaItemFragment.DAY_ID_PARAM)
        val agenda = arguments.getParcelable<Agenda>(AgendaItemFragment.AGENDA_PARAM)

        if (agenda.days.isEmpty()) return

        val talks = agenda.days[dayId].talkPanels
        val adapter = AgendaAdapter(talks)
        agendaRecyclerView.layoutManager = LinearLayoutManager(agendaRecyclerView.context)
        agendaRecyclerView.adapter = adapter
    }

    companion object {

        private val DAY_ID_PARAM = "day_id"
        private val AGENDA_PARAM = "agenda"

        fun newInstance(agenda: Agenda, dayId: Int): AgendaItemFragment {
            val fragment = AgendaItemFragment()
            val args = Bundle()
            args.putInt(AgendaItemFragment.DAY_ID_PARAM, dayId)
            args.putParcelable(AGENDA_PARAM, agenda)
            fragment.arguments = args
            return fragment
        }
    }
}