package com.appn.core_network.converter

import com.appn.core_network.exception.FactoryRegistry
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * A [Converter.Factory] to transform different data struct to one.
 *
 * Please put [TransformConverterFactory] at the first place in
 * [Retrofit] [Converter.Factory] lists
 *
 *  *  Retrofit.Builder()
 *  *      .baseUrl("https://example.com/")
 *  *      // put at the first !!!
 *  *      .addConverterFactory(TransformConverterFactory())
 */
class TransformConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        return FactoryRegistry.getResponseTransformer(type)?.let { transformer ->
            TransformConverter(
                retrofit.nextResponseBodyConverter(this, type, annotations),
                transformer
            )
        }
    }

    inner class TransformConverter(
        private val baseConverter: Converter<ResponseBody, Any>,
        private val responseTransformer: ResponseTransformer
    ) : Converter<ResponseBody, Any> {
        override fun convert(value: ResponseBody): Any? {
            val transformed = ResponseBody.create(
                value.contentType(),
                responseTransformer.transform(value.byteStream()).readBytes()
            )
            return baseConverter.convert(transformed)
        }
    }
}