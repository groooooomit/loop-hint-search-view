package com.ctrip.ibu.myctrip.util.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * 安全版本的 LiveData，具体如下：
 *
 * 1、防止 LiveData postValue 丢数据.
 * 2、范型进行严格的 null 或 not null 区分
 *
 * 另外进行严格的 null 和 not null 处理后，可以实现 [ReadOnlyProperty] 便于 kotlin 属性委托
 */
@Suppress("unused")
open class SafelyLiveData<T>(value: T) : LiveData<T>(value), ReadOnlyProperty<Any?, T> {

    /**
     * 使用 [uiHandler] 防止 [LiveData.postValue] 跳过中间数据
     */
    override fun setValue(value: T) {
        runOnUi {
            super.setValue(value)
        }
    }

    override fun postValue(value: T) {
        /* 统一交给 setValue. */
        setValue(value)
    }

    /**
     * 因为 Java 版本的 [LiveData.getValue] 被标记为 [androidx.annotation.Nullable], 为了 kotlin 范型 null
     * 和 not null 根据 [T] 来进行控制，此处强制转换为 [T]，与此同时去掉 [LiveData] 的无参构造方法
     */
    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T = super.getValue() as T

    /**
     * implements [ReadOnlyProperty]
     */
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T = value

    ///////////////////////////////////////////////////////////////////////////
    // run on ui tool kit
    ///////////////////////////////////////////////////////////////////////////

    private val uiHandler = Handler(Looper.getMainLooper())

    private val isMainThread: Boolean
        get() = Looper.getMainLooper() == Looper.myLooper()

    private fun runOnUi(action: () -> Unit) {
        if (isMainThread) {
            action()
        } else {
            uiHandler.post(action)
        }
    }

}
