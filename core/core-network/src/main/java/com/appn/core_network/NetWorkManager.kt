package com.appn.core_network

import android.content.Context
import com.appn.core_network.adapter.IAdapterHandler
import com.appn.core_network.converter.NullOnEmptyConverterFactory
import com.appn.core_network.converter.ResponseTransformer
import com.appn.core_network.converter.TransformConverterFactory
import com.appn.core_network.exception.DefaultExceptionHandler
import com.appn.core_network.exception.FactoryRegistry
import com.appn.core_network.exception.IExceptionHandler
import com.appn.core_network.result.DefaultThrowableResolverFactory
import com.appn.core_network.ssl.SSLContextUtil
import com.dayunauto.lib_api.network.exception.ThrowableResolver
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Proxy
import java.lang.reflect.Type
import java.lang.reflect.WildcardType
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.intrinsics.intercepted

class NetWorkManager : INetWork {
    lateinit var retrofit: Retrofit
    lateinit var mContext: Context
    private lateinit var baseUrl: String
    private var exceptionHandlers: IExceptionHandler? = null
    private lateinit var adapterHandler: IAdapterHandler
    private lateinit var okHttpClient: OkHttpClient
    private var okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()

    companion object {
        private const val DEFAULT_CONNECT_TIMEOUT = 30
        private const val DEFAULT_WRITE_TIMEOUT = 30
        private const val DEFAULT_READ_TIMEOUT = 30

        //懒单例模式
        val instance: NetWorkManager by lazy {
            NetWorkManager()
        }
    }

    init {
        okHttpBuilder.connectTimeout(DEFAULT_CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(DEFAULT_WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.writeTimeout(DEFAULT_READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
        //错误重连开启
        okHttpBuilder.retryOnConnectionFailure(true)
        //默认不拦截所有的hostname
        okHttpBuilder.hostnameVerifier(SSLContextUtil.HOSTNAME_VERIFIER)
        //默认开启支持 https
        okHttpBuilder.sslSocketFactory(
            SSLContextUtil.createSSLSocketFactory(),
            SSLContextUtil.trustManagers
        )
    }

    override fun addInterceptor(interceptor: Interceptor): INetWork {
        okHttpBuilder.addInterceptor(interceptor)
        return this
    }

    override fun baseUrl(baseUrl: String): INetWork {
        this.baseUrl = baseUrl
        return this
    }

    override fun addExceptionHandler(exceptionHandler: IExceptionHandler): INetWork {
        this.exceptionHandlers = exceptionHandler
        return this
    }

    override fun setAdapterHandler(adapterHandler: IAdapterHandler): INetWork {
        this.adapterHandler = adapterHandler
        return this
    }

    override fun hostnameVerifier(hostnameVerifier: HostnameVerifier): INetWork {
        return this
    }

    override fun sslSocketFactory(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): INetWork {
        okHttpBuilder.sslSocketFactory(sslSocketFactory, trustManager)
        return this
    }

    override fun exceptionHandler(): IExceptionHandler? {
        return exceptionHandlers
    }

    override fun getAdapterHandler(): IAdapterHandler {
        return adapterHandler
    }

    override fun registerResponseTransformer(converter: ResponseTransformer.Factory): INetWork {
        FactoryRegistry.registerResponseTransformer(converter)
        return this
    }

    override fun registerThrowableResolver(factory: ThrowableResolver.Factory<*>): INetWork {
        FactoryRegistry.registerThrowableResolver(factory)
        return this
    }


    @OptIn(ExperimentalSerializationApi::class)
    override fun build(context: Context) {
        this.mContext = context
        val contentType = "application/json".toMediaType()
        //Json 解析器配置
        val jsonDecoder = Json {
            ignoreUnknownKeys = true
            //当数据类字段为可空, 则赋值为null. 要求explicitNulls = false
            explicitNulls = false
        }
        if (exceptionHandlers == null) {
            this.exceptionHandlers = DefaultExceptionHandler()
        }

        this.okHttpClient = okHttpBuilder.build()
        FactoryRegistry.registerThrowableResolver(DefaultThrowableResolverFactory())
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(NullOnEmptyConverterFactory())
            .addConverterFactory(TransformConverterFactory())
            .addConverterFactory(jsonDecoder.asConverterFactory(contentType))
            .baseUrl(baseUrl)
            .build()
    }

    /**
     * 获取对应的Service
     *
     * @param service Service 的 class
     * @param <T>
     * @return <T>
     **/
    inline fun <reified T> create(service: Class<T>): T {
        return retrofit.create(service).proxyRetrofit()
    }

    /** *
     *  `proxyRetrofit` 方法主要作用是重新对接口进行动态代理，这样就可以在 * `InvocationHandler#invoke`
     *  中对异常进行拦截，这样调用方就不用显示地调用 * `try catch` 了 统一处理协程的异常
     */
    inline fun <reified T> T.proxyRetrofit(): T {
        // 获取原先的 retrofit 的代理对象的的 InvocationHandler
        // 这样我就可以继续使用 retrofit 的能力进行网络请求
        val retrofitHandler = Proxy.getInvocationHandler(this)
        return Proxy.newProxyInstance(
            T::class.java.classLoader, arrayOf(T::class.java)
        ) { proxy, method, args ->
            // 判断当前是为 suspend 方法
            method.takeIf { it.isSuspendMethod }?.getSuspendReturnType()
                ?.let { FactoryRegistry.getThrowableResolver(it) }
                ?.let { resolver ->
                    // 替换原始的 Contiuation 对象，这样我们就可以对异常进行拦截
                    args.updateAt(
                        args.lastIndex,
                        FakeSuccessContinuationWrapper(
                            //intercepted保证协程上下文的调度一致，否则会新启一个线程
                            (args.last() as Continuation<Any>).intercepted(),
                            resolver as ThrowableResolver<Any>
                        )
                    )
                }
            retrofitHandler.invoke(proxy, method, args)
        } as T
    }

    class FakeSuccessContinuationWrapper<T>(
        private val original: Continuation<T>,
        private val resolver: ThrowableResolver<T>
    ) :
        Continuation<T> {
        override val context: CoroutineContext = original.context

        override fun resumeWith(result: Result<T>) {
            result.onSuccess {
                // when it's success, resume with original Continuation
                original.resumeWith(result)
            }.onFailure {
                // when it's failure, resume a wrapper success which contain
                // failure, so we don't need to add try catch
                //将错误伪装成，成功的状态
                val fakeSuccessResult = resolver.resolve(it)
                original.resumeWith(Result.success(fakeSuccessResult))
            }
        }

    }

    /**
     * A property to indicate where the method is a suspend method or not.
     */
    val Method.isSuspendMethod: Boolean
        get() = genericParameterTypes.lastOrNull()
            ?.let { it as? ParameterizedType }?.rawType == Continuation::class.java

    /**
     * Get a suspend method return type, if the method is not a suspend method return null.
     *
     * @return return type of the suspend method.
     */
    fun Method.getSuspendReturnType(): Type? {
        return genericParameterTypes.lastOrNull()
            ?.let { it as? ParameterizedType }?.actualTypeArguments?.firstOrNull()
            ?.let { it as? WildcardType }?.lowerBounds?.firstOrNull()
    }

    /**
     * Update Array at a special index.
     *
     * @param index the index to update
     * @param updated the updated value
     */
    fun Array<Any?>.updateAt(index: Int, updated: Any?) {
        this[index] = updated
    }
}