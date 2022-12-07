package com.alanfeng.goal.model

import com.alanfeng.goal.SqlIdPlaceholder


@kotlinx.serialization.Serializable
data class Goal(
    var parent_id: Long?,
    var name: String,
    var tags: List<String>,
    val created_at: Long,
    var flag_del: Boolean,
    var enable: Boolean,
    val id: Long= SqlIdPlaceholder,
)