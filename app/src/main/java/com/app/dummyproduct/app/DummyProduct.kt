package com.app.dummyproduct.app
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import androidx.fragment.app.FragmentManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DummyProduct : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        FragmentManager.enableDebugLogging(true)
    }

    override fun onCreate() {
        super.onCreate()
        ctx = this
    }

    fun setCurrentActivity(mCurrentActivity: Activity) {
        currentActivity = mCurrentActivity
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var currentActivity: Activity? = null
        @SuppressLint("StaticFieldLeak")
        lateinit var ctx: DummyProduct
        fun getAppContext(): DummyProduct {
            return ctx
        }
    }
}