package com.alanfeng.goal

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alanfeng.goal.android.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@SuppressLint("StaticFieldLeak")
class App:Application() {
    companion object{
        lateinit var context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context =applicationContext

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }

}