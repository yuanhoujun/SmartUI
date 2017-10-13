package me.foji.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dialog.*

/**
 * 对话框Demo
 *
 * @author Scott Smith 2017-10-13 10:26
 */
class DialogActivity: AppCompatActivity() {
    private var mBottomUpDialog: BottomUpDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        initView()
        initListener()
    }

    private fun initView() {
        mBottomUpDialog = BottomUpDialog(this)
    }

    private fun initListener() {
        btn_botttom_up.setOnClickListener {
            mBottomUpDialog?.show()
        }
    }
}
