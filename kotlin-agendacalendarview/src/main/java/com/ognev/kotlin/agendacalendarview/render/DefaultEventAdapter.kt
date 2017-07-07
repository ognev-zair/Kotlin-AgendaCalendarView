package com.ognev.kotlin.agendacalendarview.render

import android.support.annotation.NonNull
import android.view.View
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent

/**
 * Class helping to inflate our default layout in the AgendaAdapter
 */
open class DefaultEventAdapter : EventAdapter<CalendarEvent>() {

    override fun getEventLayout(isEmptyEvent: Boolean): Int {
        return 0
    }

    override
    fun getEventItemView(@NonNull view: View, @NonNull event: CalendarEvent, position: Int) {

    }

}
