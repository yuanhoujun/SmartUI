package me.foji.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mix.*
import org.jetbrains.anko.startActivity

/**
 * MixActivity
 *
 * @author Scott Smith
 */
class MixActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mix)
        setTitle(R.string.mix_widget)

        btn_tab_bar.setOnClickListener {
            startActivity<TabBarActivity>()
        }
    }
}