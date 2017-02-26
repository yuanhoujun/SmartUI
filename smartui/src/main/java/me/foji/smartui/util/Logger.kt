package me.foji.smartui.util

import android.util.Log
import me.foji.smartui.BuildConfig

/**
 * Logger
 *
 * @author Scott Smith  Creation time: 2017-01-20 12:06
 */
class Logger {
    companion object {
        val TAG = "SmartUI"
        val DEBUG = BuildConfig.DEBUG

        fun e(message: String) {
            if(DEBUG) {
                Log.e(TAG, message)
            }
        }
    }
}