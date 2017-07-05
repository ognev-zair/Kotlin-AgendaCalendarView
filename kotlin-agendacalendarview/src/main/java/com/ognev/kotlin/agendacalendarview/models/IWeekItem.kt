package com.ognev.kotlin.agendacalendarview.models

import java.util.Date

interface IWeekItem {


    var weekInYear: Int

    var year: Int

    var month: Int

    var date: Date

    var label: String

    fun hasEvents(): Boolean

    fun setHasEvents(hasEvents: Boolean)

    var dayItems: List<IDayItem>
}
