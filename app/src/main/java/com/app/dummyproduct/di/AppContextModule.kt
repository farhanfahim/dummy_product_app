package com.app.dummyproduct.di

import android.content.Context
import com.app.dummyproduct.app.DummyProduct
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppContextModule {

    @Singleton
    @Provides
    fun getApplicationContext(): Context = DummyProduct.getAppContext().applicationContext

}