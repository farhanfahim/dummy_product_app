package com.app.dummyproduct.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.app.dummyproduct.R
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    private var ctx: FragmentActivity? = null

    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.ctx = activity
    }


    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun dismissTouch() {
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun enableTouch(){
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun snack(message: String) {
        val snackbar: Snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
        val sbView = snackbar.view
        sbView.setBackgroundResource(R.color.black)
        snackbar.show()
    }

    fun addFragment(
        fragment: Fragment,
        frameId: Int,
        addBackStack: Boolean,
        clearBackStack: Boolean
    ) {
        ctx!!.supportFragmentManager.transact {
            if (addBackStack) {
                addToBackStack(fragment::class.java.name)
            }
            if (clearBackStack) {
                for (i in 0 until ctx!!.supportFragmentManager.backStackEntryCount) {
                    ctx!!.supportFragmentManager.popBackStack()
                }
            }
            add(frameId, fragment)
        }
    }

    private inline fun FragmentManager.transact(action: FragmentTransaction.() -> Unit) {
        beginTransaction().apply {
            action()
        }.commit()
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    fun <T> changeActivity(cls: Class<T>?) {
        val intent = Intent(requireActivity(), cls)
        startActivity(intent)
        requireActivity().finish()
    }


    protected inline fun <reified T : ViewDataBinding> binding(
        inflater: LayoutInflater,
        @LayoutRes resId: Int,
        container: ViewGroup?
    ): T = DataBindingUtil.inflate(inflater, resId, container, false)

    fun popStack() {
        hideKeyboard((ctx as Activity?)!!)
        ctx!!.supportFragmentManager.popBackStack()
    }

    fun whiteStatusBar(){
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.colorWhite)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }

}