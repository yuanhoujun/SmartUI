package me.foji.smartui

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.TextView
import me.foji.smartui.util.Logger
import org.jetbrains.anko.dip

/**
 * SmartTextView是对 {@link TextView}的扩展。
 * 目前在{@link TextView}的基础上新增了如下功能：
 * 1）字体大小自动适配
 *
 * @author Scott Smith
 */
open class SmartTextView(context: Context , attrs: AttributeSet?) : TextView(context , attrs) {
    // 遵守Android规范，默认关闭文字大小自动适配
    private var mAutoTextSize: Boolean = false
    // 默认最小字体大小
    private val DEFAULT_MIN_TEXT_SIZE = dip(10f)
    // Paint
    private var mPaint: Paint? = null

    constructor(context: Context): this(context , null)

    init {
        if(null != attrs) {
            val typedArr = context.obtainStyledAttributes(attrs , R.styleable.SmartTextView , 0 , 0)
            mAutoTextSize = typedArr.getBoolean(R.styleable.SmartTextView_autoTextSize , false)
            typedArr.recycle()
        }

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.set(paint)
    }

    private fun autoTextSize(text: CharSequence? , textWidth: Int) {
        var currTextSize = textSize
        Logger.e("当前文字大小：$currTextSize")
        if(currTextSize <= DEFAULT_MIN_TEXT_SIZE || textWidth <= 0 || TextUtils.isEmpty(text)) return

        val availableWidth = textWidth - paddingLeft - paddingRight
        Logger.e("Width = $textWidth , Available Width = $availableWidth")
        mPaint!!.textSize = currTextSize
        while (currTextSize > DEFAULT_MIN_TEXT_SIZE && mPaint!!.measureText(text!!.toString()) > availableWidth) {
            mPaint!!.textSize = --currTextSize
        }
        Logger.e("缩小后的文字大小：$currTextSize")
        setTextSize(TypedValue.COMPLEX_UNIT_PX , currTextSize)
    }

    fun setAutoTextSize(autoTextSize: Boolean) {
        mAutoTextSize = autoTextSize
        if(autoTextSize) {
            autoTextSize(text, width)
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        if(mAutoTextSize) {
            autoTextSize(text, width)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(mAutoTextSize && w != oldw) {
            autoTextSize(text , w)
        }
    }
}