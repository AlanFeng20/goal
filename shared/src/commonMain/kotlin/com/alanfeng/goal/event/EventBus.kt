package com.alanfeng.goal.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

object EventBus {
    private val stickyList = mutableListOf<Any>()
    private val observerMap= mutableMapOf<KClass<*>,MutableList<(Any)->Unit>>()
    private val eventFlow = MutableSharedFlow<Any>().apply { 
        MainScope().launch {
            collect{ ev ->
                observerMap[ev]?.forEach {
                    it.invoke(it)
                }
            }
        }
    }
    
    
    fun send(event: Any,sticky:Boolean=false) {
        if(sticky){
            stickyList.add(event)
            val callback= observerMap[event]
            callback?.forEach {
                it.invoke(event)
            }
        }else{
            GlobalScope.launch {
                eventFlow.emit(event)
            }
        }
    }

    fun <T:Any> observe(event: KClass<T>,observer:(T)->Unit){
        var list= observerMap[event]
        if(list==null){
            list= mutableListOf()

            observerMap[event]= list
        }
        list.add(observer as (Any) -> Unit)
        val find= stickyList.lastOrNull { it::class==event }
        if(find!=null){
            observer(find)
            stickyList.remove(find)
        }
    }
    
    fun removeObserver(event: KClass<*>,observer:(Any)->Unit){
        val list = observerMap[event]
        list?.run {
            remove(observer)
            if (isEmpty()){
                observerMap.remove(event)
            }
        }
    }

}

@Composable
fun <T:Any> withEvent(event: KClass<T>,observer:(T)->Unit) {
    DisposableEffect(true) {
        EventBus.observe(event, observer)
        onDispose {
            EventBus.removeObserver(event, observer as (Any) -> Unit)
        }
    }

}
