package com.app.dummyproduct.data.remote.calls

import android.annotation.SuppressLint
import android.util.Log
import com.app.dummyproduct.data.remote.Connectivity
import com.app.dummyproduct.data.remote.retrofit.WebService
import com.app.dummyproduct.data.remote.retrofit.Result
import org.json.JSONObject
import java.net.HttpURLConnection
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkCall @Inject constructor(
    val webService: WebService,
    val connectivity: Connectivity
) {

    @SuppressLint("NewApi")
    suspend inline fun <reified T : Any> get(
        endpoint: String,
        tokenMap: Map<String, String>?,
        queryMap: Map<String, Any>
    ): Result =
        try {

            val response = webService.get(endpoint, "application/json", tokenMap!!, queryMap)
            Log.d("GENERAL_REQ", "Headers---> ${response.headers()}")
            Log.d("GENERAL_REQ", "RawResponse CODE ---> ${response.code()}")
            if (response.code() == HttpURLConnection.HTTP_OK) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("")
                }
            } else if (response.code() == 401) {
                Result.UnAuthorized("")
            } else {

                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Result.Error(jObjError.getString("message"))
                } catch (e: java.lang.Exception) {
                    Result.Error(e.message.toString())
                }
            }
        } catch (exception: Exception) {
            Log.d("GENERAL_REQ", "Exception ${exception.message!!}")
            Result.Exception(exception)
        }


    @SuppressLint("NewApi")
    suspend inline fun <reified T : Any> post(
        endpoint: String,
        queryMap: Map<String, String>
    ): Result =
        try {

            val response = webService.post(endpoint, "application/json", queryMap)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error("")
                }
            } else if (response.code() == 401) {
                Result.UnAuthorized("")
            } else {

                try {
                    val jObjError = JSONObject(response.errorBody()!!.toString())
                    Result.Error(jObjError.getString("message"))
                } catch (e: java.lang.Exception) {
                    Result.Error(e.message.toString())
                }
            }
        } catch (exception: Exception) {
            Log.d("GENERAL_REQ", "Exception ${exception.message!!}")
            Result.Exception(exception)
        }


}