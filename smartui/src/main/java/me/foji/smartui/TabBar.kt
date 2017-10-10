package me.foji.smartui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.SoundEffectConstants
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import me.foji.smartui.util.Logger
import org.jetbrains.anko.dip

/**
 * TabBar控件拥有两种状态：选中和未选中。
 * TabBar的用法类似于{@link android.widget.RadioButton}, 常常和{@link TabBarGroup}搭配使用，主要用于构建APP的底部栏。
 *
 * @author Scott Smith
 */
class TabBar : ViewGroup , Checkable , Drawable.Callback {
    private var mImageView: ImageView? = null
    private var mTextView: TextView? = null
    private var mBadgeView: RoundedTextView? = null

    /**
     * 图标与文字垂直间隔距离
     */
    private var mVerticalSpace: Int = 0

    /**
     * 圆形标记与TabBar右侧间距
     */
    private var mBadgeMarginRight = 0

    /**
     * 圆形标记与TabBar上方间距
     */
    private var mBadgeMarginTop = 0

    /**
     * 文字颜色
     */
    private var mTextColor: ColorStateList? = null

    /**
     * 图标资源
     */
    private var mImageDrawable: Drawable? = null

    /**
     * 当前是否选中
     */
    private var isChecked = false

    /**
     * 当前状态下文字颜色
     */
    private var mCurrTextColor: Int = 0

    // On check changed listener
    var onCheckChangeListener: OnCheckChangeListener? = null

    private val sScaleTypeArray = arrayOf(ImageView.ScaleType.MATRIX,
                                          ImageView.ScaleType.FIT_XY,
                                          ImageView.ScaleType.FIT_START,
                                          ImageView.ScaleType.FIT_CENTER,
                                          ImageView.ScaleType.FIT_END,
                                          ImageView.ScaleType.CENTER,
                                          ImageView.ScaleType.CENTER_CROP,
                                          ImageView.ScaleType.CENTER_INSIDE)

    private val CHECKED_STATE_SET = intArrayOf(android.R.attr.state_checked)

    interface OnCheckChangeListener {
        fun onCheckChange(view: TabBar, checked: Boolean)
    }

