package com.ognev.kotlin.agendacalendarview.utils

import android.content.Context
import com.ognev.kotlin.agendacalendarview.CalendarManager
import com.ognev.kotlin.agendacalendarview.models.IWeekItem
import com.ognev.kotlin.agendacalendarview.R

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Class containing helper functions for dates
 */
object DateHelper {

    // region Public methods

    /**
     * Check if two Calendar instances have the same time (by month, year and day of month)

     * @param cal          The first Calendar instance.
     * *
     * @param selectedDate The second Calendar instance.
     * *
     * @return True if both instances have the same time.
     */
    fun sameDate(cal: Calendar, selectedDate: Calendar): Boolean {
        return cal.get(Calendar.MONTH) === selectedDate.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) === selectedDate.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) === selectedDate.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * Check if a Date instance and a Calendar instance have the same time (by month, year and day
     * of month)

     * @param cal          The Calendar instance.
     * *
     * @param selectedDate The Date instance.
     * *
     * @return True if both have the same time.
     */
    fun sameDate(cal: Calendar, selectedDate: Date): Boolean {
        val selectedCal = Calendar.getInstance()
        selectedCal.time = selectedDate
        return cal.get(Calendar.MONTH) === selectedCal.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) === selectedCal.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) === selectedCal.get(Calendar.DAY_OF_MONTH)
    }

    /**
     * Check if a Date instance is between two Calendar instances' dates (inclusively) in time.

     * @param selectedDate The date to verify.
     * *
     * @param startCal     The start time.
     * *
     * @param endCal       The end time.
     * *
     * @return True if the verified date is between the two specified dates.
     */
    fun isBetweenInclusive(selectedDate: Date, startCal: Calendar, endCal: Calendar): Boolean {
        val selectedCal = Calendar.getInstance()
        selectedCal.time = selectedDate
        // Check if we deal with the same day regarding startCal and endCal
        return sameDate(selectedCal, startCal) || selectedCal.after(startCal) && selectedCal.before(endCal)
    }

    /**
     * Check if Calendar instance's date is in the same week, as the WeekItem instance.

     * @param cal  The Calendar instance to verify.
     * *
     * @param week The WeekItem instance to compare to.
     * *
     * @return True if both instances are in the same week.
     */
    fun sameWeek(cal: Calendar, week: IWeekItem): Boolean {
        return cal.get(Calendar.WEEK_OF_YEAR) === week.weekInYear && cal.get(Calendar.YEAR) === week.year
    }

    /**
     * Convert a millisecond duration to a string format

     * @param millis A duration to convert to a string form
     * *
     * @return A string of the form "Xd" or either "XhXm".
     */
    fun getDuration(context: Context, millis: Long): String {
        var millis = millis
        if (millis < 0) {
            throw IllegalArgumentException("Duration must be greater than zero!")
        }

        val days = TimeUnit.MILLISECONDS.toDays(millis)
        millis -= TimeUnit.DAYS.toMillis(days)
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)

        val sb = StringBuilder(64)
        if (days > 0) {
            sb.append(days)
            sb.append(context.getString(R.string.agenda_event_day_duration))
            return sb.toString()
        } else {
            if (hours > 0) {
                sb.append(hours)
                sb.append("h")
            }
            if (minutes > 0) {
                sb.append(minutes)
                sb.append("m")
            }
        }

        return sb.toString()
    }

    /**
     * Used for displaying the date in any section of the agenda view.

     * @param calendar The date of the section.
     * *
     * @param locale   The locale used by the Sunrise calendar.
     * *
     * @return The formatted date without the year included.
     */
    fun getYearLessLocalizedDate(calendar: Calendar, locale: Locale): String {
        val sdf = SimpleDateFormat.getDateInstance(DateFormat.FULL, CalendarManager.instance!!.locale) as SimpleDateFormat
        val pattern = sdf.toPattern()

        val yearLessPattern = pattern.replace("\\W?[Yy]+\\W?", "")
        val yearLessSDF = SimpleDateFormat(yearLessPattern, locale)
        var yearLessDate = yearLessSDF.format(calendar.getTime()).toUpperCase()
        if (yearLessDate.endsWith(",")) {
            yearLessDate = yearLessDate.substring(0, yearLessDate.length - 1)
        }
        return yearLessDate
    }

    // endregion
}
