package com.alanfeng.goal.model

import com.alanfeng.goal.SqlIdPlaceholder
import kotlinx.datetime.DayOfWeek

@kotlinx.serialization.Serializable
data class Task(
    var goal_id: Long,
    var name: String,
    var remind_way: String?,
    var remind_time: List<RemindTime>,
    val created_at: Long,
    var flag_del: Boolean,
    var enable: Boolean,
    var finish_mark: String?,
    val id: Long= SqlIdPlaceholder,
)

@kotlinx.serialization.Serializable
data class RemindTime(
    var hour: Int,
    var minutes: Int,
    var weekDays: Set<DayOfWeek>
)

data class TaskResult(
    val task_id: Long,
    val is_success: Long,
    val success_mark: String?,
    val emotion: String?,
    val fail_hardness: Long?,
    val fail_mark: String?,
    val created_at: Long,
    val id: Long= SqlIdPlaceholder,
)
