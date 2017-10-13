package me.foji.android.ui

import android.content.Context
import me.foji.smartui.BasePushUpDialog

/**
 * 底部弹出对话框
 *
 * @author Scott Smith 2017-10-13 10:39
 */
class BottomUpDialog(context: Context): BasePushUpDialog(context) {

    override fun onBindView(): Int {
        return R.layout.dialog_bottom_up
    }
}