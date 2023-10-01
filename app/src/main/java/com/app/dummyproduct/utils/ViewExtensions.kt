package com.app.dummyproduct.utils

import android.content.Context
import android.net.ConnectivityManager
import android.view.View
import java.text.DecimalFormat


fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}


fun roundToOneDecimalPlace(value: Double): String {
    val decimalFormat = DecimalFormat("#.#")
    return decimalFormat.format(value).toDouble().toString()
}

fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
    return false
}
