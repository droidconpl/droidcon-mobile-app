package pl.droidcon.app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupNavigation()
    }

    private fun setupNavigation() {
        viewpager.adapter = NavigationPagerAdapter(supportFragmentManager)

        bottom_navigation.setOnNavigationItemSelectedListener({
            it.findPosition()?.run {
                viewpager.setCurrentItem(this, false)
            }
            false
        })

        viewpager.addOnPageChangeListener(NavigationPageChangeListener(bottom_navigation))
    }
}