package com.alanfeng.goal.uistate

import com.alanfeng.goal.base.AccessStateFlow
import com.alanfeng.goal.base.ViewModel
import org.koin.core.component.KoinComponent

class AppVM: ViewModel(),KoinComponent {
    val error= AccessStateFlow<Throwable?>(null)

}