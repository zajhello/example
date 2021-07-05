package com.dxmovie.dxbase.net.annotation

import kotlin.reflect.KClass

/**
 * Api Service网络配置注解声明
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class STApi(
        //api url地址
        val url:String = "",
        //网络配置器class
        val providerClass:KClass<*>
)