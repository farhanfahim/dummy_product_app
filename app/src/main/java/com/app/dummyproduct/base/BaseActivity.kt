package com.app.dummyproduct.base

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.app.dummyproduct.app.DummyProduct
import com.app.dummyproduct.data.remote.ConnectivityChangeReceiver
import com.app.dummyproduct.data.remote.ConnectivityChangeReceiver.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), OnConnectivityChangedListener {

    var mMyApp: DummyProduct? = null
    private var connectivityChangeReceiver: ConnectivityChangeReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mMyApp = this.applicationContext as DummyProduct
        connectivityChangeReceiver = ConnectivityChangeReceiver(this)
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(connectivityChangeReceiver, filter)
    }


    protected inline fun <reified T : ViewDataBinding> binding(
        @LayoutRes resId: Int
    ): Lazy<T> = lazy { DataBindingUtil.setContentView<T>(this, resId) }


    override fun onResume() {
        super.onResume()
        mMyApp!!.setCurrentActivity(this)
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityChangeReceiver)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    override fun onConnectivityChanged(isConnected: Boolean) {
    }
}