    constructor(context: Context) : this(context , null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context , attrs)
    }

    private fun init(context: Context , attrs: AttributeSet?) {
        var verticalSpace: Int = dip(3)
        var text: String? = null
        var textColor: ColorStateList? = null
        var textSize: Float = dip(14f).toFloat()
        var imageDrawable: Drawable? = null
        var imageWidth: Int = LayoutParams.WRAP_CONTENT
        var imageHeight: Int = LayoutParams.WRAP_CONTENT
        var scaleType: ImageView.ScaleType = ImageView.ScaleType.CENTER
        var badgeVisible: Boolean = false
        var badgeMarginRight: Int = dip(5f)
        var badgeMarginTop: Int = badgeMarginRight
        var badgeRadius: Int = dip(5f)
        var badgeText: String? = null
        var badgeTextColor: Int = Color.WHITE
        var badgeTextSize: Float = dip(12f).toFloat()
        var badgeFillColor: Int = Color.RED

        if(null != attrs) {
            val arr = context.obtainStyledAttributes(attrs , R.styleable.TabBar , 0 , 0)
            for(index in 0..arr.indexCount) {
                when(arr.getIndex(index)) {
                    R.styleable.TabBar_verticalSpace -> {
                        verticalSpace = arr.getDimension(R.styleable.TabBar_verticalSpace , dip(3).toFloat()).toInt()
                    }
                    R.styleable.TabBar_text -> {
                        text = arr.getString(R.styleable.TabBar_text)
                    }
                    R.styleable.TabBar_textColor -> {
                        textColor = arr.getColorStateList(R.styleable.TabBar_textColor)
                    }
                    R.styleable.TabBar_textSize -> {
                        textSize = arr.getDimension(R.styleable.TabBar_textSize , dip(14f).toFloat())
                    }
                    R.styleable.TabBar_imageDrawable -> {
                        imageDrawable = arr.getDrawable(R.styleable.TabBar_imageDrawable)
                    }
                    R.styleable.TabBar_imageWidth -> {
                        imageWidth = arr.getDimension(R.styleable.TabBar_imageWidth , LayoutParams.WRAP_CONTENT.toFloat()).toInt()
                    }
                    R.styleable.TabBar_imageHeight -> {
                        imageHeight = arr.getDimension(R.styleable.TabBar_imageHeight , LayoutParams.WRAP_CONTENT.toFloat()).toInt()
                    }
                    R.styleable.TabBar_imageScaleType -> {
                        val index = arr.getInt(R.styleable.TabBar_imageScaleType , -1)
                        if(index > 0) {
                            scaleType = sScaleTypeArray[index]
                        }
                    }
                    R.styleable.TabBar_badgeVisible -> {
                        badgeVisible = arr.getBoolean(R.styleable.TabBar_badgeVisible , false)
                    }
                    R.styleable.TabBar_badgeMarginRight -> {
                        badgeMarginRight = arr.getDimension(R.styleable.TabBar_badgeMarginRight , dip(5f).toFloat()).toInt()
                    }
                    R.styleable.TabBar_badgeMarginTop -> {
                        badgeMarginTop = arr.getDimension(R.styleable.TabBar_badgeMarginTop , dip(5f).toFloat()).toInt()
                    }
                    R.styleable.TabBar_badgeRadius -> {
                        badgeRadius = arr.getDimension(R.styleable.TabBar_badgeRadius , dip(5f).toFloat()).toInt()
                    }
                    R.styleable.TabBar_badgeText -> {
                        badgeText = arr.getString(R.styleable.TabBar_badgeText)
                    }
                    R.styleable.TabBar_badgeTextColor -> {
                        badgeTextColor = arr.getColor(R.styleable.TabBar_badgeTextColor , Color.WHITE)
                    }
                    R.styleable.TabBar_badgeTextSize -> {
                        badgeTextSize = arr.getDimension(R.styleable.TabBar_badgeTextSize , dip(12f).toFloat())
                    }
                    R.styleable.TabBar_badgeFillColor -> {
                        badgeFillColor = arr.getColor(R.styleable.TabBar_badgeFillColor , Color.RED)
                    }
                }
            }

            arr.recycle()
        }

        // Initialize ImageView
        mImageView = ImageView(context)
        mImageView!!.scaleType = scaleType
        if(null != imageDrawable) {
            setDrawable(imageDrawable)
        }
        mImageView!!.layoutParams = MarginLayoutParams(imageWidth , imageHeight)
        addView(mImageView)

        // Initialize TextView
        mTextView = TextView(context)
        mTextView!!.text = text
        mTextView!!.layoutParams = MarginLayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT)
        addView(mTextView)

        setTextColor(if (textColor != null) textColor else ColorStateList.valueOf(0xFF000000.toInt()))
        mTextView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX , textSize)

        // Initialize BadgeView
        mBadgeView = RoundedTextView(context)
        mBadgeView!!.layoutParams = MarginLayoutParams(badgeRadius * 2 , badgeRadius * 2)
        addView(mBadgeView)

        if(badgeVisible) {
            mBadgeView!!.setRadius(badgeRadius.toFloat())
            mBadgeView!!.setFillColor(badgeFillColor)
            mBadgeView!!.text = badgeText
            mBadgeView!!.setTextColor(badgeTextColor)
            mBadgeView!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, badgeTextSize)
        } else {
            mBadgeView!!.visibility = View.GONE
        }
        isClickable = true

        mVerticalSpace = verticalSpace
        mBadgeMarginRight = badgeMarginRight
        mBadgeMarginTop = badgeMarginTop
    }

    fun setImageResource(@DrawableRes resId: Int) {
        mImageView!!.setImageResource(resId)
    }

    fun imageView(): ImageView {
        return mImageView!!
    }

    fun textView(): TextView {
        return mTextView!!
    }

    fun badageView(): RoundedTextView? {
        return mBadgeView
    }

    fun setBadgeVisible(visible: Boolean) {
        mBadgeView!!.visibility = if(visible) View.VISIBLE else View.GONE
    }

    fun setBadgeText(text: String) {
        mBadgeView!!.text = text
    }

    private fun setDrawable(drawable: Drawable) {
        if(mImageDrawable != drawable) {
            if(null != mImageDrawable) {
                mImageDrawable!!.callback = null
                unscheduleDrawable(mImageDrawable)
            }

            mImageDrawable = drawable

            drawable.callback = this
            drawable.setVisible(visibility == View.VISIBLE , false)
            if(drawable.isStateful) {
                drawable.state = drawableState
            }
        }
    }

    // Set text color
    private fun setTextColor(textColor: ColorStateList) {
        mTextColor = textColor
        updateTextColor()
    }

    private fun updateTextColor() {
        mCurrTextColor = mTextColor!!.getColorForState(drawableState , Color.BLACK)
        mTextView!!.setTextColor(mCurrTextColor)
        mTextView!!.invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // TabBar宽高
        var width = 0
        var height = 0

        // 测量ImageView
        var widthUsed = paddingLeft + paddingRight
        var heightUsed = paddingTop + paddingBottom
        measureChildWithMargins(mImageView , widthMeasureSpec , widthUsed , heightMeasureSpec , heightUsed)

        width = mImageView!!.measuredWidth
        height = mImageView!!.measuredHeight

        // 测量TextView
        heightUsed += mImageView!!.measuredHeight + mVerticalSpace
        measureChildWithMargins(mTextView , widthMeasureSpec , widthUsed , heightMeasureSpec , heightUsed)

        width = Math.max(width , mTextView!!.measuredWidth)
        height += mTextView!!.measuredHeight + mVerticalSpace

        // 测量BadageView
        measureChildWithMargins(mBadgeView, widthMeasureSpec , 0 , heightMeasureSpec , 0)

        // 设置TabBar尺寸
        setMeasuredDimension(resolveSize(width , widthMeasureSpec) , resolveSize(height , heightMeasureSpec))
    }

    override fun onLayout(change: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        // 放置BadageView参数
        var bLeft = 0
        var bTop = 0
        var bRight = 0
        var bBottom = 0

        // 放置ImageView
        var childLeft = paddingLeft + (measuredWidth - mImageView!!.measuredWidth) / 2
        var childTop = paddingTop + (measuredHeight - mImageView!!.measuredHeight - mTextView!!.measuredHeight - mVerticalSpace) / 2
        var childRight = childLeft + mImageView!!.measuredWidth
        var childBottom = childTop + mImageView!!.measuredHeight

        mImageView!!.layout(childLeft , childTop , childRight , childBottom)

        Logger.e("ImageView , l = $childLeft , t = $childTop , r = $childRight , b = $childBottom")

        if(View.GONE != mBadgeView?.visibility) {
            bLeft = childRight - mBadgeMarginRight - mBadgeView!!.measuredWidth
            bTop = childTop + mBadgeMarginTop
            bRight = bLeft + mBadgeView!!.measuredWidth
            bBottom = bTop + mBadgeView!!.measuredHeight

            // 放置Badage View
            mBadgeView!!.layout(bLeft, bTop, bRight, bBottom)

            Logger.e("BadageView , l = $bLeft , t = $bTop , r = $bRight , b = $bBottom")
        }

        // 放置TextView
        childLeft = paddingLeft + (measuredWidth - mTextView!!.measuredWidth) / 2
        childTop += mImageView!!.measuredHeight + mVerticalSpace
        childRight = childLeft + mTextView!!.measuredWidth
        childBottom = childTop + mTextView!!.measuredHeight

        mTextView!!.layout(childLeft , childTop , childRight , childBottom)
        Logger.e("TextView , l = $childLeft , t = $childTop , r = $childRight , b = $childBottom")
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT)
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun performClick(): Boolean {
        toggle()

        val handled = super.performClick()
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK)
        }

        Logger.e("performClick")


        return handled
    }

    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        if(!isChecked) {
            setChecked(!isChecked)
        }
    }

    override fun setChecked(checked: Boolean) {
        if(isChecked != checked) {
            this.isChecked = checked
            refreshDrawableState()
            onCheckChangeListener?.onCheckChange(this, checked)
        }
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        val drawableState = super.onCreateDrawableState(extraSpace + 1)
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET)
        }

        Logger.e("onCreateDrawableState")

        return drawableState
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()

        Logger.e("drawableStateChanged + ${mImageDrawable?.current}")

        if(mImageDrawable is StateListDrawable) {
            val stateDrawable = mImageDrawable as StateListDrawable
            stateDrawable.state = drawableState
            mImageView!!.setImageDrawable(stateDrawable.current)
        } else {
            mImageView!!.setImageDrawable(mImageDrawable)
        }

        updateTextColor()
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return super.verifyDrawable(who) || who === mImageDrawable
    }

    override fun jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState()
        if (mImageDrawable != null) mImageDrawable!!.jumpToCurrentState()
    }
}
