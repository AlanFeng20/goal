package com.alanfeng.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext

actual val dispatcherIO: CoroutineDispatcher = newFixedThreadPoolContext(30, "ios")