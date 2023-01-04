package com.alanfeng.goal

import kotlin.native.concurrent.ThreadLocal

enum class XLogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    ASSERT,
}

expect class PlatFormLogger() {
    fun log(priority: XLogLevel, tag: String?, throwable: Throwable?, message: String?)
}

@ThreadLocal
private var enable = false

object XLog {
    private val logger = PlatFormLogger()


    fun v(tag: String? = null, msg: () -> String) {
        if (enable) {
            logger.log(XLogLevel.VERBOSE, tag, null, msg())
        }
    }

    fun i(tag: String?, msg: () -> String) {
        if (enable) {
            logger.log(XLogLevel.INFO, tag, null, msg())
        }
    }

    fun w(tag: String, msg: () -> String) {
        if (enable) {
            logger.log(XLogLevel.INFO, tag, null, msg())
        }
    }

    fun d(tag: String, msg: () -> String) {
        if (enable) {
            logger.log(XLogLevel.DEBUG, tag, null, msg())
        }
    }


    fun e(tag: String? = null, throwable: Throwable? = null, msg: (() -> String)) {
        if (enable) {
            logger.log(XLogLevel.ERROR, tag, throwable, msg())
        }
    }

    fun e(tag: String? = null, throwable: Throwable, msg: String? = null) {
        if (enable) {
            logger.log(XLogLevel.ERROR, tag, throwable, msg)
        }
    }
}