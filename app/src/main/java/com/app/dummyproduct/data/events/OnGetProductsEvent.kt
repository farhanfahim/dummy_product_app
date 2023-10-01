package com.app.dummyproduct.data.events

import com.app.dummyproduct.data.model.responses.ProductResponse

sealed class OnGetProductsEvent {
    object StartLoading : OnGetProductsEvent()
    object StopLoading : OnGetProductsEvent()
    class OnGetProductsModel(val model: ProductResponse?) : OnGetProductsEvent()
    class Exception(val exception: java.lang.Exception?) : OnGetProductsEvent()
    class Error(val error: String?) : OnGetProductsEvent()
}