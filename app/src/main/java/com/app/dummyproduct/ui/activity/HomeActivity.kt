package com.app.dummyproduct.ui.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.dummyproduct.R
import com.app.dummyproduct.base.BaseActivity
import com.app.dummyproduct.databinding.ActivityMainBinding
import com.app.dummyproduct.ui.fragment.auth.SignInFragment
import com.app.dummyproduct.ui.fragment.home.ProductFragment
import com.app.dummyproduct.viewmodel.HomeViewModel
import com.app.dummyproduct.viewmodel.SplashViewModel

class HomeActivity : BaseActivity() {

    private val binding: ActivityMainBinding by binding(R.layout.activity_main)
    lateinit var viewModel: HomeViewModel
    private var fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        binding.lifecycleOwner = this@HomeActivity

        fragment = ProductFragment.newInstance()
        defaultFragment(fragment)
    }


    private fun defaultFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container_main, fragment, fragment::class.java.name)
            .commit()
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack()
        } else {
            exitDialog()
        }
    }


    private fun exitDialog() {
        val dialog = Dialog(this@HomeActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.exit_dialog)

        val yesButton: TextView = dialog.findViewById(R.id.btn_yes) as TextView
        val noButton: TextView = dialog.findViewById(R.id.btn_no) as TextView

        yesButton.setOnClickListener {
            dialog.dismiss()
            viewModel.clearAllPreferences()
            finishAffinity()
        }

        noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}