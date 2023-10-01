package com.app.dummyproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dummyproduct.data.events.*
import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.model.responses.ProductResponse
import com.app.dummyproduct.data.remote.repo.*
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import com.app.dummyproduct.data.remote.retrofit.Result
import com.google.gson.Gson
import javax.net.ssl.SSLHandshakeException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val gson: Gson,
    private val productsRepository: ProductsRepository
) : ViewModel() {

    private val getProductsNavEvent = MutableLiveData<BaseEvent<OnGetProductsEvent>>()
    val getProductsEvent: LiveData<BaseEvent<OnGetProductsEvent>> = getProductsNavEvent

    fun onGetProductsListing(map: Map<String, Any>) {
        viewModelScope.launch {
            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StartLoading)
            try {
                productsRepository.onGetProductsListing(map).let {
                    when (it) {
                        is Result.Success -> {
                            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StopLoading)
                            try {

                                val data: ProductResponse = it.response as ProductResponse

                                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.OnGetProductsModel(data))

                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StopLoading)
                            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StopLoading)
                            it.apiError.let {
                                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.Error(it!!.message as String?))
                            }
                        }
                        is Result.Exception -> {
                            getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException
                                || it.exception is ConnectException || it.exception is SSLHandshakeException
                                || it.exception is TimeoutException
                            ) {
                            } else {
                                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.Error(it.exception.message))
                            }
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.StopLoading)
                getProductsNavEvent.value = BaseEvent(OnGetProductsEvent.Error(e.message))
            }
        }
    }

}