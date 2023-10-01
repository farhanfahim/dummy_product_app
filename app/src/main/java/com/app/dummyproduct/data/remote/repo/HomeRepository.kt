package com.app.dummyproduct.data.remote.repo

import javax.inject.Inject
import javax.inject.Singleton
import com.app.dummyproduct.utils.PrefManager

@Singleton
class HomeRepository @Inject constructor(
    private val prefManager: PrefManager
) {

    fun clearAllPreferences(){
        prefManager.clearAllPreferences()
    }

}