package com.appn.core_network.converter

import java.io.InputStream
import java.lang.reflect.Type

/**
 * 例如接口返回的结果：errorCode data errorMsg -> 转换成我们的Result 想要的类型，就可以使用该类实现
 * class WanAndroidResponseTransformerFactory : ResponseTransformer.Factory {
 *     companion object {
 *         private val gson = Gson()
 *     }
 *
 *     override fun create(type: Type): ResponseTransformer? {
 *         return (type as? ParameterizedType)?.rawType
 *             ?.takeIf { it == BaseAPIResult::class.java }
 *             ?.let { Transformer() }
 *     }
 *
 *     inner class WanAndroidResult<T> {
 *         @SerializedName("code", alternate = ["errorCode"])
 *         private val errorCode: Int = -1
 *
 *         @SerializedName("data")
 *         private val data: T? = null
 *
 *         @SerializedName("message", alternate = ["errorMsg"])
 *         private val errorMsg: String? = null
 *     }
 *
 *     inner class Transformer : ResponseTransformer {
 *         override fun transform(original: InputStream): InputStream {
 *             val response = gson.fromJson<WanAndroidResult<JsonElement>>(
 *                 original.reader(), object : TypeToken<WanAndroidResult<JsonElement>>() {}.type
 *             )
 *             return gson.toJson(response).byteInputStream()
 *         }
 *     }
 * }
 */
interface ResponseTransformer {
    fun transform(original: InputStream): InputStream

    interface Factory {
        /**
         * Create a [ResponseTransformer].
         *
         * @param type the return type of method
         *
         * @return a instance of [ResponseTransformer]
         */
        fun create(type: Type): ResponseTransformer?
    }
}