package com.alanfeng.goal.model

data class Goal(
    var name: String,
    var tags: List<String>,
    val last_days: Int,
    val created_at: Long,
    var enable: Boolean,
    val id: Long = 0,
)