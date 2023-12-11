package com.appn.core_network.adapter

import com.appn.core_network.exception.IExceptionHandler

/**
 * @desc 适配处理器，用来处理适配异常处理器
 * @author sufulu
 * @time 2021/7/20 - 6:00 下午
 * @contact sufululove@gmail.com
 * @name android_framework_basic_kotlin
 * @version 1.0.0
 */
interface IAdapterHandler {
    fun handlerException(exception: IExceptionHandler, t: Throwable)
}