package com.app.dummyproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.dummyproduct.data.events.BaseEvent
import com.app.dummyproduct.data.events.OnLoginEvent
import com.app.dummyproduct.data.model.responses.LoginResponse
import com.app.dummyproduct.data.remote.repo.SignInRepository
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException
import com.app.dummyproduct.data.remote.retrofit.Result
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val gson: Gson,
    private val loginRepository: SignInRepository
) : ViewModel() {

    private val loginNavEvent = MutableLiveData<BaseEvent<OnLoginEvent>>()
    val loginEvent: LiveData<BaseEvent<OnLoginEvent>> = loginNavEvent

    fun onLogin(map: Map<String, String>) {
        viewModelScope.launch {
            loginNavEvent.value = BaseEvent(OnLoginEvent.StartLoading)
            try {
                loginRepository.onLogin(map).let {
                    when (it) {
                        is Result.Success -> {
                            loginNavEvent.value = BaseEvent(OnLoginEvent.StopLoading)

                            try {
                                val data: LoginResponse = it.response as LoginResponse

                                loginRepository.onSaveAccessToken(data.token)
                                loginRepository.onSaveLoginData(data)
                                loginNavEvent.value = BaseEvent(OnLoginEvent.OnLoginData(data))

                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                loginNavEvent.value = BaseEvent(OnLoginEvent.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            loginNavEvent.value = BaseEvent(OnLoginEvent.StopLoading)
                            loginNavEvent.value = BaseEvent(OnLoginEvent.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            loginNavEvent.value = BaseEvent(OnLoginEvent.StopLoading)
                            it.apiError.let {
                                loginNavEvent.value =
                                    BaseEvent(OnLoginEvent.Error(it!!.message))
                            }
                        }
                        is Result.Exception -> {
                            loginNavEvent.value = BaseEvent(OnLoginEvent.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException
                                || it.exception is ConnectException || it.exception is SSLHandshakeException
                                || it.exception is TimeoutException
                            ) {
                            } else {
                                loginNavEvent.value =
                                    BaseEvent(OnLoginEvent.Error(it.exception.message))
                            }
                        }
                        else -> {}
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                loginNavEvent.value = BaseEvent(OnLoginEvent.StopLoading)
                loginNavEvent.value = BaseEvent(OnLoginEvent.Error(e.message))
            }
        }
    }

}