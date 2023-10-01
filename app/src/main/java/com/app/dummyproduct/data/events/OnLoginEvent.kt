package com.app.dummyproduct.data.events

import com.app.dummyproduct.data.model.responses.LoginResponse

sealed class OnLoginEvent {
    object StartLoading : OnLoginEvent()
    object StopLoading : OnLoginEvent()
    class OnLoginData(val model: LoginResponse?) : OnLoginEvent()
    class Exception(val exception: java.lang.Exception?) : OnLoginEvent()
    class Error(val error: String?) : OnLoginEvent()
    class UnAuthorized(val error: String?) : OnLoginEvent()
}