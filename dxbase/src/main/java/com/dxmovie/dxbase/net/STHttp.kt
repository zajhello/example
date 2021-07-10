package com.dxmovie.dxbase.net

import androidx.collection.ArrayMap
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.dxmovie.dxbase.net.annotation.STApi
import com.dxmovie.dxbase.net.factory.LiveDataCallAdapterFactory
import com.dxmovie.dxbase.net.handle.LogHandle
import com.dxmovie.dxbase.net.interceptor.HttpLogInterceptor
import com.dxmovie.dxbase.net.provider.DefaultHttpProvider
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger


/**
 * 网络请求管理类
 * 使用说明：
 * 基础库提供了一份默认的网络配置 {@link DefaultHttpProvider}, 如果不为指定的url设置网络配置那么将使用默认的DefaultHttpProvider
 * 创建api service有三种方式：
 *      1.通过指定的url创建，前提是为该url设置了网络配置器provider，如果没有设置provider将使用默认的DefaultHttpProvider;
 *      2.无url创建，此种方式将使用默认的base url以及默认的DefaultHttpProvider创建api service，前提是必须设置默认的base url
 *      3.带url变化监听的生成方式，若provider对应的base url发生变化，调用#switchUrl完成切换并会将新的api service通知给外部观察者
 */
object STHttp {

    const val CONNECT_TIME_OUT = 10L

    /**
     * retrofit集合
     */
    private val retrofits:ArrayMap<String, Retrofit> = ArrayMap()

    /**
     * OkHttpClient集合
     */
    private val clients:ArrayMap<String, OkHttpClient> = ArrayMap()

    /**
     * 网络配置集合
     */
    private val providers:ArrayMap<String, IHttpProvider> = ArrayMap()

    /**
     * 监听base url变化的观察者集合
     * key: url
     * value:url对应的观察者列表
     */
    private val apiObservers:ArrayMap<String, MutableList<ApiObserver<*>>> = ArrayMap()

    /**
     * 默认网络配置器
     */
    var defaultProvider: IHttpProvider = DefaultHttpProvider()
    private set

    /**
     * 默认的base url
     */
    var defaultBaseUrl:String? = null
    private set

    /**
     * 连接超时
     */
    private val connectTimeOut = CONNECT_TIME_OUT

    /**
     * 读写超时
     */
    private val readTimeOut = CONNECT_TIME_OUT

    private val writeTimeOut = CONNECT_TIME_OUT

    private val gson: Gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()

    /**
     * 默认线程池, 连接池
     */
    private val dispatcher:Dispatcher = Dispatcher()

    private val connectionPool:ConnectionPool = ConnectionPool()

    /**
     * 线程池引用计数
     */
    private val dispatcherReferenceCount = AtomicInteger(0)
    /**
     * 连接池引用计数
     */
    private val connectionReferenceCount = AtomicInteger(0)

    @JvmStatic
    fun setDefaultProvider(provider: IHttpProvider) {
        this.defaultProvider = provider
    }

    /**
     * 设置指定url对应的http配置器
     */
    @JvmStatic
    fun setProvider(baseUrl: String, provider: IHttpProvider) {
        providers[dropLastUrl(baseUrl)] = provider
    }

    @JvmStatic
    fun setBaseUrl(baseUrl: String) {
        this.defaultBaseUrl = dropLastUrl(baseUrl)
    }

    /**
     * 查询某个url是否配置了网络设置
     */
    @JvmStatic
    fun hasHttpProvider(url:String): Boolean {
        return providers[dropLastUrl(url)] != null
    }

    @JvmStatic
    fun getClient(url:String): OkHttpClient? {
        return clients[dropLastUrl(url)]
    }

    @JvmStatic
    fun getRetrofit(url:String): Retrofit? {
        return retrofits[dropLastUrl(url)]
    }

    private fun dropLastUrl(url:String): String {
        return url.dropLastWhile { it == '/' }
    }

    @JvmOverloads
    private fun createRetrofit(baseUrl:String, provider:IHttpProvider? = null, client: OkHttpClient? = null): Retrofit {
        if(baseUrl.isNullOrEmpty()) {
            throw IllegalStateException("base url must not be null")
        }
        var retrofit = retrofits[baseUrl]
        if(retrofit != null) {
            return retrofit
        }
        var provider = provider
        if(provider == null) {
            //设置已有的
            provider = providers[baseUrl]
        }
        if(provider == null) {
            //设置默认的
            provider = defaultProvider
        }
        if(provider == null) throw IllegalStateException("provider must not be null")
        //构建retrofit
        retrofit = buildRetrofit(baseUrl, provider, client)
        retrofits[baseUrl] = retrofit
        providers[baseUrl] = provider

        return retrofit!!
    }

