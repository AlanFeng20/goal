package com.alanfeng.goal

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
class App:Application() {
    companion object{
        lateinit var context:Context
    }
    override fun onCreate() {
        super.onCreate()
        context =applicationContext
    }

}