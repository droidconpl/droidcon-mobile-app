package pl.droidcon.app.sessions.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.domain.Session
import pl.droidcon.app.sessions.SessionsPresenter
import pl.droidcon.app.sessions.SessionsView
import javax.inject.Inject

class SessionsFragment : Fragment(), SessionsView {

    @Inject lateinit var presenter: SessionsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_speakers, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.attachView(null)
    }

    override fun displaySessions(sessions: List<Session>) {
        println("display $sessions")
    }

    companion object {
        fun instance() = SessionsFragment()
    }
}