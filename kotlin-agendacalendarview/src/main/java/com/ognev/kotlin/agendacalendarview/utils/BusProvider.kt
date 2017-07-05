package com.ognev.kotlin.agendacalendarview.utils

import rx.Observable
import rx.subjects.PublishSubject
import rx.subjects.SerializedSubject

class BusProvider {

    private val mBus = SerializedSubject<Any, Any>(PublishSubject.create())

    // endregion

    // region Public methods

    fun send(`object`: Any) {
        mBus.onNext(`object`)
    }

    fun toObserverable(): Observable<Any> {
        return mBus
    }

    companion object {

        var mInstance: BusProvider? = null

        // region Constructors

        val instance: BusProvider
            get() {
                if (mInstance == null) {
                    mInstance = BusProvider()
                }
                return mInstance!!
            }
    }


}
