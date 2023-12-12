package com.appn.core_network.result

import com.appn.core_network.NetWorkManager
import com.appn.core_network.exception.ResponseThrowable

/**
 * 基础的通用类 需要实现该接口[APIResult]
 */
interface APIResult<T, F : APIResult.Failure> {
    interface Failure

    val isSuccess: Boolean

    val isFailure: Boolean
        get() {
            return isSuccess.not()
        }

    val data: T?

    val failure: F?

    val code: Int

    val message: String
}

inline fun <T, F, R : APIResult<T, F>> R.onSuccess(successBlock: (data: T?) -> Unit): R {
    if (isSuccess) {
        data.let(successBlock)
    }
    return this
}

inline fun <T, F, R : APIResult<T, F>> R.onFailure(
    handle: Boolean = true, //默认处理异常
    failureBlock: (failure: F) -> Unit
): R {
    if (isFailure) {
        val exceptionHandler = NetWorkManager.instance.exceptionHandler()
        //异常的处理完全交给AdapterHandler进行全局是否需要处理
        if (handle) {
            exceptionHandler?.let {
                NetWorkManager.instance.getAdapterHandler()
                    ?.handlerException(it, ResponseThrowable(code, message))
            }
        }
    }
    failure?.takeIf { isFailure }?.let(failureBlock)
    return this
}

inline fun <IT, IF, IR : APIResult<IT, IF>, OT, OF, OR : APIResult<OT, OF>> IR.onThen(
    block: (data: IT?, failure: IF?) -> OR
): OR {
    return block.invoke(data, failure)
}

inline fun <IT, IF, IR : APIResult<IT, IF>, OT, OF, OR : APIResult<OT, OF>> IR.onSuccessThen(
    successBlock: (data: IT?) -> OR
): OR? {
    if (isSuccess) {
        data.let(successBlock)
    }
    return null
}

inline fun <IT, IF, IR : APIResult<IT, IF>, OT, OF, OR : APIResult<OT, OF>> IR.onFailureThen(
    handle: Boolean = true, //默认处理异常
    failureBlock: (failure: IF) -> OR
): OR? {
    if (isFailure) {
        val exceptionHandler = NetWorkManager.instance.exceptionHandler()
        //异常的处理完全交给AdapterHandler进行全局是否需要处理
        if (handle) {
            exceptionHandler?.let {
                NetWorkManager.instance.getAdapterHandler()
                    ?.handlerException(it, ResponseThrowable(code, message))
            }
        }
    }
    return failure?.takeIf { isFailure }?.let(failureBlock)
}