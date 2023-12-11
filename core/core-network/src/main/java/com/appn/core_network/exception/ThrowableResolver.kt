package com.dayunauto.lib_api.network.exception

import java.lang.reflect.Type

interface ThrowableResolver<T> {
    fun resolve(throwable: Throwable): T
    interface Factory<T> {
        fun create(type: Type): ThrowableResolver<T>?
    }
}