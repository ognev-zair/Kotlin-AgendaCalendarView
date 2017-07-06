package com.ognev.kotlin.agendacalendarview.agenda

import android.content.Context
import android.util.AttributeSet
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.utils.DateHelper
import se.emilsjolander.stickylistheaders.StickyListHeadersListView

import java.util.Calendar

/**
 * StickyListHeadersListView to scroll chronologically through events.
 */
class AgendaListView : StickyListHeadersListView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    fun scrollToCurrentDate(today: Calendar) {
        val events = CalendarManager.instance!!.events

        var toIndex = 0
        for (i in 0..events.size - 1) {
            if (DateHelper.sameDate(today, events[i].instanceDay)) {
                toIndex = i
                break
            }
        }

        val finalToIndex = toIndex
        post { setSelection(finalToIndex) }
    }

}
