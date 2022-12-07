package com.alanfeng.goal

import io.ktor.client.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newFixedThreadPoolContext

actual val dispatcherIO: CoroutineDispatcher = newFixedThreadPoolContext(30, "ios")