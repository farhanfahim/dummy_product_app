package com.app.dummyproduct.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.dummyproduct.R
import com.app.dummyproduct.adapter.OnMyItemsClick
import com.app.dummyproduct.adapter.ProductAdapter
import com.app.dummyproduct.base.BaseFragment
import com.app.dummyproduct.callback.ActionCallback
import com.app.dummyproduct.constants.Constant
import com.app.dummyproduct.data.events.BaseEvent
import com.app.dummyproduct.data.events.OnGetProductsEvent
import com.app.dummyproduct.data.model.responses.Product
import com.app.dummyproduct.databinding.FragmentProductBinding
import com.app.dummyproduct.ui.activity.AuthActivity
import com.app.dummyproduct.ui.activity.HomeActivity
import com.app.dummyproduct.utils.*
import com.app.dummyproduct.utils.Utils.Companion.loadImagesWithGlideExt
import com.app.dummyproduct.viewmodel.ProductsViewModel

class ProductFragment : BaseFragment(), ActionCallback, OnMyItemsClick {
    private lateinit var binding: FragmentProductBinding
    lateinit var adapter: ProductAdapter
    lateinit var viewModel: ProductsViewModel

    var isLoading = false
    private var isLastPage = false
    var total: Int? = null
    var skip: Int = 0
    lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        viewModel.getProductsEvent.observe(this, getProductsObserver)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            binding<FragmentProductBinding>(
                inflater,
                R.layout.fragment_product,
                container
            ).apply {
                lifecycleOwner = viewLifecycleOwner
                callback = this@ProductFragment
                toolbarMain.callback = this@ProductFragment

                toolbarMain.tvName.text = resources.getString(R.string.dummy_products)

            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callProductsListingApi()

        setUpAdapter()
        setUpRefreshLayout()
        setUpPaginationListener()

    }

    companion object {
        private const val TAG = "ProductFragment"

        @JvmStatic
        fun newInstance() = ProductFragment()
    }

    override fun onClick(view: View) {
    }

    private fun setUpAdapter() {

        staggeredGridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvProducts.layoutManager = staggeredGridLayoutManager
        adapter = ProductAdapter(requireActivity(), this)
        binding.rvProducts.adapter = adapter
        binding.rvProducts.itemAnimator = null
    }

    private fun setUpRefreshLayout() {
        binding.swipeRefresh.setOnRefreshListener {
            val handler = Handler()
            handler.postDelayed({
                if (binding.swipeRefresh.isRefreshing) {
                    if (binding.shimmer.visibility == View.GONE) {
                        if (!isLoading) {
                            onRefreshData()
                        } else {
                            binding.swipeRefresh.isRefreshing = false
                        }
                    }
                }
            }, 0)
        }

        Utils.setSwipeColor(binding.swipeRefresh, requireActivity())
    }

    private fun setUpPaginationListener() {
        binding.rvProducts.addOnScrollListener(object :
            GridPaginationScrollListener(staggeredGridLayoutManager) {
            override fun loadMoreItems() {
                this@ProductFragment.isLoading = true
                skip += 20

                Handler().postDelayed({
                    callProductsListingApi()
                }, 1000)
            }

            override val isLastPage: Boolean
                get() = this@ProductFragment.isLastPage
            override val isLoading: Boolean
                get() = this@ProductFragment.isLoading
        })
    }


    @SuppressLint("NotifyDataSetChanged")
    private val getProductsObserver = Observer<BaseEvent<OnGetProductsEvent>> {
        when (val event = it.getEventIfNotHandled()) {
            is OnGetProductsEvent.StartLoading -> {
                if (!binding.swipeRefresh.isRefreshing) {
                    if (!isLoading) {
                        showLoader()
                    }
                }
            }

            is OnGetProductsEvent.StopLoading -> {
                binding.shimmer.gone()
                if (binding.swipeRefresh.isRefreshing) {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        binding.swipeRefresh.isRefreshing = false
                    }, 50)
                } else {
                    val handler = Handler(Looper.getMainLooper())
                    handler.postDelayed({
                        hideLoader()
                    }, 0)
                }
            }

            is OnGetProductsEvent.OnGetProductsModel -> {
                hideLoader()
                total = event.model!!.total.toInt()

                if (skip == 0) {
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                    adapter.addAll(event.model.products)
                    if (skip < total!!) {
                        adapter.addLoadingFooter()
                    } else {
                        isLastPage = true
                    }
                } else {
                    adapter.removeLoadingFooter()
                    isLoading = false
                    adapter.addAll(event.model.products)
                    if (skip < total!!) {
                        adapter.addLoadingFooter()
                    } else {
                        isLastPage = true
                    }
                }
            }
            is OnGetProductsEvent.Error -> {
                Log.d("Farhan", "${event.error}")

                enableTouch()
                snack(event.error!!)
            }

            is OnGetProductsEvent.Exception -> {
                Log.d("Farhan", "${event.exception?.message.toString()}")

                enableTouch()
                snack(event.exception?.message.toString())
            }
            else -> {

            }
        }
    }

    private fun callProductsListingApi() {
        if(isNetworkAvailable(requireContext())) {
            val hashMap = HashMap<String, Any>()
            hashMap["skip"] = skip
            hashMap["limit"] = 20
            viewModel.onGetProductsListing(hashMap)
        }else{
            snack(Constant.ErrorMessageNoConnectivity)
            binding.swipeRefresh.isRefreshing = false
            isLoading = false
        }

    }


    private fun showLoader() {
        binding.shimmer.visible()
        binding.rvProducts.gone()
    }

    private fun hideLoader() {
        binding.shimmer.gone()
        binding.rvProducts.visible()
    }

    private fun onRefreshData() {
        StaticMethods.hideSoftKeyboard(requireActivity())
        adapter.clear()
        isLoading = false
        isLastPage = false
        skip = 0
        val handler = Handler()
        handler.postDelayed({
            callProductsListingApi()
        }, 400)
    }


    override fun onMyItemsClick(data: Product, position: Int) {
        addFragment(
            ProductDetailFragment.getInstance(data),
            R.id.fragment_container_main,
            addBackStack = true,
            clearBackStack = false
        )
    }
}