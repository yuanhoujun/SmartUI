package me.foji.android.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_tab_bar.*
import java.util.*

/**
 * TabBarActivity
 *
 * @author Scott Smith
 */
class TabBarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_bar)
        setTitle(R.string.tab_bar)

        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(DiscoverFragment())
        fragments.add(MessageFragment())
        fragments.add(MineFragment())
        view_pager.adapter = ViewPagerAdapter(supportFragmentManager , fragments)

        view_pager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(position) {
                    0 -> tab_bar_home.isChecked = true
                    1 -> tab_bar_discover.isChecked = true
                    2 -> tab_bar_message.isChecked = true
                    3 -> tab_bar_mine.isChecked = true
                }
            }
        })

        val listener = View.OnClickListener { view ->
            when(view.id) {
                R.id.tab_bar_home -> view_pager.setCurrentItem(0 , false)
                R.id.tab_bar_discover -> view_pager.setCurrentItem(1 , false)
                R.id.tab_bar_message -> view_pager.setCurrentItem(2 , false)
                R.id.tab_bar_mine -> view_pager.setCurrentItem(3 , false)
            }
        }

        tab_bar_home.setOnClickListener(listener)
        tab_bar_discover.setOnClickListener(listener)
        tab_bar_message.setOnClickListener(listener)
        tab_bar_mine.setOnClickListener(listener)
    }

    class ViewPagerAdapter(fm: FragmentManager , fragments: ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
        private var mFragments: ArrayList<Fragment> = fragments

        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }
    }
}