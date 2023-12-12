package com.example.core_network

import androidx.test.core.app.ApplicationProvider
import androidx.test.runner.AndroidJUnit4
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
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import retrofit2.http.GET
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {


}