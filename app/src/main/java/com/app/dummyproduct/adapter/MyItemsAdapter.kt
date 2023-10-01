package com.app.dummyproduct.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.dummyproduct.data.model.responses.Product
import com.app.dummyproduct.R
import com.app.dummyproduct.databinding.*
import com.app.dummyproduct.utils.Utils.Companion.loadImagesWithGlideExt
import com.app.dummyproduct.utils.roundToOneDecimalPlace
import java.util.*

class ProductAdapter(
    private val context: Context,
    private val itemClickListener: OnMyItemsClick,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    private var list: MutableList<Product>? = ArrayList<Product>()
    private var isLoadingAdded = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM -> {
                (holder as MyItemsViewHolder).bind(position, context)
            }
            LOADING -> {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val binding: ProductItemsBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.product_items, parent, false
                )
                viewHolder = MyItemsViewHolder(binding, list!!, itemClickListener)
            }
            LOADING -> {
                val v2: View = inflater.inflate(R.layout.item_grid_progress, parent, false)
                viewHolder = LoadingVH(v2)
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return if ((position == list!!.size - 1 && isLoadingAdded)) LOADING else ITEM
    }

    fun add(r: Product) {
        list!!.add(r)
        notifyItemInserted(list!!.size - 1)
    }

    fun addAllSearch(moveResults: List<Product?>) {
        for (result in moveResults) {
            addSearch(result)
        }
    }

    private fun addSearch(r: Product?) {
        list!!.add(r!!)
        notifyItemInserted(list!!.size - 1)
    }


    fun addAll(moveResults: List<Product>) {
        for (result: Product in moveResults) {
            add(result)
        }
    }

    fun remove(r: Product) {
        val position = list!!.indexOf(r)
        if (position > -1) {
            list!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removePosition(position: Int) {
        list!!.removeAt(position)
        notifyItemRemoved(position)
    }


    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    val isEmpty: Boolean
        get() = itemCount == 0

    fun addLoadingFooter() {
        isLoadingAdded = true
        //add(DummyData())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position = list!!.size - 1
        val result: Product? = getItem(position)
        if (result != null) {
            list!!.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItem(position: Int): Product {
        return list!![position]
    }


    private inner class LoadingVH(itemView: View?) : RecyclerView.ViewHolder(itemView!!)

    companion object {
        private val ITEM = 0
        private val LOADING = 1
    }
}

class MyItemsViewHolder(
    binding: ProductItemsBinding,
    list: List<Product>,
    clickListener: OnMyItemsClick,
) : RecyclerView.ViewHolder(binding.root) {
    var width: Int = 0
    var binding: ProductItemsBinding? = null
    var list: List<Product>? = null
    var clickListener: OnMyItemsClick? = null

    init {
        this.binding = binding
        this.list = list
        this.clickListener = clickListener
    }

    fun bind(position: Int, context: Context) {
        binding!!.tvName.text = list!![position].title
        binding!!.tvPrice.text = "$${list!![position].price}"
        if (list!![position].thumbnail.isNotEmpty()) {
            binding!!.image.loadImagesWithGlideExt(list!![position].thumbnail)
        }else{
            binding!!.image.loadImagesWithGlideExt("")
        }

        binding!!.rating.text = roundToOneDecimalPlace(list!![position].rating)
        binding!!.relativeLayout.setOnClickListener {
            clickListener!!.onMyItemsClick(list!![position], position)
        }
    }
}

interface OnMyItemsClick {
    fun onMyItemsClick(data: Product, position: Int)
}