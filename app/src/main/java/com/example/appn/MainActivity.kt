package com.example.appn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromStream
import retrofit2.http.GET
import java.io.InputStream
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}