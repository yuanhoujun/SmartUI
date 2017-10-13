package me.foji.android.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_basic_widget.setOnClickListener {
            startActivity<BasicWidgetActivity>()
        }

        btn_container_widget.setOnClickListener {

        }

        btn_mix_widget.setOnClickListener {
            startActivity<MixActivity>()
        }

        btn_dialog.setOnClickListener {
            startActivity<DialogActivity>()
        }
    }
}
