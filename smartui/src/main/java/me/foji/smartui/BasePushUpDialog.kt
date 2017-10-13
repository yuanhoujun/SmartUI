package me.foji.smartui

import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.WindowManager

/**
 * 底部弹窗基类
 *
 * @author Scott Smith 2017-02-26 17:10
 */
abstract class BasePushUpDialog(context: Context) : Dialog(context, R.style.DialogStyle) {

    init {
        setContentView(onBindView())
    }

    abstract fun onBindView(): Int

    override fun show() {
        val lp = WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        lp.copyFrom(window.attributes)
        lp.width = screenWidth()

        lp.gravity = Gravity.BOTTOM
        window.attributes = lp

        super.show()
    }

    private fun screenWidth(): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)

        return metrics.widthPixels
    }
}