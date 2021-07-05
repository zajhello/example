package com.kingkong.common_library.source

import com.dxmovie.dxbase.response.BaseResponse
import com.kingkong.common_library.response.SecretKeyModel
import com.kingkong.common_library.response.SecretKeyResponse
import io.reactivex.Observable
import io.rx_cache2.DynamicKey
import io.rx_cache2.EvictProvider
import io.rx_cache2.LifeCache
import io.rx_cache2.Reply
import java.util.concurrent.TimeUnit


interface CacheProviders {


    /**
     * 获取平台解密key 缓存
     *
     * @return
     */
    @LifeCache(duration = 20, timeUnit = TimeUnit.SECONDS)
    fun getSecretKet(oSecretKeyModel: Observable<SecretKeyResponse>, key: DynamicKey, evictProvider: EvictProvider): Observable<SecretKeyResponse>
}