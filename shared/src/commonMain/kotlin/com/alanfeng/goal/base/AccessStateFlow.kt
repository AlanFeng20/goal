package com.alanfeng.goal.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class AccessStateFlow<T>(
    init: T
) {
    internal val state = MutableStateFlow(init)

    @Composable
    fun collectAsState(
        context: CoroutineContext = EmptyCoroutineContext
    ) = state.collectAsState(context)

}

