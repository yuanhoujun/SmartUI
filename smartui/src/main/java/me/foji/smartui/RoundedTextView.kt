package me.foji.smartui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity

/**
 * RoundedTextView是在{@link SmartTextView}的基础上实现了圆形背景功能，用于实现一些圆形小圆点提示的功能。
 *
 * @author Scott Smith
 */
class RoundedTextView(context: Context, attrs: AttributeSet?) : SmartTextView(context, attrs) {
    // 填充颜色
    private var fillColor = Color.RED
    // 半径
    private var radius = 0f
    // 文字颜色
    private var mTextColor = 0

    private var mPaint: Paint? = null

    init {
        gravity = Gravity.CENTER

        if (null != attrs) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundedTextView, 0, 0)
            fillColor = typedArray.getColor(R.styleable.RoundedTextView_fillColor, Color.RED)
            radius = typedArray.getDimension(R.styleable.RoundedTextView_radius, 0f)
            typedArray.recycle()
        }

        mPaint = paint
        if (null != textColors) {
            mTextColor = textColors.defaultColor
        }
        setAutoTextSize(true)
    }

    constructor(context: Context): this(context , null)

    fun setFillColor(color: Int) {
        fillColor = color
    }

    fun setRadius(radius: Float) {
        this.radius = radius
    }

    override fun onDraw(canvas: Canvas) {
        val width = width
        val height = height

        if (radius <= 0) {
            radius = (if (width > height) height / 2 else width / 2).toFloat()
        }
        mPaint!!.color = fillColor
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius, mPaint)

        mPaint!!.color = mTextColor
        super.onDraw(canvas)
    }
}
