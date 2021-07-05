package com.dxmovie.dxbase.net;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;

import io.reactivex.exceptions.CompositeException;
import io.rx_cache2.RxCacheException;
import retrofit2.HttpException;

/**
 * @Description 网络相应异常封装
 */
public class ExceptionHandle {

    private static final int READ_TIMEOUT = 100;
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int NOT_ALLOW = 405;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;


    public static ServiceException handleException(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }

        if (e instanceof CompositeException) {
            CompositeException compositeException = (CompositeException) e;
            if (compositeException.getExceptions().size() > 1) {
                e = handleException(compositeException.getExceptions().get(0));
            }
        }

        if (e instanceof ServiceException) {
            //如果是该类型直接返回
            return (ServiceException) e;
        }

        ServiceException ex;

        if (e instanceof HttpException) {
            /**
             * HttpException 触发域名轮训
             */
            UrlManager.switchRootUrl();
            HttpException httpException = (HttpException) e;
            String message = null;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    message = "操作未授权";
                    break;
                case FORBIDDEN:
                    message = "请求被拒绝";
                    break;
                case NOT_FOUND:
                case SERVICE_UNAVAILABLE:
                    message = "服务器不可用";
                    break;
                case REQUEST_TIMEOUT:
                    message = "服务器执行超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    message = "服务器内部错误";
                    break;
                case NOT_ALLOW:
                    message = "HTTP 405 not allowed";
                    break;
                case READ_TIMEOUT:
                    message = "读取数据超时";
                    break;
                default:
                    message = "网络错误";
                    break;
            }
            ex = new ServiceException(e, httpException.code(), message);


        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {
            ex = new ServiceException(e, ERROR.PARSE_ERROR, "解析错误");
        } else if (e instanceof ConnectException) {
            ex = new ServiceException(e, ERROR.NETWORD_ERROR, "连接失败");
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ServiceException(e, ERROR.SSL_ERROR, "证书验证失败");
        } else if (e instanceof ConnectTimeoutException
                || e instanceof RxCacheException) {
            ex = new ServiceException(e, ERROR.TIMEOUT_ERROR, "连接超时");
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ServiceException(e, ERROR.TIMEOUT_ERROR, "连接超时");
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ServiceException(e, ERROR.TIMEOUT_ERROR, "网络异常、请检查网络！");
        } else if (e instanceof CustomException) {
            ex = new ServiceException(e, ((CustomException) e).getCode(), ((CustomException) e).getErrorMessage());
        } else {
            ex = new ServiceException(e, ERROR.UNKNOWN, "未知错误");
        }
        return ex;
    }


    /**
     * 约定异常 这个具体规则需要与服务端或者领导商讨定义
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;
    }

}

