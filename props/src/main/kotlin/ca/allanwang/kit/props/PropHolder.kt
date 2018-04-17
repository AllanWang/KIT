package ca.allanwang.kit.props

import java.io.File
import java.util.*
import kotlin.reflect.KProperty

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
    fun int(key: String, default: Int = 0, errorMessage: String? = null): Lazy<Int> =
            SynchronizedLazyProp({
                nonnull(get(key)?.toIntOrNull(), default, errorMessage)
            })

    /**
     * Prop delegation to return a nonnull string or default
     */
    fun string(key: String, default: String = "", errorMessage: String? = null): Lazy<String> =
            SynchronizedLazyProp({
                nonnull(get(key), default, errorMessage)
            })

    /**
     * Prop delegation to return a nullable string
     */
    fun nullableString(key: String): Lazy<String?> =
            SynchronizedLazyProp({
                get(key)
            })

}

private class StringProp {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, thank you for delegating '${property.name}' to me!"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}' in $thisRef.")
    }
}


private object UNINITIALIZED_PROP

private class SynchronizedLazyProp<out T>(initializer: () -> T, lock: Any? = null) : Lazy<T> {
    private var initializer: (() -> T)? = initializer
    @Volatile
    private var _value: Any? = UNINITIALIZED_PROP
    // final field is required to enable safe publication of constructed instance
    private val lock = lock ?: this

    override val value: T
        get() {
            val _v1 = _value
            if (_v1 !== UNINITIALIZED_PROP) {
                @Suppress("UNCHECKED_CAST")
                return _v1 as T
            }

            return synchronized(lock) {
                val _v2 = _value
                if (_v2 !== UNINITIALIZED_PROP) {
                    @Suppress("UNCHECKED_CAST") (_v2 as T)
                } else {
                    val typedValue = initializer!!()
                    _value = typedValue
                    initializer = null
                    typedValue
                }
            }
        }

    override fun isInitialized(): Boolean = _value !== UNINITIALIZED_PROP

    override fun toString(): String = if (isInitialized()) value.toString()
    else "Prop value not initialized yet."

}