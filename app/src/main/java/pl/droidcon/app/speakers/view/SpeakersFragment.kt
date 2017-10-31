package pl.droidcon.app.speakers.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_speakers.*
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speakers.SpeakersPresenter
import pl.droidcon.app.speakers.SpeakersView
import pl.droidcon.app.widget.SpacesItemDecoration
import javax.inject.Inject

class SpeakersFragment : Fragment(), SpeakersView {

    @Inject lateinit var presenter: SpeakersPresenter

    private var adapter: SpeakersAdapter? = null

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

    override fun display(speakers: List<Speaker>) {
        view?.let {
            speakersView.layoutManager = GridLayoutManager(it.context, 2)
            speakersView.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing)))
            adapter = SpeakersAdapter(speakers)
            speakersView.adapter = adapter
        }
    }

    override fun display(speaker: Speaker) {

    }

    companion object {
        fun instance() = SpeakersFragment()
    }
}
