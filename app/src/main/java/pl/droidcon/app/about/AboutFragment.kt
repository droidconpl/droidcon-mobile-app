package pl.droidcon.app.about

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks
import com.github.ksoichiro.android.observablescrollview.ScrollState
import com.github.ksoichiro.android.observablescrollview.ScrollUtils
import kotlinx.android.synthetic.main.about_logo.*
import kotlinx.android.synthetic.main.flexible_image_header.*
import kotlinx.android.synthetic.main.fragment_about.*
import pl.droidcon.app.R
import kotlinx.android.synthetic.main.about_after_party.after_party_map as afterPartyMap
import kotlinx.android.synthetic.main.about_venue.venue_map as venueMap
import kotlinx.android.synthetic.main.flexible_image_header.image_foreground as imageForeground

private const val DROIDCON_ADDRESS = "https://www.google.pl/maps/place/Wydzia%C5%82+Prawa+i+Administracji+Uniwersytetu+Jagiello%C5%84skiego/@50.0633584,19.9259511,19z/data=!4m13!1m7!3m6!1s0x47165b0a24b48dbf:0x922162f7fd163e4f!2sKrupnicza+33a,+31-123+Krak%C3%B3w!3b1!8m2!3d50.063076!4d19.9256936!3m4!1s0x0:0x6058041a9a3052de!8m2!3d50.0630339!4d19.925854?hl=en&shorturl=1"
private const val AFTER_PARTY_ADDRESS = "https://www.google.pl/maps/place/Forum+Przestrzenie/@50.0453055,19.933825,17z/data=!3m1!4b1!4m5!3m4!1s0x47165b663ea4d507:0xdc0abe3e0bf6dd37!8m2!3d50.0453021!4d19.9360137?hl=en"

class AboutFragment : Fragment(), ObservableScrollViewCallbacks {

    private var flexiblePhotoHeight = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_about, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scroll.setScrollViewCallbacks(this)
        flexiblePhotoHeight = resources.getDimensionPixelSize(R.dimen.about_flexible_photo_height)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        styleBackground()

        venueMap.setOnClickListener { openMap(DROIDCON_ADDRESS) }
        afterPartyMap.setOnClickListener { openMap(AFTER_PARTY_ADDRESS) }
    }

    private fun styleBackground() = context?.let {
        val transparentColor = getColor(it, android.R.color.transparent)
        val accentColor = getColor(it, R.color.pistachio_dark)

        val gradientDrawable = GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP,
                intArrayOf(accentColor,
                           transparentColor,
                           transparentColor)
        )

        imageForeground.background = gradientDrawable
        container.setBackgroundColor(accentColor)
    }

    private fun openMap(address: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(address)))
    }

    override fun onScrollChanged(scrollY: Int, firstScroll: Boolean, dragging: Boolean) {
        val minOverlayTransitionY = -overlay.height
        overlay.translationY = ScrollUtils.getFloat((-scrollY).toFloat(), minOverlayTransitionY.toFloat(), 0f)
        image_container.translationY = ScrollUtils.getFloat((-scrollY / 2).toFloat(), minOverlayTransitionY.toFloat(), 0f)
        logo.translationY = -ScrollUtils.getFloat((-scrollY / 2).toFloat(), minOverlayTransitionY.toFloat(), 0f)
        logo.alpha = 1 - scrollY.toFloat() * (1.toFloat() / flexiblePhotoHeight) * 1.7f
    }

    override fun onUpOrCancelMotionEvent(scrollState: ScrollState?) {
    }

    override fun onDownMotionEvent() {
    }

    companion object {
        fun instance() = AboutFragment()
    }
}