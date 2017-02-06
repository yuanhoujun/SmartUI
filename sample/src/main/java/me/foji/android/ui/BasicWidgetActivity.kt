package me.foji.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * BasicWidgetActivity
 *
 * @author Scott Smith
 */
class BasicWidgetActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_widget)
        setTitle(R.string.basic_widget)
    }
}