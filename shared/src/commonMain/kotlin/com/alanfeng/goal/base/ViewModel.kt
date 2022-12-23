package com.alanfeng.goal.base

import kotlinx.coroutines.MainScope

open class ViewModel {
    val scope= MainScope()

    var <T> AccessStateFlow<T>.value
        get() = state.value
        set(value) {
            this.state.value = value
        }
}