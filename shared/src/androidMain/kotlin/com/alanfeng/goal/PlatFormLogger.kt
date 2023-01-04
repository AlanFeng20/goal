package com.alanfeng.goal

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.util.regex.Pattern
import kotlin.math.min

/**
 *
 * @author: fenglongjun
 *
 * @create: 2022-12-30 16:43
 **/
actual class PlatFormLogger {
    actual fun log(
        priority: XLogLevel,
        tag: String?,
        throwable: Throwable?,
        message: String?
    ) {
        val debugTag = tag ?: performTag()

        val fullMessage = if (message != null) {
            if (throwable != null) {
                "$message\n${throwable.stackTraceString}"
            } else {
                message
            }
        } else throwable?.stackTraceString ?: return

        val length = fullMessage.length
        if (length <= MAX_LOG_LENGTH) {
            // Fast path for small messages which can fit in a single call.
            if (priority == XLogLevel.ASSERT) {
                Log.wtf(debugTag, fullMessage)
            } else {
                Log.println(priority.toValue(), debugTag, fullMessage)
            }
            return
        }

        // Slow path: Split by line, then ensure each line can fit into Log's maximum length.
        // TODO use lastIndexOf instead of indexOf to batch multiple lines into single calls.
        var i = 0
        while (i < length) {
            var newline = fullMessage.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = fullMessage.substring(i, end)
                if (priority.toValue() == Log.ASSERT) {
                    Log.wtf(debugTag, part)
                } else {
                    Log.println(priority.toValue(), debugTag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }
    private fun performTag(): String {
        val thread = Thread.currentThread().stackTrace

        return if (thread.size >= CALL_STACK_INDEX) {
            thread[CALL_STACK_INDEX].run {
                "${createStackElementTag(className)}\$$methodName"
            }
        } else {
            defaultTag
        }
    }

    private val Throwable.stackTraceString
        get(): String {
            // DO NOT replace this with Log.getStackTraceString() - it hides UnknownHostException, which is
            // not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }
    private fun createStackElementTag(className: String): String {
        var tag = className
        val m = anonymousClass.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH ) {
            tag
        } else tag.substring(0, MAX_TAG_LENGTH)
    }
    private fun XLogLevel.toValue() = when (this) {
        XLogLevel.VERBOSE -> Log.VERBOSE
        XLogLevel.DEBUG -> Log.DEBUG
        XLogLevel.INFO -> Log.INFO
        XLogLevel.WARNING -> Log.WARN
        XLogLevel.ERROR -> Log.ERROR
        XLogLevel.ASSERT -> Log.ASSERT
    }
    companion object {
        private val anonymousClass = Pattern.compile("(\\$\\d+)+$")
        private const val defaultTag: String=""
        private const val MAX_LOG_LENGTH = 4000
        private const val MAX_TAG_LENGTH = 23
        private const val CALL_STACK_INDEX = 9
    }
}