/*
 * Copyright 2019 Allan Wang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.allanwang.kit.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Helper to reveal an [apply] method for interceptors.
 * Each chain will repackage the request and proceed
 */
abstract class BaseInterceptor : Interceptor {

    abstract fun apply(request: Request.Builder, originalChain: Interceptor.Chain)

    override fun intercept(chain: Interceptor.Chain): Response? {
        val origRequest = chain.request()
        val request = origRequest.newBuilder()
        apply(request, chain)
        return chain.proceed(request.build())
    }
}

private const val CONTENT_TYPE = "Content-Type"
private const val APPLICATION_JSON = "application/json;charset=UTF-8"

/**
 * Injects the token to each request
 * [token] will be called with each request, and will be added as an authorization token if it is not blank
 */
class TokenInterceptor(private val token: () -> String) : BaseInterceptor() {

    override fun apply(request: Request.Builder, originalChain: Interceptor.Chain) {
        val token = token()
        if (token.isNotBlank())
            request.addHeader("Authorization", "Token $token")
    }
}

class CookieInterceptor(private val cookie: () -> String) : BaseInterceptor() {

    override fun apply(request: Request.Builder, originalChain: Interceptor.Chain) {
        val cookie = cookie()
        if (cookie.isNotBlank())
            request.addHeader("Cookie", cookie)
    }
}
