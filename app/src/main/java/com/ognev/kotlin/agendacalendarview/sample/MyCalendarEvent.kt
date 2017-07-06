package com.ognev.kotlin.agendacalendarview.sample

import com.ognev.kotlin.agendacalendarview.models.*
import java.util.*

/**
 * Created by ognev on 7/6/17.
 */

class MyCalendarEvent: BaseCalendarEvent {
    override var isPlaceholder: Boolean = false
    override val time: String = ""

    override fun setLocation(mLocation: String) {
    }

    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar
    override var name: String = ""

    override fun setTitle(mTitle: String) {
    }

    override val status: Int = 0
    override lateinit var event: Any

    override val instanceDay: Calendar
        get() = mInstanceDay!!

    override var id: Long = 0

    private var mInstanceDay: Calendar? = null

    override lateinit var dayReference: IDayItem

    override lateinit var weekReference: IWeekItem

    private var hasEvent: Boolean = false


    override fun setInstanceDay(mInstanceDay: Calendar): MyCalendarEvent {
        this.mInstanceDay = mInstanceDay
        this.mInstanceDay!!.set(Calendar.HOUR, 0)
        this.mInstanceDay!!.set(Calendar.MINUTE, 0)
        this.mInstanceDay!!.set(Calendar.SECOND, 0)
        this.mInstanceDay!!.set(Calendar.MILLISECOND, 0)
        this.mInstanceDay!!.set(Calendar.AM_PM, 0)
        return this
    }

    constructor(calendarEvent: CalendarEvent<Any>) {
        this.id = calendarEvent.id
        this.hasEvent = calendarEvent.hasEvent()
    }


    constructor(title: String, description: String,
                location: String, color: String,
                startTime: Calendar, endTime: Calendar,
                allDay: Boolean, dayItem: DayItem,
                hasEvent: Boolean, event: SampleEvent) {
        this.name = title
        this.startTime = startTime;
        this.endTime = endTime;
        this.hasEvent = hasEvent
        this.dayReference = dayItem
        this.event = event
    }


    override
    fun copy(): MyCalendarEvent {
        return MyCalendarEvent(this)
    }

    override fun hasEvent(): Boolean {
        return event != null
    }

    // endregion

    override
    fun toString(): String {
        return super.toString();
    }


}