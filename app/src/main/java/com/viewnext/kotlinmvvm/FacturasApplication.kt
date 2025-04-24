package com.viewnext.kotlinmvvm

import android.app.Application
import com.viewnext.kotlinmvvm.data_retrofit.data.AppContainer
import com.viewnext.kotlinmvvm.data_retrofit.data.DefaultAppContainer

class FacturasApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}