package com.appn.core_network.result

import com.dayunauto.lib_api.network.exception.ThrowableResolver
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 默认的异常处理工厂，可以自定义实现[NetWorkManager.instance.registerThrowableResolver]
 */
class DefaultThrowableResolverFactory : ThrowableResolver.Factory<Result<*>> {
    override fun create(type: Type): ThrowableResolver<Result<*>>? {
        return (type as? ParameterizedType)?.rawType
            ?.takeIf { it == Result::class.java }
            ?.let { Resolver() }
    }

    inner class Resolver : ThrowableResolver<Result<*>> {
        override fun resolve(throwable: Throwable): Result<*> {
            return Result<Any>(
                -1,
                "网络异常",
                "网络异常",
                null,
                throwable
            )
        }
    }
}