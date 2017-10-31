package pl.droidcon.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import pl.droidcon.app.about.AboutFragment
import pl.droidcon.app.sessions.view.SessionsFragment
import pl.droidcon.app.speakers.view.SpeakersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        viewpager.adapter = NavigationPagerAdapter(supportFragmentManager)

        bottom_navigation.setOnNavigationItemSelectedListener({
            when (it.itemId) {
                R.id.menu_agenda -> viewpager.setCurrentItem(0, false)
                R.id.menu_speakers -> viewpager.setCurrentItem(1, false)
                R.id.menu_about -> viewpager.setCurrentItem(2, false)
            }
            false
        })

        viewpager.addOnPageChangeListener(NavigationPageChangeListener(bottom_navigation))
    }
}

private class NavigationPageChangeListener(private val bottomNavigationView: BottomNavigationView) : ViewPager.OnPageChangeListener {

    private var lastItem: MenuItem? = null

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        lastItem?.isChecked = false

        lastItem = bottomNavigationView.menu.getItem(position)
        lastItem?.isChecked = true
    }

}

private class NavigationPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = when (position) {
        0 -> SessionsFragment.instance()
        1 -> SpeakersFragment.instance()
        2 -> AboutFragment.instance()
        else -> throw IllegalStateException()
    }

    override fun getCount() = 3
}