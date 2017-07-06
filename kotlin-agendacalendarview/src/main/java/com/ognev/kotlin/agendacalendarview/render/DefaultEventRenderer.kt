package com.ognev.kotlin.agendacalendarview.render

import android.graphics.Color
import android.support.annotation.NonNull
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.CalendarItemLayout
import com.ognev.kotlin.agendacalendarview.R
import com.ognev.kotlin.agendacalendarview.models.BaseCalendarEvent
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
open class DefaultEventRenderer : EventRenderer<CalendarEvent<Any>>() {

    override fun getEventLayout(isEmptyEvent: Boolean): Int {
        return 0
    }

    override
    fun render(@NonNull view: View, @NonNull event: CalendarEvent<Any>) {

    }

}
