package com.ctrip.ibu.myctrip.util.livedata

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 防止 LiveData postValue 丢数据.
 *
 * 进行严格的 null 和 not null 处理后，可以实现 [ReadWriteProperty] 便于 kotlin 属性委托
 */
@Suppress("unused")
open class SafelyMutableLiveData<T>(value: T) : SafelyLiveData<T>(value),
    ReadWriteProperty<Any?, T> {

    public override fun setValue(value: T) {
        super.setValue(value)
    }

    public override fun postValue(value: T) {
        super.postValue(value)
    }

    /**
     * implements [ReadWriteProperty]
     */
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

}
