package com.alanfeng.goal

import io.ktor.client.*

expect fun defaultHttpClient():HttpClient

val httpClient by lazy {
    defaultHttpClient()
}