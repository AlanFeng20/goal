package com.alanfeng.goal.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.toDateTimePeriod

data class Goal(
    var name: String,
    var tags: List<String>,
    val end_at: Long,
    val created_at: Long,
    var enable: Boolean,
    val finished_at: Long?,
    val id: Long = 0,
) {
    val lastDays
        get() = (Instant.fromEpochMilliseconds(end_at) - Instant.fromEpochMilliseconds(created_at)).toDateTimePeriod().days

    val isOverEnd
        get() = Clock.System.now().toEpochMilliseconds() > end_at
}
