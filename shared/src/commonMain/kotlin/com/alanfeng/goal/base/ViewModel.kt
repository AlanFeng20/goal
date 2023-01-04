package com.alanfeng.goal.base

import com.alanfeng.goal.XLog
import com.alanfeng.goal.event.EventBus
import com.alanfeng.goal.event.events.CoroutineErrorEvent
import kotlinx.coroutines.*

open class ViewModel {
    val scope = MainScope() + CoroutineExceptionHandler { _, throwable ->
        EventBus.send(CoroutineErrorEvent(throwable))
        XLog.e("ViewModel coroutine", throwable)
    }

    var <T> AccessStateFlow<T>.value
        get() = state.value
        set(value) {
            this.state.value = value
        }
}