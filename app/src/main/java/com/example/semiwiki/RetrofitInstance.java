package com.example.semiwiki;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Retrofit 설정을 담당하는 싱글톤 클래스
public class RetrofitInstance {

    // 서버의 기본 URL (이걸 기준으로 엔드포인트들이 붙음)
    private static final String BASE_URL = "http://43.201.199.73:8080";

    // Retrofit 객체 하나만 만들기 위한 static 변수
    private static Retrofit retrofit;

    // Retrofit 인스턴스를 만들어서 리턴
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            // 처음 한 번만 생성됨 (싱글톤)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // 기본 URL 설정
                    .addConverterFactory(GsonConverterFactory.create()) // JSON 변환 처리
                    .build();
        }
        return retrofit;
    }

    // Retrofit으로 만든 AuthService 구현체를 리턴
    public static AuthService getAuthService() {
        return getRetrofitInstance().create(AuthService.class);
    }
}
