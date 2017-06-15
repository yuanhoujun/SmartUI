package me.foji.smartui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import org.jetbrains.anko.dip
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.*

/**
 * 更强大，更智能的WebView
 *
 * @author Scott Smith 2017-04-11 11:27
 */
open class SmartWebView(context: Context , attrs: AttributeSet?): WebView(context , attrs) {
    private var mProgressBar: ProgressBar? = null
    private var mDisplayProgressBar = true
    private var mProgressBarColor = Color.RED
    private var mProgressBarBackgroundColor = Color.YELLOW
    private var mProgressBarHeight = 0f

    private var mWebViewClient: WebViewClient? = null
    private var mWebChromeClient: WebChromeClient? = null

    private var mSchemes: ArrayList<WebViewScheme> = ArrayList()
    private var interceptors: ArrayList<UrlInterceptor> = ArrayList()

    /**
     * It's not perfect！！！
     */
    interface UrlInterceptor {
        fun intercept(url: String?)
    }

    init {
        if(null != attrs) {
            val typedArr = context.obtainStyledAttributes(attrs , R.styleable.SmartWebView , 0 , 0)
            mDisplayProgressBar = typedArr.getBoolean(
                    R.styleable.SmartWebView_smart_ui_display_progress_bar , true)
            mProgressBarColor = typedArr.getColor(
                    R.styleable.SmartWebView_smart_ui_progress_bar_color, Color.RED)
            mProgressBarBackgroundColor = typedArr.getColor(
                    R.styleable.SmartWebView_smart_ui_progress_bar_background_color, Color.YELLOW)
            mProgressBarHeight = typedArr.getDimension(
                    R.styleable.SmartWebView_smart_ui_progress_bar_height , dip(3).toFloat())
            typedArr.recycle()
        }

        initView()
        prepare()
    }

