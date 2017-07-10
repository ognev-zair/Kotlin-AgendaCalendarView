package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

/**
 * Event model class containing the information to be displayed on the agenda view.
 */
open class BaseCalendarEvent : CalendarEvent {


    override lateinit var instanceDay: Calendar
    override lateinit var dayReference: IDayItem
    override lateinit var weekReference: IWeekItem

    override fun hasEvent(): Boolean {
        return false
    }

    override lateinit var startTime: Calendar
    override lateinit var endTime: Calendar

    override var event: Any? = null

    override fun setEventInstanceDay(instanceDay: Calendar): BaseCalendarEvent {
        this.instanceDay = instanceDay
        this.instanceDay.set(Calendar.HOUR, 0)
        this.instanceDay.set(Calendar.MINUTE, 0)
        this.instanceDay.set(Calendar.SECOND, 0)
        this.instanceDay.set(Calendar.MILLISECOND, 0)
        this.instanceDay.set(Calendar.AM_PM, 0)
        return this
    }
//    override fun setInstanceDay(mInstanceDay: Calendar): BaseCalendarEvent {
//        this.mInstanceDay = mInstanceDay
//        this.mInstanceDay!!.set(Calendar.HOUR, 0)
//        this.mInstanceDay!!.set(Calendar.MINUTE, 0)
//        this.mInstanceDay!!.set(Calendar.SECOND, 0)
//        this.mInstanceDay!!.set(Calendar.MILLISECOND, 0)
//        this.mInstanceDay!!.set(Calendar.AM_PM, 0)
//        return this
//    }


    override
    fun copy(): CalendarEvent {
        return BaseCalendarEvent(this)
    }

    constructor(baseCalendarEvent: BaseCalendarEvent)
    constructor()

    override
    fun toString(): String {
        return "BaseCalendarEvent{"
                .plus("title='")
                .plus(", instanceDay= ")
//                .plus(mInstanceDay!!.getTime())
                .plus("}")
    }

}
