package com.alanfeng.goal.android

import com.alanfeng.goal.uistate.HomeVM
import org.koin.dsl.module


val appModule = module {
    single { HomeVM() }
}