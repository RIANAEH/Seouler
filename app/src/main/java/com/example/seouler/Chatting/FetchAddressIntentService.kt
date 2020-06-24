package com.example.seouler.Chatting

import android.app.IntentService
import android.content.Intent
import android.location.Geocoder
import java.util.*

object Constants {
    const val SUCCESS_RESULT = 0
    const val FAILURE_RESULT = 1
    const val PACKAGE_NAME = "com.example.seouler"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val RESULT_DATA_KEY = "${PACKAGE_NAME}.RESULT_DATA_KEY"
    const val LOCATION_DATA_EXTRA = "${PACKAGE_NAME}.LOCATION_DATA_EXTRA"
}
class FetchAddressIntentService(name: String) : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        var geocoder = Geocoder(this, Locale.US)
    }
}