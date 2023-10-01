package com.app.dummyproduct.viewmodel

import androidx.lifecycle.ViewModel
import com.app.dummyproduct.data.remote.repo.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    fun clearAllPreferences(){
       homeRepository.clearAllPreferences()
    }

}