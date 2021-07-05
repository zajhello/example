package com.kingkong.common_library.interceptors;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.dxmovie.dxbase.net.ServiceException;
import com.dxmovie.dxbase.utils.GsonUtils;
import com.dxmovie.dxbase.utils.KLog;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.kingkong.common_library.helper.AppHelper;
import com.kingkong.common_library.http.encryption.SymmetricEncoder;
import com.kingkong.common_library.utils.EncryUtil;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpCodec;
import timber.log.Timber;

import static com.kingkong.common_library.helper.AppHelper.USER_OUT;
import static com.kingkong.common_library.utils.EncryUtil.isSecretRequest;

/**
 * @Description 对身份验证失效处理
 */
public abstract class TokenInterceptor implements Interceptor {

    static final String TAG = TokenInterceptor.class.getName();

    // 可重入锁
    static final Lock LOCK = new ReentrantLock();
    /**
     * 是否已经跳转到登录界面 true 跳转到登录界面 false 还没有跳转
     */
    private static AtomicBoolean toLogin = new AtomicBoolean(true);

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) {

        Request request = chain.request();

        try {

            Response response = chain.proceed(request);
            /** 解密key过期操作*/
            String decryBody = isDeckeyExpired(response);
            if (TextUtils.isEmpty(decryBody)) {
                String deckey = null;
                if (LOCK.tryLock()) {
                    try {
                        deckey = refreshDeckeySync();
                    } finally {
                        LOCK.unlock();
                    }
                }

                Timber.tag(TAG).d("deckey：%s", deckey);
                request = buildNewRequest(request);
                return chain.proceed(request);
            }

            /** token过期操作*/
            if (isTokenExpired(decryBody) && !toLogin.get()) {
                KLog.e("isTokenExpired", "   =====" + toLogin.get());
                if (LOCK.tryLock()) {
                    try {
                        toLoginActivity(USER_OUT);
                    } finally {
                        LOCK.unlock();
                    }
                }
            }

            ResponseBody body = ResponseBody.create(MediaType.parse("application/json;charset=UTF-8"), decryBody);
            return response.newBuilder()
                    .body(body)
                    .build();

        } catch (Exception e) {

            e.printStackTrace();
            String errorMessage = e.getLocalizedMessage();
            if (TextUtils.isEmpty(errorMessage))
                errorMessage = e.toString();
            ServiceException errorResponse = new ServiceException(e, -1, errorMessage);
            ResponseBody responseBody = ResponseBody.create(MediaType.parse("json"), GsonUtils.toJson(errorResponse));


            return new Response.Builder().request(request)
                    .body(responseBody)
                    .code(HttpCodec.DISCARD_STREAM_TIMEOUT_MILLIS)
                    // 这里需要跟踪
                    .message(e.getMessage())
                    .protocol(Protocol.HTTP_2)
                    .build();
        }
    }

    /**
     * 判断解密key是否过期
     */
    public abstract String isDeckeyExpired(Response response);

    /**
     * 同步刷新key
     */
    public abstract String refreshDeckeySync();

    /**
     * 判断token是否过期
     */
    public abstract boolean isTokenExpired(String decryBody);


    /**
     * 构建新的请求
     *
     * @param oldRequest
     * @return
     */
    public abstract Request buildNewRequest(Request oldRequest);

    public static void setLoginAtomic(boolean islogin) {
        toLogin.set(islogin);
    }

    public static void toLoginActivity(long status) {
        setLoginAtomic(true);
        AppHelper.INSTANCE.loginOut(status);
    }
}
