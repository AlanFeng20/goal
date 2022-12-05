package com.alanfeng.goal.model


@kotlinx.serialization.Serializable
data class Goal(
    val id: Long,
    val parent_id: Long?,
    val name: String,
    var tags: List<String>,
    val created_at: Long,
    val flag_del: Boolean,
    val enable: Boolean
)