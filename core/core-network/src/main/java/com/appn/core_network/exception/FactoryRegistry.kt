package com.appn.core_network.exception

import com.appn.core_network.converter.ResponseTransformer
import com.dayunauto.lib_api.network.exception.ThrowableResolver
import java.lang.reflect.Type

object FactoryRegistry {
    private val throwableResolvers = mutableListOf<ThrowableResolver.Factory<*>>()

    private val responseTransformers = mutableListOf<ResponseTransformer.Factory>()

    @JvmStatic
    fun registerResponseTransformer(factory: ResponseTransformer.Factory) {
        responseTransformers.add(factory)
    }

    @JvmStatic
    fun getResponseTransformer(rawType: Type): ResponseTransformer? {
        responseTransformers.forEach {
            val transformer = it.create(rawType)
            if (transformer != null) {
                return transformer
            }
        }
        return null
    }

    @JvmStatic
    fun registerThrowableResolver(factory: ThrowableResolver.Factory<*>) {
        throwableResolvers.add(factory)
    }

    @JvmStatic
    fun getThrowableResolver(type: Type): ThrowableResolver<*>? {
        throwableResolvers.forEach {
            val resolver = it.create(type)
            if (resolver != null) {
                return resolver
            }
        }
        return null
    }
}