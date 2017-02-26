package me.foji.smartui

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import android.util.AttributeSet
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import org.jetbrains.anko.dip


/**
 * SmartButton意图解决每次创建圆角按钮、文字颜色样式引入xml大量文件的问题，使用SmartButton可以用解决一些常用
 * 的圆角按钮需求，如果需要更多官方按钮功能，请使用@{link Button}。

 * @author Scott Smith 2017-02-26 16:56
 */
class SmartButton : SmartTextView {
    private var mRadius: Float = 0f
    private var mPressedBackgroundColor: Int = Color.WHITE
    private var mNormalBackgroundColor: Int = Color.WHITE
    private var mDisableBackgroundColor: Int = Color.WHITE
    private var mPressedTextColor: Int = Color.BLACK
    private var mNormalTextColor: Int = Color.BLACK
    private var mDisableTextColor: Int = Color.BLACK
    // 外边框线条颜色
    private var mOutBorderLineColor: Int = Color.WHITE
    // 外边框线条宽度
    private var mOutBorderLineWidth: Float = 0f
    // 外边框线条类型（实线或者虚线）
    private var mOutBorderLineType: Int = LINE_TYPE_SOLID
    private var mOutBorderLineDashWidth: Float = dip(10f).toFloat()
    private var mOutBorderLineDashGap: Float = dip(5f).toFloat()
    private var mBackgroundDrawable: StateListDrawable? = null
    private var mTextColor: ColorStateList? = null
    private var mNormalBackgroundDrawable: GradientDrawable? = null
    private var mPressedBackgroundDrawable: GradientDrawable? = null
    private var mDisableBackgroundDrawable: GradientDrawable? = null

