package com.app.dummyproduct.data.remote.repo

import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.remote.calls.RemoteCall
import com.app.dummyproduct.utils.PrefManager
import retrofit2.http.FieldMap
import javax.inject.Inject
import javax.inject.Singleton
import com.app.dummyproduct.data.remote.retrofit.Result

@Singleton
class SignInRepository @Inject constructor(
    private val remoteCall: RemoteCall,
    private val prefManager: PrefManager
) {

    suspend fun onLogin(@FieldMap map: Map<String, String>): Result {
        return remoteCall.onLoginApi(map)
    }

    fun onSaveAccessToken(accessToken: String){
        prefManager.putString("accessToken", accessToken)
    }

    fun onSaveLoginData(userLoginResponse: LoginResponse){
        prefManager.putObject("loginModel", userLoginResponse)
    }

}