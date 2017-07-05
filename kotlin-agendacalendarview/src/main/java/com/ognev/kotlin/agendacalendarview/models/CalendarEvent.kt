package com.ognev.kotlin.agendacalendarview.models

import java.util.Calendar

interface CalendarEvent {

    var isPlaceholder: Boolean

    val time: String

    fun setLocation(mLocation: String)

    var id: Long

    var startTime: Calendar

    var endTime: Calendar

    val name: String

    fun setTitle(mTitle: String)

    val instanceDay: Calendar

    fun setInstanceDay(mInstanceDay: Calendar): BaseCalendarEvent

    var dayReference: IDayItem

    var weekReference: IWeekItem

    fun copy(): CalendarEvent

    fun hasEvent(): Boolean

    val status: Int
}
