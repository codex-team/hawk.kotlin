package com.hawk.akscorp.kotlin

import android.app.Application
import hawk.HawkExceptionCatcher

/**
 * Created by AksCorp on 12.11.2017.
 */

class Start : Application() {

    private var exceptionCatcher: HawkExceptionCatcher? = null

    private fun defineExceptionCather() {
        exceptionCatcher = HawkExceptionCatcher(this, "0927e8cc-f3f0-4ce4-aa27-916f0774af51")
        try {
            exceptionCatcher?.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreate() {
        super.onCreate()
        defineExceptionCather()
    }
}