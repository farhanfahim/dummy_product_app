package com.app.dummyproduct.data.events

sealed class OnCreateCustomerEvent {
    object StartLoading : OnCreateCustomerEvent()
    object StopLoading : OnCreateCustomerEvent()
    //class OnGetCustomerModel(val model: CreatePaymentCustomerResponse?) : OnCreateCustomerEvent()
    class Exception(val exception: java.lang.Exception?) : OnCreateCustomerEvent()
    class Error(val error: String?) : OnCreateCustomerEvent()
    class UnAuthorized(val error: String?) : OnCreateCustomerEvent()
}