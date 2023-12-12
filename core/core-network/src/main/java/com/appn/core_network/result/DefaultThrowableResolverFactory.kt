package com.appn.core_network.result

import android.util.Log
import com.dayunauto.lib_api.network.exception.ThrowableResolver
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 默认的异常处理工厂，可以自定义实现[NetWorkManager.instance.registerThrowableResolver]
 */
class DefaultThrowableResolverFactory : ThrowableResolver.Factory<BaseResult<*>> {
    override fun create(type: Type): ThrowableResolver<BaseResult<*>>? {
        return (type as? ParameterizedType)?.rawType
            ?.takeIf { it == BaseResult::class.java }
            ?.let { Resolver() }
    }

    inner class Resolver : ThrowableResolver<BaseResult<*>> {
        override fun resolve(throwable: Throwable): BaseResult<*> {
            Log.d("TAG", "resolve: ${throwable.message}")
            return BaseResult<Any>(
                -1,
                "网络异常",
                "网络异常",
                null,
                throwable
            )
        }
    }
}