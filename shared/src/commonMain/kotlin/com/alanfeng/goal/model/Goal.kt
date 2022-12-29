package com.alanfeng.goal.model

import kotlinx.datetime.LocalDateTime

data class Goal(
    var name: String,
    var tags: List<String>,
    val end_at: Long,
    val created_at: Long,
    var enable: Boolean,
    val finished_at: Long?,
    val id: Long = 0,
)
