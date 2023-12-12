package com.example.core_network

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.appn.core_network.NetWorkManager
import com.appn.core_network.converter.ResponseTransformer
import com.appn.core_network.result.BaseResult
import com.appn.core_network.result.onFailure
import com.appn.core_network.result.onFailureThen
import com.appn.core_network.result.onSuccess
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import okhttp3.logging.HttpLoggingInterceptor

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.http.GET
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.appn.core_network.test", appContext.packageName)
        /// 测试网络框架的使用
        NetWorkManager.instance.baseUrl("https://www.wanandroid.com/")
            .registerResponseTransformer(WanAndroidResponseTransformerFactory())
            .setSuccessCode(0)
            .build(appContext)
        val service = NetWorkManager.instance.create(WanAndroidService::class.java)
        runBlocking {
            service.test()
                .onSuccess { println("execute test success ==> $it") }
                .onFailure { println("execute test() failure ==> $it") }
                // userInfo
                .onFailureThen { service.userInfo() }
                ?.onSuccess { println("execute userInfo success ==> $it") }
                ?.onFailure { println("execute userInfo() failure ==> $it") }
                // banner
                ?.onFailureThen { service.banner() }
                ?.onSuccess { println("execute banner() success ==> $it") }
                ?.onFailure { println("execute banner() failure ==> $it") }
        }
    }

    interface WanAndroidService {
        @GET("test")
        suspend fun test(): BaseResult<JsonElement>

        @GET("tree/json")
        suspend fun userInfo(): BaseResult<JsonElement>

        @GET("banner/json")
        suspend fun banner(): BaseResult<JsonElement>
    }

    class WanAndroidResponseTransformerFactory : ResponseTransformer.Factory {
        companion object {
            private val gson = Gson()
        }

        override fun create(type: Type): ResponseTransformer? {
            return (type as? ParameterizedType)?.rawType
                ?.takeIf { it == BaseResult::class.java }
                ?.let { Transformer() }
        }

        inner class WanAndroidResult<T> {
            @SerializedName("code", alternate = ["errorCode"])
            private val errorCode: Int = -1

            @SerializedName("data")
            private val data: T? = null

            @SerializedName("msg", alternate = ["errorMsg"])
            private val errorMsg: String? = null
        }

        inner class Transformer : ResponseTransformer {
            override fun transform(original: InputStream): InputStream {
                val response = gson.fromJson<WanAndroidResult<com.google.gson.JsonElement>>(
                    original.reader(),
                    object : TypeToken<WanAndroidResult<com.google.gson.JsonElement>>() {}.type
                )
                Log.d("ResponseTransformer", "transform: ${gson.toJson(response)}")
                return gson.toJson(response).byteInputStream()
            }
        }
    }
}