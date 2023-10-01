package com.app.dummyproduct.data.remote.repo

import com.app.dummyproduct.data.remote.calls.RemoteCall
import javax.inject.Inject
import javax.inject.Singleton
import com.app.dummyproduct.data.remote.retrofit.Result

@Singleton
class ProductsRepository @Inject constructor(
    private val remoteCall: RemoteCall,
) {

    suspend fun onGetProductsListing(map: Map<String, Any>): Result {
        return remoteCall.onGetProductsListing(map)
    }

}