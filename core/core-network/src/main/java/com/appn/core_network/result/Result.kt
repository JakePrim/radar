package com.appn.core_network.result

import com.appn.core_network.exception.DefaultExceptionHandler
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Result<T>(
    override val code: Int = -1,
    val desc: String = "",
    val msg: String = "",
    override val data: T?,
    @Contextual
    val throwable: Throwable? = null
) : APIResult<T, Result.Failure> {
    data class Failure(val code: Int, val message: String?, val throwable: Throwable?) :
        APIResult.Failure

    override val message: String
        get() = msg
    override val isSuccess: Boolean
        get() = code == DefaultExceptionHandler.APP_ERROR.SUCC

    override val failure: Failure?
        get() = if (isFailure) Failure(code, msg, throwable) else null
}