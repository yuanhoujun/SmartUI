package me.foji.smartui

import android.content.Context
import android.os.Build
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton

/**
 * TabBarGroup主要用于管理一组TabBar控件，将TabBar作为子控件添加到该控件中。
 * 该控件将可以保证一次只能有一个TabBar处于选中状态。同时，你也可以通过checkedButton属性指定默认选中的TabBar。
 *
 * @author Scott Smith
 */
class TabBarGroup : LinearLayout {
    private var mCheckedId = -1
    private var mChildOnCheckedChangeListener: TabBar.OnCheckChangeListener? = null
    // 防止循环调用
    private var mProtectFromCheckedChange = false
    private var mPassThroughListener: PassThroughHierarchyChangeListener? = null

    constructor(context: Context) : this(context , null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context , attrs)
    }

    private fun init(context: Context , attrs: AttributeSet?) {
        if (null != attrs) {
            val array = context.obtainStyledAttributes(attrs, R.styleable.TabBarGroup, 0, 0)
            mCheckedId = array.getResourceId(R.styleable.TabBarGroup_checkedButton, View.NO_ID)
            array.recycle()
        }

        initListener()
    }

    private fun initListener() {
        mPassThroughListener = PassThroughHierarchyChangeListener()
        mChildOnCheckedChangeListener = CheckedStateTracker()
        super.setOnHierarchyChangeListener(mPassThroughListener)
    }

    private inner class CheckedStateTracker : TabBar.OnCheckChangeListener {
        override fun onCheckChange(button: TabBar, isCheck: Boolean) {
            if (mProtectFromCheckedChange) {
                return
            }

            mProtectFromCheckedChange = true
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false)
            }
            mProtectFromCheckedChange = false

            val id = button.id
            setCheckedId(id)
        }
    }

    private fun setCheckedStateForView(viewId: Int, checked: Boolean) {
        val checkedView = findViewById(viewId)
        if (checkedView != null && checkedView is TabBar) {
            checkedView.isChecked = checked
        }
    }

    private fun setCheckedId(@IdRes id: Int) {
        mCheckedId = id
    }

    override fun setOnHierarchyChangeListener(listener: ViewGroup.OnHierarchyChangeListener) {
        mPassThroughListener!!.mOnHierarchyChangeListener = listener
    }

    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is RadioButton) {
            if (child.isChecked) {
                mProtectFromCheckedChange = true
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false)
                }
                mProtectFromCheckedChange = false
                setCheckedId(child.id)
            }
        }

        super.addView(child, index, params)
    }

    fun check(@IdRes id: Int) {
        // don't even bother
        if (id != -1 && id == mCheckedId) {
            return
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false)
        }

        if (id != -1) {
            setCheckedStateForView(id, true)
        }

        setCheckedId(id)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true
            setCheckedStateForView(mCheckedId, true)
            mProtectFromCheckedChange = false
            setCheckedId(mCheckedId)
        }
    }
    inner class PassThroughHierarchyChangeListener : ViewGroup.OnHierarchyChangeListener {
        var mOnHierarchyChangeListener: ViewGroup.OnHierarchyChangeListener? = null

        override fun onChildViewAdded(parent: View, child: View) {
            if (parent === this@TabBarGroup && child is TabBar) {
                var id = child.getId()
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    if (Build.VERSION.SDK_INT >= 17) {
                        id = View.generateViewId()
                    }
                    child.setId(id)
                }
                child.onCheckChangeListener = mChildOnCheckedChangeListener
            }

            mOnHierarchyChangeListener?.onChildViewAdded(parent, child)
        }

        override fun onChildViewRemoved(parent: View, child: View) {
            if (parent === this@TabBarGroup && child is TabBar) {
                child.onCheckChangeListener = null
            }

            mOnHierarchyChangeListener?.onChildViewRemoved(parent, child)
        }
    }
}
