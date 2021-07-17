package com.kingkong.common_library.repository;

import com.dxmovie.dxbase.net.STHttp;
import com.dxmovie.dxbase.request.BaseRequest;
import com.dxmovie.dxbase.utils.FileUtils;
import com.dxmovie.dxbase.utils.RxUtils;
import com.kingkong.common_library.request.SecretKeyRequest;
import com.kingkong.common_library.response.SecretKeyResponse;
import com.kingkong.common_library.source.Api;
import com.kingkong.common_library.source.CacheProviders;
import com.kingkong.common_library.source.DxApi;
import com.kingkong.common_library.source.IRepository;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

public class PrecondRepository implements IRepository {


    @Override
    public void clean() {

    }

    private static PrecondRepository INSTANCE = null;

    private PrecondRepository() {
    }

    public static PrecondRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PrecondRepository();
        }
        return INSTANCE;
    }

    public Observable<SecretKeyResponse> getSecretKetWithCache(BaseRequest<SecretKeyRequest> secret) {
        return cacheProviders.getSecretKet(STHttp.get(DxApi.class).getSecretKey(secret), new DynamicKey(Api.getSecret), new EvictDynamicKey(false))
                .compose(RxUtils.exceptionIoTransformer());
    }

    public Observable<SecretKeyResponse> getSecretKet(BaseRequest<SecretKeyRequest> secret) {
        return STHttp.get(DxApi.class).getSecretKey(secret)
                .compose(RxUtils.exceptionIoTransformer());
    }
}
