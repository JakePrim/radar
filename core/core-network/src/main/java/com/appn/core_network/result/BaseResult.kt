package com.appn.core_network.result

import com.appn.core_network.NetWorkManager
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class BaseResult<T>(
    override val code: Int = -1,
    val desc: String = "",
    val msg: String = "",
    override val data: T?,
    @Contextual
    val throwable: Throwable? = null
) : APIResult<T, BaseResult.Failure> {
    data class Failure(val code: Int, val message: String?, val throwable: Throwable?) :
        APIResult.Failure

    override val message: String
        get() = msg
    override val isSuccess: Boolean
        get() = code == NetWorkManager.instance.getSuccessCode()

    override val failure: Failure?
        get() = if (isFailure) Failure(code, msg, throwable) else null
}