    companion object {
        /**
         * 实线
         */
        val LINE_TYPE_SOLID = 1
        /**
         * 虚线
         */
        val LINE_TYPE_DOTTED = 2
        /**
         * 正常状态
         */
        val STATE_NORMAL = 1
        /**
         * 按下状态
         */
        val STATE_PRESSED = 2
        /**
         * 关闭状态
         */
        val STATE_DISABLE = 3
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (null != attrs) {
            val typedArr = context.obtainStyledAttributes(attrs, R.styleable.SmartButton, 0, 0)
            mRadius = typedArr.getDimension(R.styleable.SmartButton_smart_ui_radius, 0f)
            mPressedBackgroundColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_pressed_background_color, Color.WHITE)
            mNormalBackgroundColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_normal_background_color, Color.WHITE)
            mDisableBackgroundColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_disable_background_color, Color.WHITE)
            mPressedTextColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_pressed_text_color, Color.BLACK)
            mNormalTextColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_normal_text_color, Color.BLACK)
            mDisableTextColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_disable_text_color, Color.BLACK)
            mOutBorderLineColor = typedArr.getColor(
                    R.styleable.SmartButton_smart_ui_out_border_line_color, Color.WHITE)
            mOutBorderLineWidth = typedArr.getDimension(
                    R.styleable.SmartButton_smart_ui_out_border_line_width, 0f)
            mOutBorderLineType = typedArr.getInt(
                    R.styleable.SmartButton_smart_ui_out_border_line_type , -1)
            mOutBorderLineDashWidth = typedArr.getDimension(
                    R.styleable.SmartButton_smart_ui_dash_width, dip(10f).toFloat())
            mOutBorderLineDashGap = typedArr.getDimension(
                    R.styleable.SmartButton_smart_ui_dash_gap, dip(5f).toFloat())
            typedArr.recycle()
        }
        init()
    }

    private fun init() {
        isClickable = true
        // 设置背景颜色
        mBackgroundDrawable = StateListDrawable()

        mPressedBackgroundDrawable = GradientDrawable()
        mPressedBackgroundDrawable!!.setColor(mPressedBackgroundColor)
        mPressedBackgroundDrawable!!.cornerRadius = mRadius

        mNormalBackgroundDrawable = GradientDrawable()
        mNormalBackgroundDrawable!!.setColor(mNormalBackgroundColor)
        mNormalBackgroundDrawable!!.cornerRadius = mRadius

        mDisableBackgroundDrawable = GradientDrawable()
        mDisableBackgroundDrawable!!.setColor(mDisableBackgroundColor)
        mDisableBackgroundDrawable!!.cornerRadius = mRadius

        invalidateOutBorderLine()

        mBackgroundDrawable!!.addState(intArrayOf(android.R.attr.state_pressed ,
                android.R.attr.state_enabled), mPressedBackgroundDrawable)
        mBackgroundDrawable!!.addState(intArrayOf(-android.R.attr.state_pressed ,
                android.R.attr.state_enabled), mNormalBackgroundDrawable)
        mBackgroundDrawable!!.addState(intArrayOf(-android.R.attr.state_enabled),
                mDisableBackgroundDrawable)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = mBackgroundDrawable
        } else {
            setBackgroundDrawable(mBackgroundDrawable)
        }

        // 设置文字颜色
        setTextColor(mNormalTextColor , mPressedTextColor , mDisableTextColor)
    }

    /**
     * 设置指定状态下背景颜色
     *
     * @state 当前按钮状态 @{link #STATE_NORMAL , #STATE_PRESSED , #STATE_DISABLE}
     * @color 背景颜色
     */
    fun setBackgroundColor(state: Int , @ColorInt color: Int) {
        when(state) {
            STATE_NORMAL -> {
                mNormalBackgroundColor = color
                mNormalBackgroundDrawable!!.setColor(color)
            }
            STATE_PRESSED -> {
                mPressedBackgroundColor = color
                mPressedBackgroundDrawable!!.setColor(color)

            }
            STATE_DISABLE -> {
                mDisableBackgroundColor = color
                mDisableBackgroundDrawable!!.setColor(color)
            }
        }
    }

    /**
     * 设置指定状态下文字颜色
     *
     * @state 当前按钮状态 @{link #STATE_NORMAL , #STATE_PRESSED , #STATE_DISABLE}
     * @color 文字颜色
     */
    fun setTextColor(state: Int , @ColorInt color: Int) {
        when(state) {
            STATE_NORMAL -> {
                mNormalTextColor = color
            }
            STATE_PRESSED -> {
                mPressedTextColor = color

            }
            STATE_DISABLE -> {
                mDisableTextColor = color
            }
        }

        setTextColor(mNormalTextColor , mPressedTextColor , mDisableTextColor)
    }

    private fun setTextColor(normalTextColor: Int ,
                             pressedTextColor: Int ,
                             disableTextColor: Int) {
        val states = arrayOf(intArrayOf(android.R.attr.state_enabled, android.R.attr.state_pressed),
                intArrayOf(android.R.attr.state_enabled, -android.R.attr.state_pressed),
                intArrayOf(-android.R.attr.state_enabled))
        val colors = intArrayOf(pressedTextColor, normalTextColor, disableTextColor)

        mTextColor = ColorStateList(states, colors)
        setTextColor(mTextColor)
    }

    /**
     * 设置按钮弧度
     *
     * @param radius 弧度
     */
    fun setRadius(@FloatRange(from = 0.0) radius: Float) {
        mRadius = radius
        mPressedBackgroundDrawable!!.cornerRadius = mRadius
        mNormalBackgroundDrawable!!.cornerRadius = mRadius
        mDisableBackgroundDrawable!!.cornerRadius = mRadius
    }

    /**
     * 设置外边框线条宽度
     *
     * @param width 线条宽度
     */
    fun setOutBorderLineWidth(width: Float) {
        mOutBorderLineWidth = width
        invalidateOutBorderLine()
    }

    /**
     * 设置外边框线条Gap
     *
     * @param gap 虚线点间隔
     */
    fun setOutBorderLineGap(gap: Float) {
        mOutBorderLineDashGap = gap
        invalidateOutBorderLine()
    }

    /**
     * 设置外边框线条虚线点宽度
     *
     * @param dashWidth 虚线点宽度
     */
    fun setOutBorderLineDashWidth(dashWidth: Float) {
        mOutBorderLineDashWidth = dashWidth
        invalidateOutBorderLine()
    }

    /**
     * 设置线条类型 (实现或者虚线)
     *
     * @param type 线条类型 @{link #LINE_TYPE_SOLID , #LINE_TYPE_DOTTED}
     */
    fun setOutBorderLineType(type: Int) {
        mOutBorderLineType = type
    }

    private fun invalidateOutBorderLine() {
        if(mOutBorderLineWidth > 0) {
            if(LINE_TYPE_DOTTED == mOutBorderLineType) {
                mPressedBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor, mOutBorderLineDashWidth, mOutBorderLineDashGap)
                mNormalBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor, mOutBorderLineDashWidth, mOutBorderLineDashGap)
                mDisableBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor, mOutBorderLineDashWidth, mOutBorderLineDashGap)
            } else {
                mPressedBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor)
                mNormalBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor)
                mDisableBackgroundDrawable!!.setStroke(mOutBorderLineWidth.toInt(),
                        mOutBorderLineColor)
            }
        }
    }
}
