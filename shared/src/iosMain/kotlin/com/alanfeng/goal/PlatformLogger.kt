package com.alanfeng.goal

/**
 *
 * @author: fenglongjun
 *
 * @create: 2022-12-30 16:59
 **/

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSThread

private const val CALL_STACK_INDEX = 8


actual class PlatFormLogger{

    private val defaultTag: String="App"
    private val coroutinesSuffix: Boolean=true
    private var crashAssert = false

    private val dateFormatter = NSDateFormatter().apply {
        dateFormat = "MM-dd HH:mm:ss.SSS"
    }

    private val tagMap: HashMap<LogLevel, String> = hashMapOf(
        LogLevel.VERBOSE to "💜 VERBOSE",
        LogLevel.DEBUG to "💚 DEBUG",
        LogLevel.INFO to "💙 INFO",
        LogLevel.WARNING to "💛 WARN",
        LogLevel.ERROR to "❤️ ERROR",
        LogLevel.ASSERT to "💞 ASSERT"
    )

    actual fun log(
        priority: LogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?,
    ) {
        if (priority == LogLevel.ASSERT) {
            assert(crashAssert) { buildLog(priority, tag, throwable, message) }
        } else {
            println(buildLog(priority, tag, throwable, message))
        }
    }

    fun setTag(level: LogLevel, tag: String) {
        tagMap[level] = tag
    }

    fun setDateFormatterString(formatter: String) {
        dateFormatter.dateFormat = formatter
    }

    private fun getCurrentTime() = dateFormatter.stringFromDate(NSDate())

    private fun buildLog(priority: LogLevel, tag: String?, throwable: Throwable?, message: String?): String {
        val baseLogString = "${getCurrentTime()} ${tagMap[priority]} ${tag ?: performTag(defaultTag)} - $message"
        return if (throwable != null) {
            "$baseLogString\n${throwable.stackTraceToString()}"
        } else {
            baseLogString
        }
    }

    // find stack trace
    private fun performTag(tag: String): String {
        val symbols = NSThread.callStackSymbols
        if (symbols.size <= CALL_STACK_INDEX) return tag

        return (symbols[CALL_STACK_INDEX] as? String)?.let {
            createStackElementTag(it)
        } ?: tag
    }

    internal fun createStackElementTag(string: String): String {
        var tag = string
        tag = tag.substringBeforeLast('$')
        tag = tag.substringBeforeLast('(')
        if (tag.contains("$")) {
            // coroutines
            tag = tag.substring(tag.lastIndexOf(".", tag.lastIndexOf(".") - 1) + 1)
            tag = tag.replace("$", "")
            tag = tag.replace("COROUTINE", if (coroutinesSuffix) "[async]" else "")
        } else {
            // others
            tag = tag.substringAfterLast(".")
            tag = tag.replace("#", ".")
        }
        return tag
    }
}