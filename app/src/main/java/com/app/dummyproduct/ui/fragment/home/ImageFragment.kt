package com.app.dummyproduct.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.dummyproduct.base.BaseFragment
import com.app.dummyproduct.databinding.FragmentImageBinding
import com.app.dummyproduct.R
import com.app.dummyproduct.utils.Utils.Companion.loadImagesWithGlideExt

class ImageFragment : BaseFragment() {

    private lateinit var binding: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = binding<FragmentImageBinding>(
            inflater,
            R.layout.fragment_image,
            container
        ).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            binding.ivService.loadImagesWithGlideExt(arguments?.getString("image")!!)
        }
    }

    companion object {
        private const val TAG = "ImageFragment"

        @JvmStatic
        fun newInstance() = ImageFragment()
    }


}