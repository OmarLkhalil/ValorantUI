package com.larryyu.domain.utils

object RetrofitConstants {
    const val BASE_URL = "https://valorant-api.com/v1/"
    const val TIME_OUT = 3000
}


object NetworkConstants {

    const val LANGUAGE = "Accept-Language"
    const val BEARER = "Bearer "
    const val AUTHORIZATION = "Authorization"
    const val NETWORK_TIMEOUT: Long = 600000000
    const val API_VERSION = "/api/"

    object Languages {
        const val ARABIC = "ar"
        const val ENGLISH = "en"
    }

    object RequestKeys {
        const val SUCCESS = "success"
        const val FAIL = "fail"
        const val NEED_ACTIVE = "needActive"
        const val UN_AUTH = "unauthenticated"
        const val BLOCKED = "blocked"
        const val EXCEPTION = "exception"
    }

    object NetworkParams {
        const val METHOD = "_method"
        const val PATCH = "patch"

        const val COUNTRY_CODE = "country_code"
        const val COUNTRY_CODE_VALUE = "00966"
        const val PHONE = "phone"
        const val ACCOUNT = "account"
        const val PASSWORD = "password"
        const val OLD_PASSWORD = "old_password"
        const val CONFIRM_PASSWORD = "password_confirmation"
        const val DEVICE_ID = "device_id"
        const val DEVICE_TYPE = "device_type"
        const val TYPE_ANDROID = "android"
        const val EMAIL = "email"
        const val MSG = "message"
        const val NAME = "name"
        const val AVATAR = "avatar"
        const val CODE = "code"
        const val LAT = "lat"
        const val LNG = "long"
        const val ADDRESS = "address"
        const val PAGE = "page"
    }

    object EndPoints {
        const val LOGIN = "sign-in"
        const val FORGET = "forget-password"
        const val RESET = "reset-password"
        const val RESEND_CODE = "resend-code"
        const val REGISTER = "sign-up"
    }

    object FailRequestCode {
        const val FAIL = 400
        const val UN_AUTH = 419
        const val BLOCKED = 423
        const val NOT_FOUND = 404
        const val UN_PROCESS = 422
        const val EXCEPTION = 500
        const val FORBIDDEN = 403
    }

}