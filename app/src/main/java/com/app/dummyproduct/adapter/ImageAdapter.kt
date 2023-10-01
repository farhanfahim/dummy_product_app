package com.app.dummyproduct.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.app.dummyproduct.ui.fragment.home.ImageFragment

class ImageAdapter(fm: FragmentManager, private val list: List<String>?) :
    FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int = list!!.size

    override fun getItem(i: Int): Fragment {
        val fragment = ImageFragment()
        fragment.arguments = Bundle().apply {
            putString("image", list!![i])
        }
        return fragment
    }
}