    /**
     * 构建retrofit
     */
    @JvmOverloads
    fun buildRetrofit(baseUrl: String, provider: IHttpProvider, client: OkHttpClient? = null): Retrofit {
        val builder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client ?: createClient(baseUrl, provider!!))
        if (!provider.callAdapterFactory().isNullOrEmpty()) {
            provider.callAdapterFactory().forEach {
                builder.addCallAdapterFactory(it)
            }
        } else {
            //添加rxjava的adapter factory
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //添加live data的adapter factory
            builder.addCallAdapterFactory(LiveDataCallAdapterFactory())
        }
        //添加转换器
        builder.addConverterFactory(if (provider.converterFactory() == null) GsonConverterFactory.create(gson) else provider.converterFactory())

        return builder.build()
    }

    /**
     * 创建okhttp client
     */
    private fun createClient(baseUrl: String, provider: IHttpProvider): OkHttpClient {
        if (baseUrl.isNullOrEmpty()) {
            throw IllegalStateException("base url must not be null")
        }
        var client = clients[baseUrl]
        if (client != null) {
            return client
        }

        client = buildClient(provider)
        clients[baseUrl] = client
        return client!!
    }

    private fun buildClient(provider: IHttpProvider): OkHttpClient {
        if (provider == null) throw IllegalStateException("provider must not be null")

        val builder = OkHttpClient.Builder()
        val sslParams = SslSocketFactory.getSslSocketFactory()
        builder.sslSocketFactory(sslParams!!.sSLSocketFactory, sslParams!!.trustManager)
        builder.hostnameVerifier(SslSocketFactory.UnSafeHostnameVerifier)
        builder.connectTimeout(if (provider.connectTimeOut != 0L)
            provider.connectTimeOut
        else
            connectTimeOut, TimeUnit.SECONDS)

        builder.readTimeout(if (provider.readTimeOut != 0L)
            provider.readTimeOut
        else
            readTimeOut, TimeUnit.SECONDS)

        builder.writeTimeout(if (provider.writeTimeOut != 0L)
            provider.writeTimeOut
        else
            writeTimeOut, TimeUnit.SECONDS)

        //设置线程池
        if (provider.dispatcher() == null) {
            builder.dispatcher(dispatcher)
            dispatcherReferenceCount.incrementAndGet()
        } else {
            builder.dispatcher(provider.dispatcher())
        }

        //设置连接池
        if (provider.connectionPool() == null) {
            builder.connectionPool(connectionPool)
            connectionReferenceCount.incrementAndGet()
        } else {
            builder.connectionPool(provider.connectionPool())
        }

        provider.httpBuilder(builder)

        //设置拦截器
        provider.interceptors().forEach {
            builder.addInterceptor(it)
        }
        //设置网络拦截器
        provider.networkInterceptors().forEach {
            builder.addNetworkInterceptor(it)
        }
        //插入自定义请求操作
        val operator = provider.requestOperator()
        operator?.let {
            builder.addInterceptor(HttpInterceptor(it))
        }

        //设置日志拦截器
        if (provider.enableLog()) {
            builder.addInterceptor(LogHandle())
            builder.addInterceptor(HttpLogInterceptor())
        }

        return builder.build()
    }

    /**
     * 使用默认base url生成api service
     */
    @JvmStatic
    operator fun <T> get(service: Class<T>): T {
        var url = getUrlByAnnotation(service)
        if(url.isNullOrEmpty()) {
            if (defaultBaseUrl.isNullOrEmpty()) throw IllegalStateException("请先设置默认的base url")
            url = defaultBaseUrl
        }
        return get(url!!, service)
    }

    @JvmStatic
    @JvmOverloads
    operator fun <T> get(service: Class<T>, observer: Observer<T>):T {
        var url = getUrlByAnnotation(service)
        if(url.isNullOrEmpty()) {
            if (defaultBaseUrl.isNullOrEmpty()) throw IllegalStateException("请先设置默认的base url")
            url = defaultBaseUrl
        }
        return get(url!!, service, observer)
    }

    /**
     * 使用指定base url生成api service
     */
    @JvmStatic
    @JvmOverloads
    operator fun <T> get(baseUrl: String, service: Class<T>, observer: Observer<T>? = null):T {
        val url = dropLastUrl(baseUrl)
        var provider = providers[url]
        if(provider == null) {
            //读取注解配置
            provider = getProviderByAnnotation(service)
            providers[url] = provider
        }
        val t = createRetrofit(url).create(service)
        if(observer != null) {
            var observers = apiObservers[url]
            if(observers == null) {
                observers = mutableListOf()
                apiObservers[url] = observers
            }
            if(!observers.contains(observer as Any)) {
                observers.add(ApiObserver(service, observer))
            }
        }
        return t
    }

    /**
     * 构建新的retrofit和okhttp client
     * 注意：
     * 1.此方法将不会对retrofit,okhttpclient缓存
     * 2.provider建议覆写dispatcher和connectionPool独立管理自己的线程池和连接池
     * 3.没有特殊场景不建议使用，使用之后配合[closeDispatcherAndConnection]手动释放线程池和连接池
     *
     * @param clientCallback 返回给外部使用的okhttpclient
     *
     */
    @JvmOverloads
    @JvmStatic
    fun <T> newRequest(baseUrl: String, provider:IHttpProvider?, service: Class<T>, clientCallback:((client:OkHttpClient)-> Unit)? = null): T {
        var p = provider
        val url = dropLastUrl(baseUrl)
        if(p == null) {
            p = providers[url]
        }
        val client = buildClient(p!!)
        val retrofit = buildRetrofit(url, p, client)
        clientCallback?.invoke(client)
        return retrofit.create(service)
    }

    /**
     * 通过注解获取网络配置
     */
    private fun <T> getProviderByAnnotation(service: Class<T>): IHttpProvider? {
        val apiAnnotation = service.getAnnotation(STApi::class.java)
        if (apiAnnotation != null) {
            val instance = apiAnnotation.providerClass.java.newInstance()
            if (instance is IHttpProvider) {
                return instance
            }
        }
        return null
    }

    /**
     * 通过注解获取api地址配置
     */
    private fun getUrlByAnnotation(service: Class<*>): String? {
        val annotation = service.getAnnotation(STApi::class.java)
        return annotation?.url
    }

    /**
     * 切换base url
     */
    @JvmStatic
    fun switchUrl(oldUrl:String, newUrl:String) {
        val oUrl = dropLastUrl(oldUrl)
        val nUrl = dropLastUrl(newUrl)
        if(oUrl == defaultBaseUrl) {
            //更新默认base url
            defaultBaseUrl = nUrl
        }
        if(providers[oUrl] == null) {
            //没有配置器
            return
        }
        //将旧的api域名provider更新
        val provider = providers[oUrl]
        providers[nUrl] = provider
        providers.remove(oUrl)
        var retrofit: Retrofit? = retrofits[oUrl] ?: //retrofit没有被创建直接返回
                return

        var newRetrofit:Retrofit = retrofit!!.newBuilder().baseUrl(nUrl).build()
        retrofits[nUrl] = newRetrofit
        //删除旧retrofit
        retrofits.remove(oUrl)

        var client = clients[oUrl]
        if(client == null) {
            createClient(nUrl, provider!!)
        }else {
            clients[nUrl] = client.newBuilder().build()
        }
        //删除原有的
        clients.remove(oUrl)
        if(apiObservers[oUrl] == null) {
            //没有service监听
            return
        }
        val observers = apiObservers[oUrl]
        observers!!.forEach {
            //生成新的api service
            val obj = newRetrofit!!.create(it.service)
            it.onChanged(obj!!)
        }
        apiObservers.remove(oUrl)
        apiObservers[nUrl] = observers
    }

    /**
     * 删除指定url的网络配置provider,Retrofit,OkHttpClient
     * 如果url被其他引用，慎用此方法，一经删除需重新配置
     */
    @JvmStatic
    fun remove(baseUrl: String) {
        val url = dropLastUrl(baseUrl)
        providers.remove(url)
        clients.remove(url)
        retrofits.remove(url)
        apiObservers.remove(url)
    }

    /**
     * 关闭指定url的网络连接
     * 此操作会保留provider配置
     * 此方法只有少数情况下使用
     */
    fun close(baseUrl: String) {
        val url = dropLastUrl(baseUrl)
        val client = clients[url]
        client?.apply {
            closeDispatcherAndConnection(this)
            //清除okhttpclient、retrofit
            clients.remove(url)
            retrofits.remove(url)
            apiObservers.remove(url)
        }
    }

    /**
     * 释放线程池和连接池
     */
    fun closeDispatcherAndConnection(client: OkHttpClient) {
        client?.apply {
            val dispatcher = dispatcher()
            val connectionPool = connectionPool()
            if(dispatcher != this@STHttp.dispatcher && connectionPool != this@STHttp.connectionPool) {
                dispatcher.executorService().shutdownNow()
                dispatcher.cancelAll()
                Thread {
                    connectionPool.evictAll()
                }.start()
                cache()?.close()
                return@apply
            }
            if (dispatcher != this@STHttp.dispatcher) {
                dispatcher().executorService().shutdownNow()
                dispatcher.cancelAll()
            }else {
                if (dispatcherReferenceCount.decrementAndGet() <= 0) {
                    dispatcher.executorService().shutdownNow()
                    dispatcher.cancelAll()
                    dispatcherReferenceCount.set(0)
                }
            }
            if(connectionPool != this@STHttp.connectionPool) {
                Thread {
                    connectionPool.evictAll()
                }.start()
            }else {
                if (connectionReferenceCount.decrementAndGet() <= 0) {
                    Thread {
                        connectionPool.evictAll()
                    }.start()
                    connectionReferenceCount.set(0)
                }
            }
            if (dispatcherReferenceCount.get() <= 0
                    && connectionReferenceCount.get() <= 0) {
                cache()?.close()
            }
        }
    }

    /**
     * 清理数据
     */
    fun clear() {
        retrofits.clear()
        clients.forEach {
            closeDispatcherAndConnection(it.value)
        }
        clients.clear()
        providers.clear()
        apiObservers.clear()
    }
}