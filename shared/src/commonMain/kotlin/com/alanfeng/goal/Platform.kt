package com.alanfeng.goal

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform