package com.kingkong.common_library.http;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.kingkong.common_library.http.encryption.SymmetricEncoder;
import com.kingkong.common_library.utils.EncryUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import static com.kingkong.common_library.utils.EncryUtil.isNeed2Encry;
import static com.kingkong.common_library.utils.EncryUtil.isSecretRequest;

public final class EncryConverterFactory extends Converter.Factory {

    private static final String TAG = EncryConverterFactory.class.getName();

    public static EncryConverterFactory create() {
        return create(new Gson(), false);
    }

    public static EncryConverterFactory create(boolean decryptContent) {
        return create(new Gson(), decryptContent);
    }

    private static EncryConverterFactory create(Gson gson, boolean decryptContent) {
        return new EncryConverterFactory(gson, decryptContent);
    }

    private Gson gson;
    private boolean decryptContent;

    private EncryConverterFactory(Gson gson, boolean decryptContent) {
        if (gson == null) throw new NullPointerException("Gson object can not be null");
        this.gson = gson;
        this.decryptContent = decryptContent;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new DecodeResponseBodyConverter<>(gson, type, annotations, decryptContent);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new EncodeRequestBodyConverter(gson, type, methodAnnotations);
    }


    static class DecodeResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private Type type;
        private Annotation[] annotations;
        private Gson gson;
        private boolean decryptContent;

        public DecodeResponseBodyConverter(Gson gson, Type type, Annotation[] annotations, boolean decryptContent) {
            this.type = type;
            this.annotations = annotations;
            this.gson = gson;
            this.decryptContent = decryptContent;
        }

        @Override
        public T convert(ResponseBody responseBody) throws IOException {

            TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
            String body = responseBody.string();
            if (decryptContent)
                body = EncryUtil.decryptBody(isSecretRequest(annotations), body);

            return adapter.fromJson(body);
        }
    }

    static class EncodeRequestBodyConverter<T> implements Converter<T, RequestBody> {
        Type type;
        Annotation[] methodAnnotations;
        private Gson gson;

        public EncodeRequestBodyConverter(Gson gson, Type type, Annotation[] methodAnnotations) {
            this.gson = gson;
            this.type = type;
            this.methodAnnotations = methodAnnotations;
        }

        @Override
        public RequestBody convert(T o) throws IOException {

            TypeAdapter<T> adapter = (TypeAdapter<T>) gson.getAdapter(TypeToken.get(type));
            String body = adapter.toJson(o);
            if (isNeed2Encry(methodAnnotations))
                body = SymmetricEncoder.encrypt4AES(body);
            return RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), body);
        }
    }
}