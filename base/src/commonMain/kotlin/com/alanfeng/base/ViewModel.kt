package com.alanfeng.base

import com.alanfeng.base.event.EventBus
import com.alanfeng.base.event.events.CoroutineErrorEvent
import kotlinx.coroutines.*

open class ViewModel {
    val scope = MainScope() + CoroutineExceptionHandler { _, throwable ->
        EventBus.send(CoroutineErrorEvent(throwable))
        Logs.e("ViewModel coroutine", throwable)
    }

    val processing=AccessStateFlow(false)

    var <T> AccessStateFlow<T>.value
        get() = state.value
        set(value) {
            this.state.value = value
        }

    suspend fun withProcess(call:suspend ()->Unit){
        coroutineScope {
            launch {
                delay(500)
                if(processing.value){
                    processing.value=true
                }
            }
        }
        call()
        processing.value=false
    }
}