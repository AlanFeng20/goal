package com.alanfeng.base

import kotlin.native.concurrent.ThreadLocal

enum class LogsLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
}

expect class PlatFormLogger() {
    fun log(priority: LogsLevel, tag: String?, throwable: Throwable?, message: String?)
}

@ThreadLocal
private var enable = false

object Logs {
    private val logger = PlatFormLogger()

    fun init(_enable:Boolean){
        enable=_enable
    }

    fun v(tag: String? = null, msg: () -> String) {
        if (enable) {
            logger.log(LogsLevel.VERBOSE, tag, null, msg())
        }
    }

    fun i(tag: String?, msg: () -> String) {
        if (enable) {
            logger.log(LogsLevel.INFO, tag, null, msg())
        }
    }

    fun w(tag: String, msg: () -> String) {
        if (enable) {
            logger.log(LogsLevel.INFO, tag, null, msg())
        }
    }

    fun d(tag: String, msg: () -> String) {
        if (enable) {
            logger.log(LogsLevel.DEBUG, tag, null, msg())
        }
    }


    fun e(tag: String? = null, throwable: Throwable? = null, msg: (() -> String)) {
        if (enable) {
            logger.log(LogsLevel.ERROR, tag, throwable, msg())
        }
    }

    fun e(tag: String? = null, throwable: Throwable, msg: String? = null) {
        if (enable) {
            logger.log(LogsLevel.ERROR, tag, throwable, msg)
        }
    }
}