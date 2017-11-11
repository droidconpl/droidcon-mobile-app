package pl.droidcon.app

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.MenuItem
import pl.droidcon.app.about.AboutFragment
import pl.droidcon.app.sessions.view.SessionsFragment
import pl.droidcon.app.speakers.view.SpeakersFragment

class NavigationPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = NavigationItem.get(position).instance()

    override fun getCount() = NavigationItem.size()
}

class NavigationPageChangeListener(private val bottomNavigationView: BottomNavigationView) : ViewPager.OnPageChangeListener {

    private var lastItem: MenuItem? = null

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        lastItem?.isChecked = false

        lastItem = bottomNavigationView.menu.getItem(position)
        lastItem?.isChecked = true
    }
}

fun MenuItem.findPosition(): Int? = toNavigationItem()?.toPagerItem()

private fun MenuItem.toNavigationItem(): NavigationItem? = when (itemId) {
    R.id.menu_agenda -> NavigationItem.AGENDA
    R.id.menu_speakers -> NavigationItem.SPEAKERS
    R.id.menu_about -> NavigationItem.ABOUT
    else -> null
}

private fun NavigationItem.toPagerItem(): Int = when (this) {
    NavigationItem.AGENDA -> 0
    NavigationItem.SPEAKERS -> 1
    NavigationItem.ABOUT -> 2
}

private enum class NavigationItem {
    AGENDA {
        override fun instance() = SessionsFragment.instance()
    },
    SPEAKERS {
        override fun instance() = SpeakersFragment.instance()
    },
    ABOUT {
        override fun instance() = AboutFragment.instance()
    };

    abstract fun instance(): Fragment

    companion object {

        fun get(position: Int): NavigationItem = when (position) {
            0 -> AGENDA
            1 -> SPEAKERS
            2 -> ABOUT
            else -> throw IllegalStateException()
        }

        fun size() = values().size
    }
}