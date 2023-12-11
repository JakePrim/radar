package com.appn.core_network.exception

import com.appn.core_network.exception.ResponseThrowable

/**
 * @desc 自定义异常处理
 * @author sufulu
 * @time 2021/7/20 - 5:02 下午
 * @contact sufululove@gmail.com
 * @name android_framework_basic_kotlin
 * @version 1.0.0
 */

/**
 * 系统异常
 */
interface IExceptionHandler {
    /**
     * 处理异常
     * @param runnable 主要用来处理一些特殊的异常需要 回调处理
     */
    fun handleException(e: Throwable, runnable: ((Int) -> Unit)? = null): ResponseThrowable

    /**
     * 定义全局app的成功code,对于非success code的都会返回ResponseThrowable，在自定义的异常处理类中进行处理
     */
    fun appSuccessCode(): Int
}