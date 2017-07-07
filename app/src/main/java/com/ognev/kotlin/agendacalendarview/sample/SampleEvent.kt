package com.ognev.kotlin.agendacalendarview.sample


/**
 * Custom Sample Event which can be obtained and parsed
 * to this class from backend
 */
class SampleEvent(name: String, description: String) {
    var id: Long = 0
    var name: String = name
    var desciption: String = description
}