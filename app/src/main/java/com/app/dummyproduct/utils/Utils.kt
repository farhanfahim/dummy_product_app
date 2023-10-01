package com.app.dummyproduct.utils

import android.content.Context
import android.os.Handler
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.apachat.loadingbutton.core.customViews.CircularProgressButton
import com.app.dummyproduct.R
import com.bumptech.glide.Glide
import java.util.*

class Utils {

    companion object {


        fun onStartAnimationCall(btn: CircularProgressButton) {
            btn.startAnimation()
        }

        fun onRevertAnimationCall(btn: CircularProgressButton) {
            btn.revertAnimation()
        }

        fun onSuccessCall(btn: CircularProgressButton, context: Context){
            val icon = MyBitmapUtils.getBitmapFromVectorDrawable(context, R.drawable.ic_check_icon_white)
            btn.doneLoadingAnimation(ContextCompat.getColor(context, R.color.dark_green_color), icon)
        }

        fun onErrorCall(btn: CircularProgressButton, context: Context){
            val icon = MyBitmapUtils.getBitmapFromVectorDrawable(context, R.drawable.ic_close_icon_white)
            btn.doneLoadingAnimation(ContextCompat.getColor(context, R.color.red_color), icon)
            Handler().postDelayed({
                btn.revertAnimation()
            }, 500)
        }

        fun setErrorIcon(imageView: ImageView, editText: EditText, customTextInputLayout: CustomTextInputLayout, message: String){
            imageView.visible()
            imageView.setImageResource(R.drawable.ic_error_circle)
            editText.setBackgroundResource(R.drawable.error_edit_text)
            customTextInputLayout.error = message
        }
        fun setSuccessIcon(
            imageView: ImageView,
            editText: EditText,
            customTextInputLayout: CustomTextInputLayout
        ) {
            imageView.visible()
            imageView.setImageResource(R.drawable.ic_tick_circle_new)
            editText.setBackgroundResource(R.drawable.success_edit_text)
            customTextInputLayout.isErrorEnabled = false
        }

        fun setSwipeColor(swipeRefreshLayout: SwipeRefreshLayout, context: Context){
            swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary),
                ContextCompat.getColor(context, R.color.black),
                ContextCompat.getColor(context, R.color.black))
        }

        fun ImageView.loadImagesWithGlideExt(url: String) {
            Glide.with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.place_holder)
                .into(this)
        }


    }
}