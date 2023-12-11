package com.appn.core_network

import android.content.Context
import com.appn.core_network.adapter.IAdapterHandler
import com.appn.core_network.converter.ResponseTransformer
import com.appn.core_network.converter.TransformConverterFactory
import com.appn.core_network.exception.IExceptionHandler
import com.dayunauto.lib_api.network.exception.ThrowableResolver
import okhttp3.Interceptor
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

interface INetWork {
    /**
     * 添加拦截器
     * @param interceptor okhttp的拦截器接口
     */
    fun addInterceptor(interceptor: Interceptor): INetWork

    /**
     * Base Url
     */
    fun baseUrl(baseUrl: String): INetWork

    /**
     * 添加自定义的异常
     */
    fun addExceptionHandler(exceptionHandler: IExceptionHandler): INetWork

    /**
     * 设置处理全局异常的处理器
     */
    fun setAdapterHandler(adapterHandler: IAdapterHandler): INetWork

    /**
     * hostname 拦截设置
     */
    fun hostnameVerifier(hostnameVerifier: HostnameVerifier): INetWork

    /**
     * SSL配置
     */
    fun sslSocketFactory(
        sslSocketFactory: SSLSocketFactory,
        trustManager: X509TrustManager
    ): INetWork

    fun exceptionHandler(): IExceptionHandler?

    fun getAdapterHandler(): IAdapterHandler?

    /**
     * 定义返回的数据类型，通用的数据类
     */
    fun registerResponseTransformer(converter: ResponseTransformer.Factory): INetWork

    /**
     * 处理协程异常和[registerResponseTransformer] 一同使用
     */
    fun registerThrowableResolver(factory: ThrowableResolver.Factory<*>): INetWork

    /**
     * 构建网络请求-在application中设置
     */
    fun build(context: Context)
}