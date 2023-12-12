# core-network

### 使用如下

```kotlin
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        /// 测试网络框架的使用
        NetWorkManager.instance.baseUrl("https://www.wanandroid.com/")
            //当接口返回的类型和定义的BaseResult类型不一致时 可以实现类型转换
            .registerResponseTransformer(WanAndroidResponseTransformerFactory())
            //定义业务成功的code码
            .setSuccessCode(0)
            //定义异常的提示
            .addExceptionHandler()
            //host 校验
            .hostnameVerifier()
            //定义异常处理
            .setAdapterHandler()
            //ssl 证书设置
            .sslSocketFactory()
            //添加拦截器
            .addInterceptor()
            //开始初始化
            .build(appContext)

        //获取service
        val service = NetWorkManager.instance.create(WanAndroidService::class.java)

        //链式回调
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

    //定义类型转换器
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
```