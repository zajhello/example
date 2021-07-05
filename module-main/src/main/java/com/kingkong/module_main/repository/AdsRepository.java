package com.kingkong.module_main.repository;

import com.dxmovie.dxbase.net.STHttp;
import com.dxmovie.dxbase.request.BaseRequest;
import com.dxmovie.dxbase.utils.RxUtils;
import com.kingkong.common_library.request.SecretKeyRequest;
import com.kingkong.common_library.response.SecretKeyResponse;
import com.kingkong.common_library.source.Api;
import com.kingkong.common_library.source.DxApi;
import com.kingkong.common_library.source.IRepository;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

public class AdsRepository implements IRepository {


    @Override
    public void clean() {

    }

    private static AdsRepository INSTANCE = null;

    private AdsRepository() {
    }

    public static AdsRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AdsRepository();
        }
        return INSTANCE;
    }

    public Observable<SecretKeyResponse> getSecretKetWithCache(BaseRequest<SecretKeyRequest> secret) {
        return cacheProviders.getSecretKet(STHttp.get(DxApi.class).getSecretKey(secret), new DynamicKey(Api.getSecret), new EvictDynamicKey(false))
                .compose(RxUtils.exceptionIoTransformer());
    }

    public Observable<SecretKeyResponse> getSecretKet(BaseRequest<SecretKeyRequest> secret) {
        return cacheProviders.getSecretKet(STHttp.get(DxApi.class).getSecretKey(secret), new DynamicKey(Api.getSecret), new EvictDynamicKey(true))
                .compose(RxUtils.exceptionIoTransformer());
    }
}
