package me.foji.smartui

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.EditText
import org.jetbrains.anko.dip

/**
 * Smart EditText
 *
 * @author Scott Smith 2017-07-12 14:27
 */
class SmartEditText(context: Context, attrs: AttributeSet?): EditText(context, attrs) {
    private var mDeleteVisible = true
    private var mDrawableRight: Drawable? = null
    private var mDeleteIsVisible = false

    init {
        if(null != attrs) {
            val arr = context.obtainStyledAttributes(attrs, R.styleable.SmartEditText, 0, 0)
            mDeleteVisible = arr.getBoolean(R.styleable.SmartEditText_smart_ui_delete_visible, true)
            arr.recycle()
        }

        drawDeleteButton()
    }

    private fun drawDeleteButton() {
        addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if(mDeleteVisible) {
                    if(!TextUtils.isEmpty(s.toString())) {
                        if(null == mDrawableRight) {
                            mDrawableRight = resources.getDrawable(R.mipmap.smart_edittext_delete)
                        }
                        compoundDrawablePadding = dip(5)
                        setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableRight, null)
                        mDeleteIsVisible = true
                    } else {
                        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                        mDeleteIsVisible = false
                    }
                }
            }
        })
    }

    fun setDeleteVisible(visible: Boolean) {
        mDeleteVisible = visible
        if(!visible) {
            mDeleteIsVisible = false
            mDrawableRight = null
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(MotionEvent.ACTION_UP == event.action && mDeleteIsVisible) {
            val bounds = mDrawableRight!!.bounds
            val x = event.x

            if(x >= width - bounds.width() - dip(5) && x <= width - paddingRight + dip(5)) {
                text = null
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mDrawableRight = null
    }
}