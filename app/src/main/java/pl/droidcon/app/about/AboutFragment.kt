package pl.droidcon.app.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_about.*
import pl.droidcon.app.R

const val MAP_ADDRESS = "https://www.google.pl/maps/place/Wydzia%C5%82+Prawa+i+Administracji+Uniwersytetu+Jagiello%C5%84skiego/@50.0633584,19.9259511,19z/data=!4m13!1m7!3m6!1s0x47165b0a24b48dbf:0x922162f7fd163e4f!2sKrupnicza+33a,+31-123+Krak%C3%B3w!3b1!8m2!3d50.063076!4d19.9256936!3m4!1s0x0:0x6058041a9a3052de!8m2!3d50.0630339!4d19.925854?hl=en&shorturl=1"

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        address_map.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MAP_ADDRESS)))
        }
    }

    companion object {
        fun instance() = AboutFragment()
    }
}