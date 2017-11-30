package pl.droidcon.app.agenda.view

import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_agenda.*
import kotlinx.android.synthetic.main.fragment_agenda_item.*
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.agenda.AgendaItemPresenter
import pl.droidcon.app.agenda.AgendaItemView
import pl.droidcon.app.agenda.AgendaPresenter
import pl.droidcon.app.agenda.AgendaView
import pl.droidcon.app.domain.Agenda
import pl.droidcon.app.domain.Session
import pl.droidcon.app.domain.TalkPanel
import pl.droidcon.app.sessions.SessionActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        agenda_view_pager.adapter = AgendaPagerAdapter(childFragmentManager)
        agendaPresenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        agendaPresenter.attachView(null)
    }

    override fun display(agenda: Agenda) {
        view?.let {
            agenda_progress.visibility = View.GONE
            agenda_view_pager.visibility = View.VISIBLE
//            agenda_view_pager.adapter = AgendaPagerAdapter(childFragmentManager)
        }
    }

    companion object {
        fun instance() = AgendaFragment()
    }
}

class AgendaPagerAdapter constructor(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = AgendaItemFragment.newInstance(position)

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence {
        return if (position == 0) "Friday " else "Saturday"
    }
}

class AgendaItemFragment : Fragment(), AgendaItemView {


    @Inject
    lateinit var agendaItemPresenter: AgendaItemPresenter

    lateinit var adapter: AgendaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.inject(this)
        adapter = AgendaAdapter(agendaItemPresenter)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_agenda_item, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val dayId = arguments?.getInt(AgendaItemFragment.DAY_ID_PARAM)
        dayId?.let {
            agendaItemPresenter.attachView(this, dayId)
        }
    }

    override fun display(talkPanels: List<TalkPanel>) {
        val size = talkPanels.size
        Log.d("DDD", this.toString() + " " + size)
        view?.let {
            adapter.add(talkPanels)
            agendaView.layoutManager = LinearLayoutManager(agendaView.context)
            agendaView.adapter = adapter
            val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            dividerItemDecoration.setDrawable(resources.getDrawable(R.drawable.item_divider))
            agendaView.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroyView() {
        agendaItemPresenter.attachView(null, 0)

        super.onDestroyView()
    }

    override fun openSession(speakerPicture: ImageView, session: Session) {
        activity?.let {
            val intent = SessionActivity.intent(it.baseContext, session.sessionId)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it, speakerPicture as View, "profile")
            startActivity(intent, options.toBundle())
        }
    }

    companion object {

        private val DAY_ID_PARAM = "day_id"

        fun newInstance(dayId: Int): AgendaItemFragment {
            val fragment = AgendaItemFragment()
            val args = Bundle()
            args.putInt(AgendaItemFragment.DAY_ID_PARAM, dayId)
            fragment.arguments = args
            return fragment
        }
    }
}