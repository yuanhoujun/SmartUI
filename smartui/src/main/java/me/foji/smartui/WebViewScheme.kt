package me.foji.smartui

/**
 * 自定义Scheme，完成自定义操作
 *
 * @author Scott Smith 2017-04-11 17:11
 */
class WebViewScheme(val schemeValue: String,
                    val action: String,
                    val callback: (scheme: String, action: String, params: Map<String, String>)->Unit) {

    override fun equals(other: Any?): Boolean {
        if(other is WebViewScheme) {
            if(schemeValue == other.schemeValue && action == other.action) {
                return true
            }
        }

        return false
    }
}