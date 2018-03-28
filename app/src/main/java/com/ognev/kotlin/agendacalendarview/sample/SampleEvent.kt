package com.ognev.kotlin.agendacalendarview.sample


/**
 * Custom Sample Event which can be obtained and parsed
 * to this class from backend
 */
data class SampleEvent(val id: Long = 0, val name: String, val description: String)

