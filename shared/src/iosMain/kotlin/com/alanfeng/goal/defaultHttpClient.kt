package com.alanfeng.goal

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*

actual fun defaultHttpClient(): HttpClient= HttpClient(Darwin){
    install(ContentNegotiation){
        json()
    }
    install(ContentEncoding){
        deflate()
        gzip()
    }

    install(HttpCache)
    install(Logging) {
        logger = XLog.DEFAULT
        level = XLogLevel.HEADERS
        filter { request ->
            request.url.host.contains("ktor.io")
        }
    }
    install(HttpCookies)
    install(HttpTimeout)
    engine {

    }
}