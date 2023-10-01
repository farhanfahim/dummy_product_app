package com.app.dummyproduct.data.remote.retrofit

import com.app.dummyproduct.data.model.APIError

sealed class Result {
    class Success(val response: Any) : Result()
    class UnAuthorized(val msg: String) : Result()
    class UnProcessable(val msg: String) : Result()
    class Error(val error: String) : Result()
    class Exception(val exception: java.lang.Exception) : Result()
    class RetrofitError(val apiError: APIError?) : Result()

}