    private fun initView() {
        if(mDisplayProgressBar) {
            mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
            mProgressBar!!.layoutParams = ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, mProgressBarHeight.toInt())
            setProgressBarColor(mProgressBarColor)
            setProgressBarBackgroundColor(mProgressBarBackgroundColor)
            addView(mProgressBar)
        }
    }

    private fun prepare() {
        super.setWebViewClient(object: WebViewClient() {
            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                mWebViewClient?.doUpdateVisitedHistory(view , url , isReload)
            }

            override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
                mWebViewClient?.onFormResubmission(view, dontResend, resend)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                mWebViewClient?.onLoadResource(view, url)
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mWebViewClient?.onPageCommitVisible(view, url)
                }
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                mWebViewClient?.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                mWebViewClient?.onPageStarted(view, url, favicon)
            }

            override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mWebViewClient?.onReceivedClientCertRequest(view, request)
                }
            }

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mWebViewClient?.onReceivedError(view, request, error)
                }
            }

            override fun onReceivedHttpAuthRequest(view: WebView?, handler: HttpAuthHandler?, host: String?, realm: String?) {
                mWebViewClient?.onReceivedHttpAuthRequest(view, handler, host, realm)
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mWebViewClient?.onReceivedHttpError(view, request, errorResponse)
                }
            }

            override fun onReceivedLoginRequest(view: WebView?, realm: String?, account: String?, args: String?) {
                mWebViewClient?.onReceivedLoginRequest(view, realm, account, args)
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                mWebViewClient?.onReceivedSslError(view,  handler, error)
            }

            override fun onScaleChanged(view: WebView?, oldScale: Float, newScale: Float) {
                mWebViewClient?.onScaleChanged(view, oldScale, newScale)
            }

            override fun onUnhandledKeyEvent(view: WebView?, event: KeyEvent?) {
                mWebViewClient?.onUnhandledKeyEvent(view, event)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    interceptors.forEach { it.intercept(request.url?.toString()) }
                }

                if(match(request.url)) return true

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return mWebViewClient?.shouldOverrideUrlLoading(view, request) ?: false
                }
                return false
            }

            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return mWebViewClient?.shouldInterceptRequest(view, request)
                }
                return null
            }

            override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
                return mWebViewClient?.shouldOverrideKeyEvent(view, event) ?: false

            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                interceptors.forEach { it.intercept(url) }

                if(match(url)) return true

                return mWebViewClient?.shouldOverrideUrlLoading(view, url) ?: false
            }
        })

        super.setWebChromeClient(object: WebChromeClient() {
            override fun getDefaultVideoPoster(): Bitmap? {
                return mWebChromeClient?.defaultVideoPoster
            }

            override fun onCloseWindow(window: WebView?) {
                mWebChromeClient?.onCloseWindow(window)
            }

            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                return mWebChromeClient?.onConsoleMessage(consoleMessage) ?: false
            }

            override fun onCreateWindow(view: WebView?, isDialog: Boolean, isUserGesture: Boolean, resultMsg: Message?): Boolean {
                return mWebChromeClient?.onCreateWindow(view, isDialog, isUserGesture, resultMsg) ?: false
            }

            override fun onGeolocationPermissionsHidePrompt() {
                mWebChromeClient?.onGeolocationPermissionsHidePrompt()
            }

            override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
                mWebChromeClient?.onGeolocationPermissionsShowPrompt(origin, callback)
            }

            override fun onHideCustomView() {
                mWebChromeClient?.onHideCustomView()
            }

            override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return mWebChromeClient?.onJsAlert(view, url, message, result) ?: false
            }

            override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return mWebChromeClient?.onJsBeforeUnload(view, url, message, result) ?: false
            }

            override fun onJsConfirm(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
                return mWebChromeClient?.onJsConfirm(view, url, message, result) ?: false
            }

            override fun onJsPrompt(view: WebView?, url: String?, message: String?, defaultValue: String?, result: JsPromptResult?): Boolean {
                return mWebChromeClient?.onJsPrompt(view, url, message, defaultValue, result) ?: false
            }

            override fun onPermissionRequest(request: PermissionRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mWebChromeClient?.onPermissionRequest(request)
                }
            }

            override fun onPermissionRequestCanceled(request: PermissionRequest?) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mWebChromeClient?.onPermissionRequestCanceled(request)
                }
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                mWebChromeClient?.onProgressChanged(view, newProgress)
                updateProgress(newProgress)
            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                mWebChromeClient?.onReceivedIcon(view, icon)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                mWebChromeClient?.onReceivedTitle(view, title)
            }

            override fun onReceivedTouchIconUrl(view: WebView?, url: String?, precomposed: Boolean) {
                mWebChromeClient?.onReceivedTouchIconUrl(view, url, precomposed)
            }

            override fun onRequestFocus(view: WebView?) {
                mWebChromeClient?.onRequestFocus(view)
            }

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                mWebChromeClient?.onShowCustomView(view, callback)
            }

            override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    return mWebChromeClient?.onShowFileChooser(webView, filePathCallback, fileChooserParams) ?: false
                }

                return false
            }
        })
        useDefaultSettings()
    }

    private fun useDefaultSettings() {
        val settings = settings
        settings.javaScriptEnabled = true
        settings.javaScriptCanOpenWindowsAutomatically = true
    }

    private fun match(url: String): Boolean {
        return match(Uri.parse(url))
    }

    private fun match(uri: Uri): Boolean {
        var matched = false

        mSchemes.filter { it.schemeValue == uri.scheme && it.action == uri.authority }.forEach {
            it.callback.invoke(uri.scheme, uri.authority, paramsToMap(uri.encodedQuery))
            matched = true
        }

        return matched
    }

    private fun paramsToMap(query: String?): Map<String , String> {
        val map = HashMap<String , String>()
        if(!TextUtils.isEmpty(query)) {
            val queryArr = query!!.split("&")
            queryArr.map { it.split("=") }
                    .filter { it.size > 1 }
                    .forEach { map.put(it[0] , URLDecoder.decode(it[1], "UTF-8")) }
        }

        return map
    }

    private fun updateProgress(progress: Int) {
        if(mDisplayProgressBar) {
            mProgressBar?.visibility = View.VISIBLE
            if(progress < 100) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mProgressBar?.setProgress(progress, true)
                } else {
                    mProgressBar?.progress = progress
                }
            } else {
                mProgressBar?.visibility = View.GONE
            }
        }
    }

    fun setDisplayProgressBar(display: Boolean) {
        if(!display) {
            if(null != mProgressBar && indexOfChild(mProgressBar) >= 0) {
                removeView(mProgressBar)
            }
        } else {
            mProgressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
            mProgressBar!!.layoutParams = ViewGroup.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, mProgressBarHeight.toInt())
            setProgressBarColor(mProgressBarColor)
            setProgressBarBackgroundColor(mProgressBarBackgroundColor)
            addView(mProgressBar)
        }

        mDisplayProgressBar = display
    }

    fun setProgressBarHeight(height: Int) {
        if(mDisplayProgressBar) {
            val lp = mProgressBar?.layoutParams
            lp?.height = height
            mProgressBar?.layoutParams = lp
        }
    }

    fun setProgressBarBackgroundColor(color: Int) {
        if(mDisplayProgressBar) {
            mProgressBar?.setBackgroundColor(color)
        }
    }

    fun setProgressBarColor(color: Int) {
        if(mDisplayProgressBar) {
            val progressDrawable = mProgressBar?.progressDrawable
            progressDrawable?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
    }

    override fun setWebViewClient(client: WebViewClient?) {
        mWebViewClient = client
    }

    override fun setWebChromeClient(client: WebChromeClient?) {
        mWebChromeClient = client
    }

    /**
     * 注册一个自定义协议，H5可以使用这个协议调用Native代码
     * 注意：回调接口中的params参数的key,value均为encode后的参数，请自行decode
     *
     * @param scheme 自定义Scheme
     * @param action 操作Action
     * @param callback 自定义操作回调
     */
    fun registerScheme(scheme: String,
                       action: String,
                       callback: (scheme: String,
                                  action: String,
                                  params: Map<String, String>)->Unit) {
        val schemeObj = WebViewScheme(scheme, action, callback)
        if(!mSchemes.contains(schemeObj)) {
            mSchemes.add(schemeObj)
        }
    }

    fun addIntercetor(interceptor: UrlInterceptor) {
        interceptors.add(interceptor)
    }
}