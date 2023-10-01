package com.app.dummyproduct.data.remote.calls

import com.app.dummyproduct.constants.Constant
import com.app.dummyproduct.utils.PrefManager
import com.app.dummyproduct.data.remote.retrofit.Result
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteCall @Inject constructor(
    private val networkCall: NetworkCall,
    private val pref : PrefManager
) {

    suspend fun onLoginApi(map: Map<String, String>): Result {
        return networkCall.post<String>("${Constant.BASE_URL}auth/login", map)
    }

    suspend fun onGetProductsListing(map: Map<String, Any>): Result {
        val pref = pref.getString("accessToken", "")
        val hashMapToken = HashMap<String, String>()
        hashMapToken["Authorization"] = "Bearer $pref"
        return networkCall.get<String>("${Constant.BASE_URL}products", hashMapToken, map)
    }

}