package com.alanfeng.goal

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow

object EventBus {
    private val sharedFlowMap = mutableMapOf<Event, MutableSharedFlow<Event>>()

    suspend fun send(event: Event) {
        getSharedFlow(event).emit(event)
    }


    private fun getSharedFlow(event: Event)= sharedFlowMap[event] ?: MutableSharedFlow<Event>().also {
        sharedFlowMap[event] = it
    }
}

enum class Event {

}