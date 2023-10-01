package com.app.dummyproduct.data.remote.repo

import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.remote.calls.RemoteCall
import javax.inject.Inject
import javax.inject.Singleton
import com.app.dummyproduct.utils.PrefManager

@Singleton
class SplashRepository @Inject constructor(
    private val prefManager: PrefManager
) {

    fun onGetUserData(): LoginResponse? {
        if (prefManager.getObject("loginModel", LoginResponse::class.java) != null) {
            return prefManager.getObject("loginModel", LoginResponse::class.java)!!
        } else {
            return null
        }
    }

}