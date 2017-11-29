package pl.droidcon.app.speakers.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_speakers.*
import pl.droidcon.app.DroidconApp
import pl.droidcon.app.R
import pl.droidcon.app.domain.Speaker
import pl.droidcon.app.speaker.SpeakerActivity
import pl.droidcon.app.speakers.SpeakersPresenter
import pl.droidcon.app.speakers.SpeakersView
import pl.droidcon.app.widget.SpacesItemDecoration
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SpeakersFragment : Fragment(), SpeakersView {

    @Inject lateinit var presenter: SpeakersPresenter

    private val selectionDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        DroidconApp.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_speakers, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onDestroyView() {
        selectionDisposable.clear()
        presenter.attachView(null)
        super.onDestroyView()
    }

    override fun display(speakers: List<Speaker>) {
        view?.let {
            speakersView.visibility = View.VISIBLE
            speakers_progress.visibility = View.GONE

            selectionDisposable.clear()

            speakersView.layoutManager = GridLayoutManager(it.context, 2)
            speakersView.addItemDecoration(SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing)))

            val adapter = SpeakersAdapter(speakers)
            adapter.speakerSelection()
                    .debounce(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ speaker -> presenter.onSpeakerSelected(speaker) })
                    .addTo(selectionDisposable)

            speakersView.adapter = adapter
        }
    }

    override fun display(speaker: Speaker) {
        context?.let { startActivity(SpeakerActivity.intent(it, speaker.id)) }
    }

    companion object {
        fun instance() = SpeakersFragment()
    }
}