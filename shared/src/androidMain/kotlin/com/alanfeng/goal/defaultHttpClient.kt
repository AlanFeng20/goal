package com.alanfeng.goal

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.compression.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import okhttp3.Protocol

actual fun defaultHttpClient(): HttpClient= HttpClient(OkHttp){
    install(ContentNegotiation){
        json()
    }
    install(ContentEncoding){
        deflate()
        gzip()
    }

    install(HttpCache)
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.HEADERS
        filter { request ->
            request.url.host.contains("ktor.io")
        }
    }
    install(HttpCookies)
    install(HttpTimeout)
    engine {
        config {
            protocols(listOf(Protocol.HTTP_2))
        }
    }
}