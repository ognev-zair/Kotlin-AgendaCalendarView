package com.ognev.kotlin.agendacalendarview.models

import java.util.Date

interface IDayItem {

    // region Getters/Setters

    var date: Date

    var value: Int

    var isToday: Boolean

    var isSelected: Boolean

    var isFirstDayOfTheMonth: Boolean

    var month: String

    var dayOftheWeek: Int

    // endregion

    override fun toString(): String

    fun setHasEvents(hasEvents: Boolean)

    fun hasEvents(): Boolean
}
