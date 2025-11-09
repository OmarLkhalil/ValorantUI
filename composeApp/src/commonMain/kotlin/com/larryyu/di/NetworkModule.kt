package com.larryyu.di

import com.larryyu.domain.utils.RetrofitConstants
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json

object NetworkModule {

    fun provideKtorClient(enableNetworkLogs: Boolean = true): HttpClient {
        return HttpClient {
            // 🌐 الإعدادات الأساسية للطلب
            install(DefaultRequest) {
                headers {
                    appendIfNameAbsent(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                }
            }

            // 🔁 إعادة المحاولة عند حدوث أخطاء في السيرفر
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = 2)
            }

            // 🧩 تهيئة JSON serialization
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
                json(json)
                register(ContentType.Text.Plain, KotlinxSerializationConverter(json))
            }

            // 🧠 اللوجات الخاصة بالـ network (اختياري)
            if (enableNetworkLogs) {
                install(Logging) {
                    level = LogLevel.ALL
                    logger = object : Logger {
                        override fun log(message: String) {
                            Napier.i(tag = "KtorClient", message = message)
                        }
                    }
                }.also {
                    Napier.base(DebugAntilog())
                }
            }
        }
    }
}