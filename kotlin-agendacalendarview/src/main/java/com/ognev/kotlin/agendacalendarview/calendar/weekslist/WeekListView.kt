package com.ognev.kotlin.agendacalendarview.calendar.weekslist

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.Events

class WeekListView : RecyclerView {
    private var mUserScrolling = false
    private var mScrolling = false

    // region Constructors

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    // endregion

    // region Public methods

    /**
     * Enable snapping behaviour for this recyclerView

     * @param enabled enable or disable the snapping behaviour
     */
    fun setSnapEnabled(enabled: Boolean) {
        if (enabled) {
            addOnScrollListener(mScrollListener)
        } else {
            removeOnScrollListener(mScrollListener)
        }
    }

    // endregion

    // region Private methods

    private val mScrollListener = object : OnScrollListener() {
        override
        fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
        }

        override
        fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            val weeksAdapter = getAdapter() as WeeksAdapter

            when (newState) {
                SCROLL_STATE_IDLE -> {
                    if (mUserScrolling) {
                        scrollToView(centerView)
                        postDelayed({ weeksAdapter.isDragging = false }, 700) // Wait for recyclerView to settle
                    }

                    mUserScrolling = false
                    mScrolling = false
                }
            // If scroll is caused by a touch (scroll touch, not any touch)
                SCROLL_STATE_DRAGGING -> {
                    BusProvider.instance.send(Events.CalendarScrolledEvent() as Any)
                    // If scroll was initiated already, this is not a user scrolling, but probably a tap, else set userScrolling
                    if (!mScrolling) {
                        mUserScrolling = true
                    }
                    weeksAdapter.isDragging = (true)
                }
                SCROLL_STATE_SETTLING -> {
                    // The user's finger is not touching the list anymore, no need
                    // for any alpha animation then
                    weeksAdapter.isAlphaSet = (true)
                    mScrolling = true
                }
            }
        }
    }

    private fun getChildClosestToPosition(y: Int): View? {
        if (getChildCount() <= 0) {
            return null
        }

        val itemHeight = getChildAt(0).getMeasuredHeight()

        var closestY = 9999
        var closestChild: View? = null

        for (i in 0..getChildCount() - 1) {
            val child = getChildAt(i)

            val childCenterY = child.getY() + itemHeight / 2
            val yDistance = childCenterY - y

            // If child center is closer than previous closest, set it as closest
            if (Math.abs(yDistance) < Math.abs(closestY)) {
                closestY = yDistance.toInt()
                closestChild = child
            }
        }

        return closestChild
    }

    private val centerView: View
        get() = getChildClosestToPosition(measuredHeight / 2)!!

    private fun scrollToView(child: View?) {
        if (child == null) {
            return
        }

        stopScroll()

        val scrollDistance = getScrollDistance(child)

        if (scrollDistance != 0) {
            smoothScrollBy(0, scrollDistance)
        }
    }

    private fun getScrollDistance(child: View): Int {
        val itemHeight = getChildAt(0).getMeasuredHeight()
        val centerY = getMeasuredHeight() / 2

        val childCenterY = child.getY() + itemHeight / 2

        return childCenterY.toInt() - centerY
    }

    // endregion
}
