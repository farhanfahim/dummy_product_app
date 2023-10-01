package com.app.dummyproduct.ui.activity

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.app.dummyproduct.R
import com.app.dummyproduct.base.BaseActivity
import com.app.dummyproduct.databinding.ActivityAuthBinding
import com.app.dummyproduct.ui.fragment.auth.SignInFragment

class AuthActivity : BaseActivity() {

    private val binding: ActivityAuthBinding by binding(R.layout.activity_auth)

    private var fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration, 1.0f)
        supportActionBar?.hide()
        binding.lifecycleOwner = this@AuthActivity

        fragment = SignInFragment.newInstance()
        defaultFragment(fragment)
    }

    private fun defaultFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, fragment, fragment::class.java.name)
            .commit()
    }


    private fun adjustFontScale(configuration: Configuration, scale: Float) {
        configuration.fontScale = scale
        val metrics = resources.displayMetrics
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        baseContext.resources.updateConfiguration(configuration, metrics)
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}