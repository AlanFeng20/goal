package com.alanfeng.goal.uistate

import com.alanfeng.base.AccessStateFlow
import com.alanfeng.base.ViewModel
import org.koin.core.component.KoinComponent

class AppVM: ViewModel(),KoinComponent {
    val error= AccessStateFlow<Throwable?>(null)

}