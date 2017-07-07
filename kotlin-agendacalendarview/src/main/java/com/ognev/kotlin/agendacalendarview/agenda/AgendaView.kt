package com.ognev.kotlin.agendacalendarview.agenda

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.*
import android.widget.FrameLayout
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.utils.BusProvider
import com.ognev.kotlin.agendacalendarview.utils.Events
import com.ognev.kotlin.agendacalendarview.R

class AgendaView : FrameLayout {

    lateinit var agendaListView: AgendaListView
        private set

    private var mShadowView: View? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.view_agenda, this, true)

        (findViewById(R.id.refresh_layout) as SwipeRefreshLayout).isEnabled = false
    }


    override fun onFinishInflate() {
        super.onFinishInflate()

        agendaListView = findViewById(R.id.agenda_listview) as AgendaListView
        mShadowView = findViewById(R.id.view_shadow)

        BusProvider.instance.toObserverable()
                .subscribe({ event ->
                    if (event is Events.DayClickedEvent) {
                        val clickedEvent = event
                        agendaListView.scrollToCurrentDate(clickedEvent.calendar)
                    } else if (event is Events.CalendarScrolledEvent) {
                        val offset = (3 * getResources().getDimension(R.dimen.day_cell_height))
                        translateList(offset.toInt())
                    } else if (event is Events.EventsFetched) {
                        (agendaListView.adapter as AgendaAdapter).updateEvents()

                        viewTreeObserver.addOnGlobalLayoutListener(
                                object : ViewTreeObserver.OnGlobalLayoutListener {
                                    override
                                    fun onGlobalLayout() {
                                        if (width !== 0 && height !== 0) {
                                            // display only two visible rows on the calendar view
                                            val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
                                            val height = height
                                            val margin = (getContext().getResources().getDimension(R.dimen.calendar_header_height) + 2 * getContext().getResources().getDimension(R.dimen.day_cell_height))
                                            layoutParams.height = height
                                            layoutParams.setMargins(0, margin.toInt(), 0, 0)
                                            setLayoutParams(layoutParams)
                                            //todo
                                            if (!CalendarManager.instance!!.events.isEmpty())
                                                agendaListView.scrollToCurrentDate(CalendarManager.instance!!.today)

                                            viewTreeObserver.removeGlobalOnLayoutListener(this)
                                        }
                                    }
                                }
                        )
                    } else if (event is Events.ForecastFetched) {
                        (agendaListView.adapter as AgendaAdapter).updateEvents()
                    }
                })
    }

    override
    fun dispatchTouchEvent(event: MotionEvent): Boolean {
        val eventaction = event.action

        when (eventaction) {
            MotionEvent.ACTION_DOWN ->
                // if the user touches the listView, we put it back to the top
                translateList(0)
            else -> {
            }
        }

        return super.dispatchTouchEvent(event)
    }

    fun translateList(targetY: Int) {
        if (targetY != translationY.toInt()) {
            val mover = ObjectAnimator.ofFloat(this, "translationY", targetY.toFloat())
            mover.duration = 150
            mover.addListener(object : Animator.AnimatorListener {
                override
                fun onAnimationStart(animation: Animator) {
                    mShadowView!!.visibility = GONE
                }

                override
                fun onAnimationEnd(animation: Animator) {
                    if (targetY == 0) {
                        BusProvider.instance.send(Events.AgendaListViewTouchedEvent())
                    }
                    mShadowView!!.visibility = VISIBLE
                }

                override
                fun onAnimationCancel(animation: Animator) {

                }

                override
                fun onAnimationRepeat(animation: Animator) {

                }
            })
            mover.start()
        }
    }

}
