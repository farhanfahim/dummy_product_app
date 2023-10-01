package com.app.dummyproduct.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject

class PrefManager @Inject constructor(
    private val preferences: SharedPreferences, 
) {
    //Keys
    private val PREF_KEY_TOKEN = "token"
    private val PREF_KEY_REFRESH_TOKEN = "refresh_token"
    private val PREF_KEY_USERNAME = "username"
    private val PREF_KEY_PASSWORD = "password"

    private var gson: Gson? = Gson()

    fun getString(key: String, defaultValue: String): String {
        return preferences.getString(key, defaultValue)!!
    }

    fun putObject(key: String?, value: Any?) {
        if (gson == null) {
            return
        }
        putString(key!!, gson!!.toJson(value)!!)
    }

    fun <T> getObject(key: String?, type: Class<T>?): T? {
        return try {
            if (gson == null) {
                null
            } else gson?.fromJson(getString(key!!, ""), type)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun putString(key: String, value: String) {
        try {
            println("$key $value")
            preferences.edit().putString(key, value).apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    fun clearAllPreferences() {
        preferences.edit().clear()
            .apply()
    }

}