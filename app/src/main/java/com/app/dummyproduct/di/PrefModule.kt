package com.app.dummyproduct.di

import android.content.Context
import android.content.SharedPreferences
import com.app.dummyproduct.app.DummyProduct
import com.app.dummyproduct.constants.Constant.SHARED_PREF_FILE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object PrefModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(): SharedPreferences =
        DummyProduct.getAppContext().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)
}