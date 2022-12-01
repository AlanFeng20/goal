package com.alanfeng.goal

import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LazyProperty<T : Any>(private val propertyFactory: () -> ReadWriteProperty<Any?, T>) :
    ReadWriteProperty<Any?, T> {
    private var value: ReadWriteProperty<Any?, T>? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (this.value == null) value = propertyFactory()
        return this.value!!.getValue(thisRef, property)
    }

    override fun setValue(thisRef:Any?, property: KProperty<*>, value: T) {
        if (this.value == null) this.value = propertyFactory()
        this.value!!.setValue(thisRef, property, value)
    }
}