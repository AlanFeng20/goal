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

    private val tagMap: HashMap<XLogLevel, String> = hashMapOf(
        XLogLevel.VERBOSE to "💜 VERBOSE",
        XLogLevel.DEBUG to "💚 DEBUG",
        XLogLevel.INFO to "💙 INFO",
        XLogLevel.WARNING to "💛 WARN",
        XLogLevel.ERROR to "❤️ ERROR",
        XLogLevel.ASSERT to "💞 ASSERT"
    )

    actual fun log(
        priority: XLogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?,
    ) {
        if (priority == XLogLevel.ASSERT) {
            assert(crashAssert) { buildLog(priority, tag, throwable, message) }
        } else {
            println(buildLog(priority, tag, throwable, message))
        }
    }

    fun setTag(level: XLogLevel, tag: String) {
        tagMap[level] = tag
    }

    fun setDateFormatterString(formatter: String) {
        dateFormatter.dateFormat = formatter
    }

    private fun getCurrentTime() = dateFormatter.stringFromDate(NSDate())

    private fun buildLog(priority: XLogLevel, tag: String?, throwable: Throwable?, message: String?): String {
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