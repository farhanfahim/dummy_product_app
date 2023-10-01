package com.app.dummyproduct.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.remote.repo.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val splashRepository: SplashRepository
) : ViewModel() {

    var userData: MutableLiveData<LoginResponse> = MutableLiveData()

    fun getUserData(){
        userData.value = splashRepository.onGetUserData()
    }

}