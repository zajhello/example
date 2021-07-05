package com.dxmovie.dxbase.utils.extention

sealed class Ext<out T> constructor(val boolean: Boolean)

/** 如果该对象不是null，则执行action */
fun <T, E> T?.ifNonNull(action: T.() -> E): Ext<E> {
    if (this != null) {
        return WithData(action())
    }
    return Otherwise
}

/** 如果该对象是null，则执行action */
fun <T, E> T?.ifNull(action: () -> E) {
    if (this == null) {
        action()
    }
}

inline fun <T> Boolean.yes(block: () -> T): Ext<T> = when {
    this -> {
        WithData(block())
    }
    else -> Otherwise
}

inline fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

object Otherwise : Ext<Nothing>(true)

class WithData<out T>(val data: T) : Ext<T>(false)

/**
 * 除此以外
 */
inline infix fun <T> Ext<T>.otherwise(block: () -> T): T {
    return when (this) {
        is Otherwise -> block()
        is WithData<T> -> this.data
    }
}

inline operator fun <T> Boolean.invoke(block: () -> T) = yes(block)


fun except(a:Float, b:Float):Float{
    if (b <= 0f){
        return if (a <= 0f){
            0.5f
        }else{
            1f
        }
    }
    return a/b
}

