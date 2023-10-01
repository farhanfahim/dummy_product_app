package com.app.dummyproduct.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.util.regex.Matcher
import java.util.regex.Pattern

object StaticMethods {

    fun isValidPassword(password: String?): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    fun hideSoftKeyboard(context: Context) {
        try {
            val inputManager = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = (context as Activity).currentFocus
            if (view == null) {
                view = View(context)
            }
            inputManager.hideSoftInputFromWindow(
                view
                    .windowToken, InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}