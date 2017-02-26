package me.foji.smartui

import android.app.Dialog
import android.content.Context
import android.view.View

/**
 * 底部弹窗基类
 *
 * @author Scott Smith 2017-02-26 17:10
 */
abstract class BasePushUpDialog(context: Context) : Dialog(context) {

    init {

    }

    abstract fun onBindView(): View?
}