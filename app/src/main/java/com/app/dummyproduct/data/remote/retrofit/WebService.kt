package com.app.dummyproduct.data.remote.retrofit

import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.model.responses.ProductResponse
import retrofit2.http.*
interface WebService {

    @FormUrlEncoded
    @POST
    suspend fun post(
        @Url endpoint: String,
        @Header("Accept") header: String,
        @FieldMap query: @JvmSuppressWildcards Map<String, String>
    ): retrofit2.Response<LoginResponse>

    @GET
    suspend fun get(
        @Url endpoint: String,
        @Header("Accept") header: String,
        @HeaderMap headerMap: @JvmSuppressWildcards Map<String, String>,
        @QueryMap query: @JvmSuppressWildcards Map<String, Any>
    ): retrofit2.Response<ProductResponse>
}