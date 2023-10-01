package com.app.dummyproduct.ui.fragment.home
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.app.dummyproduct.R
import com.app.dummyproduct.adapter.ImageAdapter
import com.app.dummyproduct.base.BaseFragment
import com.app.dummyproduct.callback.ActionCallback
import com.app.dummyproduct.data.model.responses.Product
import com.app.dummyproduct.databinding.FragmentProductDetailBinding
import com.app.dummyproduct.utils.roundToOneDecimalPlace
import kotlin.collections.ArrayList

class ProductDetailFragment : BaseFragment(), ActionCallback{
    private lateinit var binding: FragmentProductDetailBinding
    private lateinit var model: Product
    var isShow = true
    var scrollRange = -1
    var list: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            binding<FragmentProductDetailBinding>(
                inflater,
                R.layout.fragment_product_detail,
                container
            ).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@ProductDetailFragment
            }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupData()
    }

    fun setupData(){

        binding.category.text = model.category
        binding.brand.text = "Brand: ${model.brand}"
        binding.description.text = model.description
        binding.price.text = "$${model.price}"
        binding.name.text = model.title
        binding.toolbarLayout.title = model.title
        binding.rating.text = roundToOneDecimalPlace(model.rating)


        if(model.images.isNotEmpty()) {
            list.addAll(model.images)
            binding.viewPager.adapter = ImageAdapter(
                childFragmentManager, list
            )
        }
    }

    companion object {
        private const val TAG = "ProductDetailFragment"

        @JvmStatic
        fun getInstance(
            model: Product,
        ): ProductDetailFragment {
            val receivedProductDetailsFragment = ProductDetailFragment()
            receivedProductDetailsFragment.model = model
            return receivedProductDetailsFragment
        }

    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imgBack -> {
                popStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        whiteStatusBar()
    }

}

