package com.ognev.kotlin.agendacalendarview.models

import java.util.Date

/**
 * Week model class.
 */
class WeekItem
// region Constructor

(
        // endregion

        // region Getters/Setters

        override var weekInYear: Int,
        override var year: Int,
        override var date: Date,
        override var label: String,
        override var month: Int) : IWeekItem {
    private var hasEvents: Boolean = false
    override lateinit var dayItems: List<IDayItem>

    override fun hasEvents(): Boolean {
        return hasEvents
    }

    override fun setHasEvents(hasEvents: Boolean) {
        this.hasEvents = hasEvents
    }

    // endregion

    override
    fun toString(): String {
        return "WeekItem{"
                .plus("label='")
                .plus(label)
                .plus('\'')
                .plus(", weekInYear=")
                .plus(weekInYear)
                .plus(", year=")
                .plus(year)
                .plus('}')
    }
}
