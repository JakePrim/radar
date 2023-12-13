package com.appn.core_common.integration

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.Method


/**
 * EventBus 的管理类，如果在某个Feature模块使用到了EventBus，自行添加依赖，
 */
class EventBusManager {
    companion object {
        private var instance: EventBusManager? = null

        fun getInstance(): EventBusManager? {
            if (instance == null) {
                synchronized(EventBusManager::class) {
                    instance = EventBusManager()
                }
            }
            return instance
        }
    }


    /**
     * 注册订阅者
     */
    fun register(subscriber: Any) {
        //存在@Subscribe注解
        if (haveAnnotation(subscriber)) {
            EventBus.getDefault().register(subscriber)
        }
    }


    /**
     * 注销订阅
     */
    fun unregister(subscriber: Any) {
        if (haveAnnotation(subscriber)) {
            EventBus.getDefault().unregister(subscriber)
        }
    }

    /**
     * 发送Event事件
     */
    fun post(event: Any) {
        EventBus.getDefault().post(event)
    }

    /**
     * 发送粘性事件
     */
    fun postSticky(event: Any) {
        EventBus.getDefault().postSticky(event)
    }

    /**
     * 注销粘性事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T {
        return EventBus.getDefault().removeStickyEvent(eventType)
    }

    /**
     * 清除订阅者和事件缓存
     */
    fun clear() {
        EventBus.clearCaches()
    }

    private fun haveAnnotation(subscriber: Any): Boolean {
        var skipSuperClasses = false
        var clazz: Class<*>? = subscriber.javaClass
        //查找类中符合注册要求的方法, 直到Object类
        while (clazz != null && !isSystemCalss(clazz.name) && !skipSuperClasses) {
            val allMethods: Array<Method> = try {
                clazz.declaredMethods
            } catch (th: Throwable) {
                try {
                    clazz.methods
                } catch (th2: Throwable) {
                    continue
                } finally {
                    skipSuperClasses = true
                }
            }
            for (method in allMethods) {
                val parameterTypes = method.parameterTypes
                //查看该方法是否含有 Subscribe 注解
                if (method.isAnnotationPresent(Subscribe::class.java) && parameterTypes.size == 1) {
                    return true
                }
            } //end for
            //获取父类, 以继续查找父类中符合要求的方法
            clazz = clazz.superclass
        }
        return false
    }

    private fun isSystemCalss(name: String): Boolean {
        return name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("android.")
    }
}