package ca.allanwang.kit.props

import java.io.File
import java.util.*

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

}