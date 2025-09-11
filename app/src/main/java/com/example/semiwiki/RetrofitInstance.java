package com.example.semiwiki;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static final String BASE_URL = "http://54.180.153.122:8080/";

    private static Retrofit retrofit;

    private static OkHttpClient buildClient() {
        // 네트워크 로그 확인용
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 기본 URL
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환
                    .client(buildClient()) // ← 로깅/타임아웃
                    .build();
        }
        return retrofit;
    }

    public static AuthService getAuthService() {
        return getRetrofitInstance().create(AuthService.class);
    }
}
