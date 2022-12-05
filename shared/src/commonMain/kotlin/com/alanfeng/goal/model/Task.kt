package com.alanfeng.goal.model

import kotlinx.datetime.DayOfWeek

@kotlinx.serialization.Serializable
data class Task(
    val id: Long,
    val goal_id: Long,
    val name: String,
    val remind_way: String?,
    val remind_time: RemindTime,
    val created_at: Long,
    val flag_del: Boolean,
    val enable: Boolean,
    val finish_mark: String?
)

@kotlinx.serialization.Serializable
data class RemindTime(
    val hour: Int,
    val minutes: Int,
    val weekDays: List<DayOfWeek>
)
data class TaskResult(
     val id: Long,
     val task_id: Long,
     val is_success: Long,
     val success_mark: String?,
     val emotion: String?,
     val fail_hardness: Long?,
     val fail_mark: String?,
     val created_at: Long
)
