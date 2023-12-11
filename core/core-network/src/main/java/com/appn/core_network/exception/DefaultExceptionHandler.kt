package com.appn.core_network.exception

import android.net.ParseException
import android.util.Log
import com.appn.core_network.NetWorkManager.Companion.instance
import com.appn.core_network.R
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import java.lang.IllegalStateException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * 异常处理类
 */
class DefaultExceptionHandler : IExceptionHandler {
    override fun handleException(e: Throwable, runnable: ((Int) -> Unit)?): ResponseThrowable {
        Log.i(TAG, "e$e")
        val ex: ResponseThrowable
        return when (e) {
            is HttpException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.HTTP_ERROR)
                when (e.code()) {
                    SYSTEM_ERROR.UNAUTHORIZED -> {
                        ex.setMessage(instance.mContext.getString(R.string.unauthorized))

                        ex.code = SYSTEM_ERROR.UNAUTHORIZED
                    }

                    SYSTEM_ERROR.FORBIDDEN -> ex.setMessage(instance.mContext.getString(R.string.forbidden_error))

                    SYSTEM_ERROR.NOT_FOUND -> ex.setMessage(instance.mContext.getString(R.string.res_not_found))

                    SYSTEM_ERROR.REQUEST_TIMEOUT -> ex.setMessage(instance.mContext.getString(R.string.request_timeout))

                    SYSTEM_ERROR.INTERNAL_SERVER_ERROR -> ex.setMessage(
                        instance.mContext.getString(
                            R.string.internal_server_error
                        )
                    )

                    SYSTEM_ERROR.SERVICE_UNAVAILABLE -> ex.setMessage(instance.mContext.getString(R.string.service_unavailable))

                    else -> ex.setMessage(instance.mContext.getString(R.string.net_work_error))
                }
                ex
            }

            is JsonParseException, is JsonSyntaxException, is IllegalStateException, is JSONException, is ParseException, is MalformedJsonException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.PARSE_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.parse_error))
                ex
            }

            is ConnectException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.NETWORD_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.net_work_error))
                ex
            }

            is SSLException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.SSL_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.ssl_error))
                ex
            }

            is ConnectTimeoutException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.connect_timeout_error))
                ex
            }

            is SocketTimeoutException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
                ex.setMessage(
                    instance.mContext.getString(R.string.connect_timeout_error)
                )
                ex
            }

            is UnknownHostException -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.TIMEOUT_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.unknown_host))
                ex
            }

            is ResponseThrowable -> {//业务异常
                ex = ResponseThrowable(e, e.code)
                when (ex.getCode()) {
                    APP_ERROR.UNAUTHORIZED -> runnable?.invoke(APP_ERROR.UNAUTHORIZED) //扔给外部处理
                    else -> {
                    }
                }
                ex.setMessage(e.message)
                ex
            }

            else -> {
                ex = ResponseThrowable(e, SYSTEM_ERROR.NETWORD_ERROR)
                ex.setMessage(instance.mContext.getString(R.string.net_work_error))
                ex
            }
        }
    }

    override fun appSuccessCode(): Int {
        return APP_ERROR.SUCC
    }

    interface SYSTEM_ERROR {
        companion object {
            const val UNAUTHORIZED = 401
            const val FORBIDDEN = 403
            const val NOT_FOUND = 404
            const val REQUEST_TIMEOUT = 408
            const val INTERNAL_SERVER_ERROR = 500
            const val SERVICE_UNAVAILABLE = 503

            /**
             * 未知错误
             */
            const val UNKNOWN = 1000

            /**
             * 解析错误
             */
            const val PARSE_ERROR = 1001

            /**
             * SSL_ERROR         * 网络错误
             */
            const val NETWORD_ERROR = 1002

            /**
             * 协议出错
             */
            const val HTTP_ERROR = 1003

            /**
             * 证书出错
             */
            const val SSL_ERROR = 1005

            /**
             * 连接超时
             */
            const val TIMEOUT_ERROR = 1006
        }
    }

    interface APP_ERROR {
        companion object {
            const val SUCC = 20000 //	处理成功，无错误
            const val UNAUTHORIZED = 60000 //token失效
            const val PHONEVERIFYERROR = 50000 //手机号或验证码错误
            const val SAFE_PASSWORD = 50003 //是否校验服务安全码
            const val AUTH_KEY_DEL = 50006 //钥匙授权删除
            const val LOGOUT = 50001 //退出登录
            const val DELETE = 50099 //文章或动态删除的code
            const val ONLINE = 50005 //车辆不在线的code
            const val AIR_RESERVE_TIME = 50007 //空调预约大于7
            const val T_SERVICE = 50008 //T 服务
        }
    }

    companion object {
        private val TAG = DefaultExceptionHandler::class.java.simpleName
    }


}