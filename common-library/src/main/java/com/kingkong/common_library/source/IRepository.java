package com.kingkong.common_library.source;

import com.dxmovie.dxbase.utils.FileUtils;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * @Description 基础数据仓库
 */
public interface IRepository {

    CacheProviders cacheProviders = new RxCache.Builder()
            .useExpiredDataIfLoaderNotAvailable(true)
            .persistence(FileUtils.getCacheFile(), new GsonSpeaker())
            .using(CacheProviders.class);

    /**
     * 清理数据
     */
    void clean();
}
