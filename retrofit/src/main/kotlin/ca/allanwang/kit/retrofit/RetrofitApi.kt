package ca.allanwang.kit.retrofit

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Generates a retrofit interface around [T].
 * See [RetrofitApiConfig] for all possible configurations
 */
inline fun <reified T> createRetrofitApi(url: String, configBuilder: RetrofitApiConfig.() -> Unit): T {
    val config = RetrofitApiConfig()
    config.configBuilder()
    val client = OkHttpClient.Builder()
    config.maxCache?.apply {
        client.addNetworkInterceptor {
            it.proceed(it.request()).newBuilder()
                    .header(
                            "Cache-Control",
                            String.format("max-age=%d, only-if-cached, max-stale=%d", first, second)
                    )
                    .build()
        }
    }
    config.tokenRetriever?.apply {
        client.addInterceptor(TokenInterceptor(this))
    }

    config.clientBuilder(client)

    val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())

    config.moshiBuilder(moshi)

    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create(moshi.build()))
            .client(client.build())

    config.retrofitBuilder(retrofit)

    return retrofit.build().create(T::class.java)
}

class RetrofitApiConfig {
    /**
     * Supply to automatically add an authorization token header whenever the token is not blank
     */
    var tokenRetriever: (() -> String)? = null
    /**
     * Pair of cache values denoting age and stale.
     * If supplied, they will be added as a cache-control header
     */
    var maxCache: Pair<Int, Int>? = null
    /**
     * Additional builder that will be applied after all other client configs.
     * Note that the [Helper] supplies some builders if you wish to use them.
     */
    var clientBuilder: (builder: OkHttpClient.Builder) -> Unit = {}
    /**
     * Additional builder that will be applied after all other moshi configs
     */
    var moshiBuilder: (builder: Moshi.Builder) -> Unit = {}
    /**
     * Additional builder that will be applied after all other retrofit configs
     */
    var retrofitBuilder: (builder: Retrofit.Builder) -> Unit = {}

    companion object Helper {
        fun loggingInterceptor(level: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BASIC): (builder: OkHttpClient.Builder) -> Unit = {
            it.addInterceptor(HttpLoggingInterceptor().setLevel(level))
        }
    }
}
