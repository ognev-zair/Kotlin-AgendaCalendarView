package com.ognev.kotlin.agendacalendarview.sample

import android.view.View
import android.widget.TextView
import com.ognev.kotlin.agendacalendarview.models.CalendarEvent
import com.ognev.kotlin.agendacalendarview.render.DefaultEventRenderer

/**
 * Created by ognev on 7/5/17.
 */

class SampleAgendaAdapter: DefaultEventRenderer() {

    override fun render(view: View, event: CalendarEvent) {
        val myEvent = event as MyCalendarEvent;
        val myObject:SampleEvent = myEvent.event as SampleEvent

        if(myEvent.hasEvent()) {
            (view.findViewById(R.id.name)
                    as TextView).text = myObject.name

            (view.findViewById(R.id.description)
                    as TextView).text = myObject.desciption
        }

        super.render(view, event)
    }

    override fun getEventLayout(hasEvent: Boolean): Int {
        return if(hasEvent) R.layout.view_agenda_event else R.layout.view_agenda_empty_event
    }

}