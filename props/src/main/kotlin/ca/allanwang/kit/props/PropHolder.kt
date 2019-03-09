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
package ca.allanwang.kit.props

import java.io.File
import java.util.Properties

/**
 * Prop container
 */
open class PropHolder(val props: Properties) {

    constructor(vararg paths: String) : this(Properties().apply {
        val file = PropUtils.loadProps(this, *paths)
        if (file != null)
            println("Found props at ${file.absolutePath}")
        else
            println("Could not find props at ${File(".").absolutePath}")
    })

    fun get(key: String): String? =
        props.getProperty(key)

    @Suppress("NOTHING_TO_INLINE")
    private inline fun <T : Any> nonnull(result: T?, default: T, errorMessage: String?): T {
        if (result != null)
            return result
        if (errorMessage != null)
            println(errorMessage)
        return default
    }

    /**
     * Prop delegation to return an int or default
     */
    fun int(key: String, default: Int = 0, errorMessage: String? = null): Lazy<Int> = lazy {
        nonnull(get(key)?.toIntOrNull(), default, errorMessage)
    }

    /**
     * Prop delegation to return a nonnull string or default
     */
    fun string(key: String, default: String = "", errorMessage: String? = null): Lazy<String> = lazy {
        nonnull(get(key), default, errorMessage)
    }

    /**
     * Prop delegation to return a nullable string
     */
    fun nullableString(key: String): Lazy<String?> = lazy {
        get(key)
    }

    /**
     * Prop delegation to return an boolean or default
     */
    fun boolean(key: String, default: Boolean = false, errorMessage: String? = null): Lazy<Boolean> = lazy {
        nonnull(
            when (get(key)?.toLowerCase()) {
                "true" -> true
                "false" -> false
                else -> null
            }, default, errorMessage
        )
    }
}
